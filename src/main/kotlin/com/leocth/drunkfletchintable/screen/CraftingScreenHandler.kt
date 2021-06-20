package com.leocth.drunkfletchintable.screen

import com.leocth.drunkfletchintable.DrunkFletchinTable
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.ScreenHandlerType

class CraftingScreenHandler(
    syncId: Int,
    private val playerInventory: PlayerInventory,
    private val context: ScreenHandlerContext = ScreenHandlerContext.EMPTY,
): FletchinScreenHandler(TYPE, syncId, playerInventory, context) {


    companion object {
        val TYPE: ScreenHandlerType<CraftingScreenHandler>
            = ScreenHandlerRegistry.registerSimple(DrunkFletchinTable.id("crafting"), ::CraftingScreenHandler)
    }
}