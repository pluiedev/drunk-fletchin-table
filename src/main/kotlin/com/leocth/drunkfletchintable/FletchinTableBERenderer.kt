package com.leocth.drunkfletchintable

import com.leocth.drunkfletchintable.tweaks.ImprovedArrowEntityRenderer
import net.minecraft.block.FletchingTableBlock
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.util.math.Vector3f
import net.minecraft.entity.EntityType
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.state.property.Properties

class FletchinTableBERenderer(
    dispatcher: BlockEntityRenderDispatcher
) : BlockEntityRenderer<FletchinTableBlockEntity>(dispatcher)
{
    val fakeArrowEntity = ArrowEntity(EntityType.ARROW, dispatcher.world)
    val arrowRenderer = ImprovedArrowEntityRenderer(MinecraftClient.getInstance().entityRenderDispatcher)

    override fun render(
        blockEntity: FletchinTableBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        val newLight = WorldRenderer.getLightmapCoordinates(blockEntity.world, blockEntity.pos.up())

        val block = blockEntity.world?.getBlockState(blockEntity.pos)
        val rotation = if (block?.block is FletchingTableBlock) {
            block[Properties.HORIZONTAL_FACING].asRotation()
        } else 0f

        matrices.push()
        rotateAtCenter(matrices, Vector3f.NEGATIVE_Y, rotation, true)

        when (blockEntity.mode) {
            FletchinTableMode.CRAFTING -> {
                setTransformationAndRender(matrices,
                    tX = 0.15, tY = 1.015, tZ = 0.6,
                    sX = 1.2f, sY = 1.2f , sZ = 1.2f,
                    rX = 90f , rY = 0f   , rZ = -150f,
                    degrees = true
                )
                {
                    MinecraftClient.getInstance().itemRenderer.renderItem(
                        ItemStack(Items.STICK), ModelTransformation.Mode.GROUND, newLight, overlay, matrices, vertexConsumers
                    )
                }
                setTransformationAndRender(matrices,
                    tX = 0.15, tY = 1.035, tZ = 0.55,
                    sX = 1.2f, sY = 1.2f , sZ = 1.2f,
                    rX = 86f , rY = 0f   , rZ = -110f,
                    degrees = true
                )
                {
                    MinecraftClient.getInstance().itemRenderer.renderItem(
                        ItemStack(Items.FEATHER), ModelTransformation.Mode.GROUND, newLight, overlay, matrices, vertexConsumers
                    )
                }
            }
            FletchinTableMode.TIPPING -> {
                val arrowStack = blockEntity.getStack(1)
                val resultStack = blockEntity.getStack(2)

                val doRender = if (!resultStack.isEmpty) {
                    fakeArrowEntity.initFromStack(resultStack)
                    true
                }
                else if (!arrowStack.isEmpty) {
                    fakeArrowEntity.initFromStack(arrowStack)
                    true
                }
                else false

                if (doRender) {
                    rotateAtCenter(matrices, Vector3f.NEGATIVE_Y, 270f, true)
                    setTransformationAndRender(
                        matrices,
                        tX = 0.325, tY = 1.06, tZ = 0.65,
                        sX = 0.85f, sY = 0.85f, sZ = 0.85f,
                        rX = 2f, rY = 0f, rZ = 0f, degrees = true
                    )
                    {
                        arrowRenderer.render(fakeArrowEntity, 0.0f, tickDelta, matrices, vertexConsumers, newLight)
                    }
                }
            }
            else -> {}
        }

        matrices.pop()
    }



}