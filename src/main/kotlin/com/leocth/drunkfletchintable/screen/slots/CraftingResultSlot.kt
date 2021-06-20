package com.leocth.drunkfletchintable.screen.slots

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.CraftingInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.CraftingResultSlot

class CraftingResultSlot(
    player: PlayerEntity,
    input: CraftingInventory,
    inventory: Inventory,
    index: Int,
    x: Int, y: Int
): CraftingResultSlot(player, input, inventory, index, x, y) {
    override fun onTakeItem(player: PlayerEntity, stack: ItemStack) {
        /* TODO implement custom recipe type
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
         */
    }
}