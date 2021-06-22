package com.leocth.drunkfletchintable.screen

import com.leocth.drunkfletchintable.DrunkFletchinTable
import com.leocth.drunkfletchintable.item.DftItemTags
import com.leocth.drunkfletchintable.screen.slots.SlotWithFilteredInput
import com.leocth.drunkfletchintable.screen.slots.TakeOnlySlot
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.CraftingInventory
import net.minecraft.inventory.CraftingResultInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionUtil
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.screen.slot.CraftingResultSlot
import net.minecraft.screen.slot.Slot
import net.minecraft.tag.ItemTags

class TippingScreenHandler(
    syncId: Int,
    playerInventory: PlayerInventory,
    inventory: Inventory = SimpleInventory(3),
    context: ScreenHandlerContext = ScreenHandlerContext.EMPTY,
): FletchinScreenHandler(TYPE, syncId, playerInventory, context) {

    init {
        checkSize(inventory, 3)
        addPlayerInventorySlots()

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