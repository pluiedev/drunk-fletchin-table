package com.leocth.drunkfletchintable

import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.LockableContainerBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList

/** LootableContainerBlockEntity, albeit without the loot. */
abstract class LootlessContainerBlockEntity (
    blockEntityType: BlockEntityType<*>
) : LockableContainerBlockEntity(blockEntityType) {

    protected abstract val inventory: DefaultedList<ItemStack>

    override fun clear() { inventory.clear() }
    override fun isEmpty(): Boolean = inventory.isEmpty()
    override fun removeStack(slot: Int, amount: Int): ItemStack {
        val stack = Inventories.splitStack(inventory, slot, amount)
        if (!stack.isEmpty) markDirty()
        return stack
    }
    override fun removeStack(slot: Int): ItemStack = Inventories.removeStack(inventory, slot)
    override fun getStack(slot: Int): ItemStack = inventory[slot]
    override fun setStack(slot: Int, stack: ItemStack) {
        inventory[slot] = stack
        if (stack.count > this.maxCountPerStack) {
            stack.count = this.maxCountPerStack
        }
        markDirty()
    }

    override fun canPlayerUse(player: PlayerEntity): Boolean {
        return if (world?.getBlockEntity(pos) !== this) {
            false
        } else {
            player.squaredDistanceTo(
                pos.x.toDouble() + 0.5,
                pos.y.toDouble() + 0.5,
                pos.z.toDouble() + 0.5
            ) <= 64.0
        }
    }

}