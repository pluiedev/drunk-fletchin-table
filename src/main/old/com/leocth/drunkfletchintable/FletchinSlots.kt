package com.leocth.drunkfletchintable.old

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.CraftingInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.CraftingResultSlot
import net.minecraft.screen.slot.Slot

open class FletchinSlot(
    private val handler: FletchinTableScreenHandler,
    private val mode: FletchinTableMode,
    inventory: Inventory,
    index: Int, x: Int, y: Int
) : Slot(
    inventory, index, x, y
) {
    override fun canInsert(stack: ItemStack): Boolean {
        return if (handler.getDelegatedMode() === mode) super.canInsert(stack) else false
    }

    override fun canTakeItems(playerEntity: PlayerEntity?): Boolean {
        return if (handler.getDelegatedMode() === mode) super.canTakeItems(playerEntity) else false
    }

    override fun doDrawHoveringEffect(): Boolean {
        return handler.getDelegatedMode() === mode && super.doDrawHoveringEffect()
    }
}

class FletchinResultSlot(
    private val handler: FletchinTableScreenHandler,
    private val mode: FletchinTableMode,
    private val player: PlayerEntity,
    private val input: CraftingInventory,
    inventory: Inventory,
    index: Int, x: Int, y: Int
) : CraftingResultSlot(
    player, input, inventory, index, x, y
) {
    override fun canInsert(stack: ItemStack): Boolean
        = handler.getDelegatedMode() === mode && super.canInsert(stack)

    override fun canTakeItems(playerEntity: PlayerEntity?): Boolean
        = handler.getDelegatedMode() === mode && super.canTakeItems(playerEntity)

    override fun doDrawHoveringEffect(): Boolean
        = handler.getDelegatedMode() === mode && super.doDrawHoveringEffect()

    override fun onTakeItem(player: PlayerEntity, stack: ItemStack): ItemStack {
        this.onCrafted(stack)
        val remainingStacks = player.world.recipeManager.getRemainingStacks(FLETCHING_RECIPE_TYPE, input, player.world)
        for (i in remainingStacks.indices) {
            var curStack = input.getStack(i)
            val remainingStack = remainingStacks[i]
            if (!curStack.isEmpty) {
                input.removeStack(i, 1)
                curStack = input.getStack(i)
            }
            if (!remainingStack.isEmpty) {
                if (curStack.isEmpty) {
                    input.setStack(i, remainingStack)
                } else if (ItemStack.areItemsEqualIgnoreDamage(curStack, remainingStack) && ItemStack.areTagsEqual(
                        curStack,
                        remainingStack
                    )
                ) {
                    remainingStack.increment(curStack.count)
                    input.setStack(i, remainingStack)
                } else if (!this.player.inventory.insertStack(remainingStack)) {
                    this.player.dropItem(remainingStack, false)
                }
            }
        }
        return stack
    }
}

//TODO
class FletchinSlotFiltered(
    handler: FletchinTableScreenHandler,
    mode: FletchinTableMode,
    inventory: Inventory,
    index: Int, x: Int, y: Int,
    private val filter: (ItemStack) -> Boolean
) : FletchinSlot(
    handler, mode, inventory, index, x, y
) {
    override fun canInsert(stack: ItemStack): Boolean = super.canInsert(stack) && filter(stack)
}