package com.leocth.drunkfletchintable

import com.leocth.drunkfletchintable.FletchinTableBlockEntity.Companion.TIPPING_TIME_PER_ITEM
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.CraftingInventory
import net.minecraft.inventory.CraftingResultInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.PotionItem
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket
import net.minecraft.potion.Potion
import net.minecraft.potion.Potions
import net.minecraft.screen.ArrayPropertyDelegate
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.slot.Slot
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.registry.Registry
import java.util.*

class FletchinTableScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory,
    private val inventory: Inventory,
    val context: ScreenHandlerContext,
    private val delegate: PropertyDelegate
): ScreenHandler(FLETCHIN_TABLE_SH, syncId)
{
    private val player: PlayerEntity = playerInventory.player
    private val craftingSlotIndex = playerInventory.size() - playerInventory.armor.size
    private val tippingSlotIndex = craftingSlotIndex + 10
    private val fletchingInput = CraftingInventory(this, 3, 3)
    private val fletchingResult = CraftingResultInventory()

    // Client
    constructor(syncId: Int, playerInventory: PlayerInventory)
            : this(syncId, playerInventory, SimpleInventory(3), ScreenHandlerContext.EMPTY, ArrayPropertyDelegate(5))

    init {
        checkSize(inventory, 3)
        checkDataCount(delegate, 5)
        // main
        for (i in 0 until 3) {
            for (j in 0 until 9) {
                addSlot(Slot(playerInventory, i * 9 + j + 9, 45 + j * 18, 93 + i * 18))
            }
        }

        // hotbar
        for (j in 0 until 9) {
            addSlot(Slot(playerInventory, j, 45 + j * 18, 151))
        }

        // offhand
        addSlot(Slot(playerInventory, 40, 10, 151))

        // crafting
        addSlot(FletchinResultSlot(
            this, FletchinTableMode.CRAFTING, playerInventory.player,
            fletchingInput, fletchingResult, 0, 169, 40
        ))
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                addSlot(FletchinSlot(this, FletchinTableMode.CRAFTING,
                    fletchingInput, i * 3 + j, 82 + j * 18, 22 + i * 18
                ))
            }
        }

        // tipping
        addSlot(FletchinSlotFiltered(
            this, FletchinTableMode.TIPPING, inventory, 0, 70, 25)
            {
                it.item is PotionItem
            }
        )
        addSlot(FletchinSlotFiltered(
            this, FletchinTableMode.TIPPING, inventory, 1, 138, 61)
            {
                //TODO
                it.item == Items.ARROW
            }
        )
        addSlot(object : FletchinSlot(
            this, FletchinTableMode.TIPPING, inventory, 2, 169, 61)
            {
                override fun canInsert(stack: ItemStack): Boolean = false

            }
        )

        addProperties(delegate)
    }

    @Environment(EnvType.CLIENT)
    fun getDelegatedMode(): FletchinTableMode {
        return FletchinTableMode.values()[delegate[0]]
    }

    fun setDelegatedMode(mode: FletchinTableMode) {
        delegate.set(0, mode.ordinal)
        sendContentUpdates()
    }


    override fun sendContentUpdates() {
        super.sendContentUpdates()
        context.run { world, pos ->
            val be = world.getBlockEntity(pos)
            if (be is FletchinTableBlockEntity) {
                be.sync()
            }
        }
    }

    @Environment(EnvType.CLIENT)
    fun getPotion(): Potion = Registry.POTION.get(delegate[3])

    @Environment(EnvType.CLIENT)
    fun getDisplayProgress(): Int = delegate[2] - delegate[1] / TIPPING_TIME_PER_ITEM

    @Environment(EnvType.CLIENT)
    fun getPotionUsesUsing(): Int = delegate[4]

    @Environment(EnvType.CLIENT)
    fun getTippingProgress(): Float = delegate[1] / getPotionUsesUsing().toFloat()

    fun drainPotion() {
        delegate[3] = Registry.POTION.getRawId(Potions.EMPTY)
        delegate[2] = 0
    }

    override fun close(player: PlayerEntity?) {
        super.close(player)
        context.run { world, pos ->
            val be = world.getBlockEntity(pos)
            if (be is FletchinTableBlockEntity) {
                be.mode = getDelegatedMode()
            }
            dropInventory(player, world, this.fletchingInput)
        }
    }


    override fun canUse(player: PlayerEntity): Boolean = canUse(context, player, Blocks.FLETCHING_TABLE)

    override fun onContentChanged(inventory: Inventory) {
        context.run { world, _ ->
            if (!world.isClient) {
                val serverPlayerEntity = player as ServerPlayerEntity
                var itemStack = ItemStack.EMPTY
                if (getDelegatedMode() === FletchinTableMode.CRAFTING) {
                    val recipeOpt: Optional<FletchingRecipe>? =
                        world.server?.recipeManager?.getFirstMatch(FLETCHING_RECIPE_TYPE, fletchingInput, world)
                    if (recipeOpt?.isPresent == true) {
                        val recipe = recipeOpt.get()
                        if (fletchingResult.shouldCraftRecipe(world, serverPlayerEntity, recipe)) {
                            itemStack = recipe.craft(fletchingInput)
                        }
                    }
                    fletchingResult.setStack(craftingSlotIndex, itemStack)
                    serverPlayerEntity.networkHandler.sendPacket(
                        ScreenHandlerSlotUpdateS2CPacket(
                            syncId,
                            craftingSlotIndex,
                            itemStack
                        )
                    )
                }
            }
        }
    }

    override fun transferSlot(player: PlayerEntity, index: Int): ItemStack? {
        var newStack = ItemStack.EMPTY
        val slot = slots[index]
        if (slot != null && slot.hasStack()) {
            val oldStack = slot.stack
            newStack = oldStack.copy()
            when (getDelegatedMode()) {
                FletchinTableMode.CRAFTING -> {
                    if (index == craftingSlotIndex) {
                        context.run { world, _ ->
                            oldStack.item.onCraft(oldStack, world, player)
                        }
                        if (!insertItem(oldStack, 0, 36, true)) {
                            return ItemStack.EMPTY
                        }
                        slot.onStackChanged(oldStack, newStack)
                    } else if (index in 0 until 37) {
                        if (!insertItem(oldStack, craftingSlotIndex+1, craftingSlotIndex+9, false)) {
                            if (index in 0..26) {
                                if (!insertItem(oldStack, 27, 36, false)) {
                                    return ItemStack.EMPTY
                                }
                            } else if (!insertItem(oldStack, 0, 27, false)) {
                                return ItemStack.EMPTY
                            }
                        }
                    } else if (!insertItem(oldStack, 0, 36, false)) {
                        return ItemStack.EMPTY
                    }
                }
                FletchinTableMode.TIPPING -> {
                    if (index in 0 until 37) {
                        if (!insertItem(oldStack, tippingSlotIndex, tippingSlotIndex+1, false)) {
                            if (index in 0..26) {
                                if (!insertItem(oldStack, 27, 36, false)) {
                                    return ItemStack.EMPTY
                                }
                            } else if (!insertItem(oldStack, 0, 27, false)) {
                                return ItemStack.EMPTY
                            }
                        }
                    } else if (!insertItem(oldStack, 0, 36, false)) {
                        return ItemStack.EMPTY
                    }
                }
                else -> {}
            }
            if (oldStack.isEmpty) {
                slot.stack = ItemStack.EMPTY
            } else {
                slot.markDirty()
            }
            if (oldStack.count == newStack.count) {
                return ItemStack.EMPTY
            }
            val itemStack3 = slot.onTakeItem(player, oldStack)
            if (index == 0) {
                player.dropItem(itemStack3, false)
            }
        }

        return newStack
    }


}
