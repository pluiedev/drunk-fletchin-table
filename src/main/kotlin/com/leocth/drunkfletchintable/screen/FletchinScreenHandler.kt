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
    private val playerInventory: PlayerInventory,
    private val context: ScreenHandlerContext = ScreenHandlerContext.EMPTY,
): ScreenHandler(type, syncId) {

    fun addPlayerInventorySlots(x: Int, y: Int) {
        addSlotGrid(playerInventory, 1, 9, x, y)
        addSlotGrid(playerInventory, 3, 9, x, y, startIndex = 9)

        for (i in 0 until 3) {
            for (j in 0 until 9) {
                addSlot(Slot(playerInventory, i * 9 + j + 9, 45 + j * 18, 93 + i * 18))
            }
        }

        // hotbar
        for (j in 0 until 9) {
            addSlot(Slot(playerInventory, j, 45 + j * 18, 151))
        }
    }

    fun addSlotGrid(
        inventory: Inventory,
        row: Int, column: Int,
        x: Int, y: Int,
        startIndex: Int = 0,
        slotOffsetX: Int = 18,
        slotOffsetY: Int = slotOffsetX
    ) {
        var curX = x
        var curY = y
        var index = startIndex

        for (r in 0 until row) {
            for (c in 0 until column) {
                addSlot(Slot(playerInventory, index, curX, curY))
                index++
                curX += slotOffsetX
            }
            curX = 0
            curY += slotOffsetY
        }
    }

    override fun canUse(player: PlayerEntity) = canUse(context, player, Blocks.FLETCHING_TABLE)
}