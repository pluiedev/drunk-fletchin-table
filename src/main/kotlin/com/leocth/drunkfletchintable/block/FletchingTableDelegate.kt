package com.leocth.drunkfletchintable.block

import com.leocth.drunkfletchintable.DftItems
import com.leocth.drunkfletchintable.DrunkFletchinTable
import com.leocth.drunkfletchintable.block.entity.FletchinTableBlockEntity
import com.leocth.drunkfletchintable.block.entity.modules.CraftingModule
import com.leocth.drunkfletchintable.block.entity.modules.ModuleProvider
import com.leocth.drunkfletchintable.block.entity.modules.TippingModule
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.Items
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.math.roundToInt

object FletchingTableDelegate {
    private val FACING: DirectionProperty = Properties.HORIZONTAL_FACING

    @JvmStatic
    fun getPlacementState(defaultState: BlockState, ctx: ItemPlacementContext): BlockState
        = defaultState.with(FACING, ctx.playerFacing.opposite)

    @JvmStatic
    fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity
        = FletchinTableBlockEntity(pos, state)

    @JvmStatic
    fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand, hit: BlockHitResult): ActionResult {
        if (!world.isClient) {
            val stack = player.getStackInHand(hand)
            val item = stack.item
            if (item is ModuleProvider<*>) {
                val blockEntity = world.getBlockEntity(pos)
                if (blockEntity is FletchinTableBlockEntity) {
                    val modules = blockEntity.modules
                    val side = hit.side

                    if (!modules.contains(side)) {
                        modules[side] = item.getModule(blockEntity)
                        blockEntity.sync()
                        return ActionResult.SUCCESS
                    }
                }
            }
        }
        return ActionResult.PASS
    }

    @JvmStatic
    fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING)
    }
}

@Suppress("NOTHING_TO_INLINE")
inline operator fun Vec3d.minus(other: Vec3d): Vec3d = subtract(other)


@Suppress("NOTHING_TO_INLINE")
inline operator fun Vec3d.times(scale: Double): Vec3d = multiply(scale)