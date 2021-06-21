package com.leocth.drunkfletchintable.screen

import com.leocth.drunkfletchintable.DrunkFletchinTable
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.CraftingInventory
import net.minecraft.inventory.CraftingResultInventory
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.CraftingResultSlot

class CraftingScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory,
    context: ScreenHandlerContext = ScreenHandlerContext.EMPTY,
): FletchinScreenHandler(TYPE, syncId, playerInventory, context) {

    private val input = CraftingInventory(this, 3, 3)
    private val result = CraftingResultInventory()

    init {
        addPlayerInventorySlots()

        addSlot(CraftingResultSlot(
            playerInventory.player,
            input, result, 0, 169, 40
        ))

        addSlotGrid(input, 3, 3, 82, 22)
    }


    companion object {
        val TYPE: ScreenHandlerType<CraftingScreenHandler>
            = ScreenHandlerRegistry.registerSimple(DrunkFletchinTable.id("crafting"), ::CraftingScreenHandler)
    }
}