package com.leocth.drunkfletchintable.screen.slots

import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot

class SlotWithFilteredInput(
    inventory: Inventory,
    index: Int,
    x: Int, y: Int,
    private val filter: (ItemStack) -> Boolean
) : Slot(inventory, index, x, y) {
    override fun canInsert(stack: ItemStack): Boolean {
        return super.canInsert(stack) && filter(stack)
    }
}