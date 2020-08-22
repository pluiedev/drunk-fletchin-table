package com.leocth.drunkfletchintable

import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.fabricmc.fabric.api.server.PlayerStream
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.PacketByteBuf
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionUtil
import net.minecraft.potion.Potions
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import net.minecraft.util.Tickable
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.registry.Registry

class FletchinTableBlockEntity:
    LootlessContainerBlockEntity(FLETCHIN_TABLE_BE), Tickable, BlockEntityClientSerializable
{
    var mode = FletchinTableMode.CRAFTING
    var tippingTicks = 0
    var tippingMaxTicks = 0
    var potion: Potion = Potions.EMPTY
    var tippingUsesLeft = 0
    override val inventory: DefaultedList<ItemStack> = DefaultedList.ofSize(size(), ItemStack.EMPTY)

    private val delegate = object : PropertyDelegate {
        override fun size(): Int = 5

        override fun get(index: Int): Int {
            return when (index) {
                0 -> FletchinTableMode.values().indexOf(mode)
                1 -> tippingTicks
                2 -> tippingUsesLeft
                3 -> Registry.POTION.getRawId(potion)
                4 -> inventory[1].count
                else -> -1
            }
        }

        override fun set(index: Int, value: Int) {
            when (index) {
                0 -> mode = FletchinTableMode.values()[value]
                1 -> tippingTicks = value
                2 -> tippingUsesLeft = value
                3 -> potion = Registry.POTION.get(value)
            }
        }
    }

    companion object {
        const val TIPPING_TIME_PER_ITEM = 2
        const val TIPPING_MAX_USES = 24
    }

    override fun size(): Int = 3

    override fun createScreenHandler(syncId: Int, playerInventory: PlayerInventory): ScreenHandler
            =  FletchinTableScreenHandler(syncId, playerInventory, this, ScreenHandlerContext.create(world, pos), delegate)

    override fun getContainerName(): Text
            = TranslatableText("container.drunkfletchintable.fletchin_table")


    override fun fromTag(state: BlockState, tag: CompoundTag) {
        super.fromTag(state, tag)
        mode = try {
            FletchinTableMode.valueOf(tag.getString("mode"))
        } catch (e: IllegalArgumentException) {
            FletchinTableMode.CRAFTING
        }
        Inventories.fromTag(tag, inventory)
        tippingTicks = tag.getInt("tippingTicks")
        tippingMaxTicks = tag.getInt("tippingMaxTicks")
        tippingUsesLeft = tag.getInt("tippingUsesLeft")
        potion = Registry.POTION.get(Identifier(tag.getString("potion")))
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        super.toTag(tag)
        tag.putString("mode", mode.toString())
        tag.putInt("tippingTicks", tippingTicks)
        tag.putInt("tippingMaxTicks", tippingMaxTicks)
        tag.putInt("tippingUsesLeft", tippingUsesLeft)
        Inventories.toTag(tag, inventory)
        tag.putString("potion", Registry.POTION.getId(potion).toString())
        return tag
    }

    override fun tick() {
        if (world?.isClient == false) {
            val potionStack = getStack(0)
            val arrowStack = getStack(1)
            val resultStack = getStack(2)
            if (!potionStack.isEmpty && potion == Potions.EMPTY) {
                potion = PotionUtil.getPotion(potionStack)
                tippingUsesLeft = TIPPING_MAX_USES
                setStack(0, ItemStack(Items.GLASS_BOTTLE))
            }
            if (tippingUsesLeft >= 0 &&
                !arrowStack.isEmpty &&
                potion != Potions.EMPTY &&
                resultStack.count < resultStack.maxCount &&
                (resultStack == ItemStack.EMPTY || PotionUtil.getPotion(resultStack) == potion)
            ) {
                val amount = tippingUsesLeft.coerceAtMost(arrowStack.count)
                    .coerceAtMost(resultStack.maxCount - resultStack.count)
                tippingMaxTicks = TIPPING_TIME_PER_ITEM * amount
                ++tippingTicks
                if (tippingTicks >= tippingMaxTicks) {
                    tippingUsesLeft -= amount

                    if (resultStack.isEmpty) {
                        val newStack = ItemStack(Items.TIPPED_ARROW, amount)
                        PotionUtil.setPotion(newStack, potion)
                        PotionUtil.setCustomPotionEffects(newStack, PotionUtil.getCustomPotionEffects(potionStack))
                        setStack(2, newStack)
                    } else {
                        resultStack.count += amount
                        setStack(2, resultStack)
                    }

                    if (amount >= arrowStack.count)
                        setStack(1, ItemStack.EMPTY)
                    else {
                        arrowStack.count -= amount
                        setStack(1, arrowStack)
                    }
                    tippingTicks = 0
                    tippingMaxTicks = 0
                }
            } else {
                tippingTicks = 0
            }
            if (tippingUsesLeft <= 0) {
                potion = Potions.EMPTY
                tippingUsesLeft = 0
                tippingTicks = 0
            }
            sync()
        }
    }

    override fun toClientTag(tag: CompoundTag): CompoundTag {
        tag.putString("mode", mode.toString())
        Inventories.toTag(tag, inventory)
        tag.putInt("tippingTicks", tippingTicks)
        tag.putString("potion", Registry.POTION.getId(potion).toString())
        return tag
    }

    override fun fromClientTag(tag: CompoundTag) {
        mode = try {
            FletchinTableMode.valueOf(tag.getString("mode"))
        } catch (e: IllegalArgumentException) {
            FletchinTableMode.CRAFTING
        }
        inventory.clear()
        Inventories.fromTag(tag, inventory)
        tippingTicks = tag.getInt("tippingTicks")
        potion = Registry.POTION.get(Identifier(tag.getString("potion")))
    }
}