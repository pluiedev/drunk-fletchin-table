package com.leocth.drunkfletchintable.block.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.LockableContainerBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos

/** LootableContainerBlockEntity, albeit without the loot. */
abstract class LootlessContainerBlockEntity(
    blockEntityType: BlockEntityType<*>,
    blockPos: BlockPos,
    blockState: BlockState
): LockableContainerBlockEntity(blockEntityType, blockPos, blockState) {

    protected abstract val inventory: DefaultedList<ItemStack>

    override fun size(): Int = inventory.size

    override fun clear() { inventory.clear() }
    override fun isEmpty() = inventory.isEmpty()
    override fun removeStack(slot: Int, amount: Int): ItemStack {
        val stack = Inventories.splitStack(inventory, slot, amount)
        if (!stack.isEmpty) markDirty()
        return stack
    }
    override fun removeStack(slot: Int): ItemStack = Inventories.removeStack(inventory, slot)
    override fun getStack(slot: Int): ItemStack = inventory[slot]
    override fun setStack(slot: Int, stack: ItemStack) {
        inventory[slot] = stack
        if (stack.count > maxCountPerStack) {
            stack.count = maxCountPerStack
        }
        markDirty()
    }

    override fun canPlayerUse(player: PlayerEntity): Boolean
        = if (world?.getBlockEntity(pos) !== this) {
            false
        } else {
            player.squaredDistanceTo(
                pos.x.toDouble() + 0.5,
                pos.y.toDouble() + 0.5,
                pos.z.toDouble() + 0.5
            ) <= 64.0
        }

}