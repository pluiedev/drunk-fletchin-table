package com.leocth.drunkfletchintable.screen

import com.leocth.drunkfletchintable.DrunkFletchinTable
import com.leocth.drunkfletchintable.block.entity.modules.TippingModule
import com.leocth.drunkfletchintable.item.DftItemTags
import com.leocth.drunkfletchintable.screen.slots.SlotWithFilteredInput
import com.leocth.drunkfletchintable.screen.slots.TakeOnlySlot
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.potion.Potion
import net.minecraft.screen.ArrayPropertyDelegate
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.tag.ItemTags
import net.minecraft.util.registry.Registry

class TippingScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory,
    inventory: Inventory = SimpleInventory(3),
    private val delegate: PropertyDelegate = ArrayPropertyDelegate(TippingModule.DELEGATE_SIZE),
    context: ScreenHandlerContext = ScreenHandlerContext.EMPTY,
): FletchinScreenHandler(TYPE, syncId, playerInventory, context) {

    val potion: Potion
        @Environment(EnvType.CLIENT) get() = Registry.POTION[delegate[0]]

    val potionAmount: Int
        @Environment(EnvType.CLIENT) get() = delegate[1]

    val pouring: Boolean
        @Environment(EnvType.CLIENT) get() = delegate[2] != 0

    init {
        checkSize(inventory, 3)
        checkDataCount(delegate, TippingModule.DELEGATE_SIZE)
        addPlayerInventorySlots()

        inventory.onOpen(player)
        addProperties(delegate)

        // TODO: add tags for acceptable items
        val potionSlot = SlotWithFilteredInput(inventory, 0, 70, 25) { it.isIn(DftItemTags.POTIONS) }
        val arrowSlot = SlotWithFilteredInput(inventory, 1, 138, 61) { it.isIn(ItemTags.ARROWS) }
        val resultSlot = TakeOnlySlot(inventory, 2, 169, 61)

        addSlots(potionSlot, arrowSlot, resultSlot)
    }


    companion object {
        val TYPE: ScreenHandlerType<TippingScreenHandler>
            = ScreenHandlerRegistry.registerSimple(DrunkFletchinTable.id("tipping"), ::TippingScreenHandler)
    }
}