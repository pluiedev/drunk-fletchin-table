package com.leocth.drunkfletchintable

import com.leocth.drunkfletchintable.mixin.FletchingTableMixin
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.LiteralText
import net.minecraft.text.Style
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.World

class TinyTaterrBlokk : Block(
    FabricBlockSettings.of(Material.WOOD)
        .hardness(0.2f)
        .nonOpaque()
        .sounds(BlockSoundGroup.WOOD)
) {
    companion object {
        val FACING = Properties.HORIZONTAL_FACING
        val SHAPE: VoxelShape = createCuboidShape(6.0, 0.0, 6.0, 10.0, 5.75, 10.0)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape = SHAPE

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState
            = defaultState.with(FACING, ctx.playerFacing.opposite)

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        if (!world.isClient) {
            val text = LiteralText("Fabric! :tiny_potato:")
            text.styled {
                it
                    .withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, LiteralText("Click to join Fabricord! :winktato:")))
                    .withClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/v6v4pMv")) }
            player.sendMessage(text, false)
        }
        return ActionResult.SUCCESS
    }

}