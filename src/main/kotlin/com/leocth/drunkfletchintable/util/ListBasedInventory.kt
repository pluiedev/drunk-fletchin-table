package com.leocth.drunkfletchintable.util

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import kotlin.reflect.KMutableProperty0


/**
 * A list-based Inventory implementation.
 *
 * Heavily based on ImplementedInventory, originally by Juuz
 */
open class ListBasedInventory(
    private val items: List<KMutableProperty0<ItemStack>>
): Inventory {

    constructor(
        vararg items: KMutableProperty0<ItemStack>
    ): this(listOf(*items))

    /**
     * Returns the inventory size.
     */
    override fun size(): Int = items.size

    /**
     * Checks if the inventory is empty.
     * @return true if this inventory has only empty stacks, false otherwise.
     */
    override fun isEmpty(): Boolean {
        for (i in 0 until size()) {
            val stack = getStack(i)
            if (!stack.isEmpty)
                return false
        }
        return true
    }

    /**
     * Retrieves the item in the slot.
     */
    override fun getStack(slot: Int): ItemStack {
        return items[slot].get()
    }

    /**
     * Removes items from an inventory slot.
     * @param slot  The slot to remove from.
     * @param count How many items to remove. If there are less items in the slot than what are requested,
     * takes all items in that slot.
     */
    override fun removeStack(slot: Int, count: Int): ItemStack {
        val result = splitStack(items, slot, count)
        if (!result.isEmpty) markDirty()
        return result
    }

    /**
     * Removes all items from an inventory slot.
     * @param slot The slot to remove from.
     */
    override fun removeStack(slot: Int): ItemStack {
        return removeStack(items, slot)
    }

    /**
     * Replaces the current stack in an inventory slot with the provided stack.
     * @param slot  The inventory slot of which to replace the itemstack.
     * @param stack The replacing itemstack. If the stack is too big for
     * this inventory ([Inventory.getMaxCountPerStack]),
     * it gets resized to this inventory's maximum amount.
     */
    override fun setStack(slot: Int, stack: ItemStack) {
        items[slot].set(stack)
        if (stack.count > maxCountPerStack) {
            stack.count = maxCountPerStack
        }
    }

    /**
     * Clears the inventory.
     */
    override fun clear() {
        for (i in items) {
            i.set(ItemStack.EMPTY)
        }
    }

    /**
     * Marks the state as dirty.
     * Must be called after changes in the inventory, so that the game can properly save
     * the inventory contents and notify neighboring blocks of inventory changes.
     */
    override fun markDirty() {
        // Override if you want behavior.
    }

    /**
     * @return true if the player can use the inventory, false otherwise.
     */
    override fun canPlayerUse(player: PlayerEntity): Boolean = true

    // cursed
    companion object {
        private fun splitStack(items: List<KMutableProperty0<ItemStack>>, slot: Int, count: Int): ItemStack {
            val stack = items[slot].get()
            return if (slot in items.indices && !(stack.isEmpty && count > 0))
                stack.split(count)
            else ItemStack.EMPTY
        }

        private fun removeStack(items: List<KMutableProperty0<ItemStack>>, slot: Int): ItemStack {
            val stack = items[slot].get()
            return if (slot in items.indices) {
                items[slot].set(ItemStack.EMPTY)
                stack
            } else ItemStack.EMPTY
        }
    }
}