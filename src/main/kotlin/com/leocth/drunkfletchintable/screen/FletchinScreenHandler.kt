package com.leocth.drunkfletchintable.screen

import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.Slot

abstract class FletchinScreenHandler(
    type: ScreenHandlerType<*>,
    syncId: Int,
    val playerInventory: PlayerInventory,
    val context: ScreenHandlerContext = ScreenHandlerContext.EMPTY,
): ScreenHandler(type, syncId) {
    val player: PlayerEntity = playerInventory.player

    protected fun addPlayerInventorySlots(
        x: Int = 45,
        y: Int = 93,
        hotbarY: Int = 151,
        offHandX: Int = 10,
    ) {
        // hotbar
        addSlotGrid(playerInventory, 1, 9, x, y)

        // main
        addSlotGrid(playerInventory, 3, 9, x, hotbarY, startIndex = 9)

        // offhand
        addSlot(Slot(playerInventory, 40, offHandX, hotbarY))
    }

    protected fun addSlotGrid(
        inventory: Inventory,
        row: Int, column: Int,
        x: Int, y: Int,
        startIndex: Int = 0,
        slotOffsetX: Int = 18,
        slotOffsetY: Int = slotOffsetX,
        addSlot: (Inventory, Int, Int, Int) -> Unit = this::addSlot
    ) {
        var curX = x
        var curY = y
        var index = startIndex

        for (r in 0 until row) {
            for (c in 0 until column) {
                addSlot(inventory, index, curX, curY)
                index++
                curX += slotOffsetX
            }
            curX = 0
            curY += slotOffsetY
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    protected inline fun addSlot(inventory: Inventory, index: Int, x: Int, y: Int) {
        addSlot(Slot(inventory, index, x, y))
    }

    protected fun addSlots(vararg slots: Slot) {
        for (slot in slots)
            addSlot(slot)
    }

    override fun canUse(player: PlayerEntity) = canUse(context, player, Blocks.FLETCHING_TABLE)
}