package com.leocth.drunkfletchintable.screen.slots

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot

class TakeOnlySlot(
    inventory: Inventory,
    index: Int,
    x: Int, y: Int
) : Slot(inventory, index, x, y) {
    override fun canInsert(stack: ItemStack): Boolean = false
}