package com.leocth.drunkfletchintable

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.CraftingInventory
import net.minecraft.inventory.Inventory
import net.minecraft.item.Item
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
    player: PlayerEntity,
    input: CraftingInventory,
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
}

//TODO
class FletchinSlotFiltered(
    private val handler: FletchinTableScreenHandler,
    private val mode: FletchinTableMode,
    inventory: Inventory,
    index: Int, x: Int, y: Int,
    private val filter: (ItemStack) -> Boolean
) : FletchinSlot(
    handler, mode, inventory, index, x, y
) {
    override fun canInsert(stack: ItemStack): Boolean = super.canInsert(stack) && filter(stack)
}