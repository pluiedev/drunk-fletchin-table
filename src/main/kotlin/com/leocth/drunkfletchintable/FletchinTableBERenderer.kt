package com.leocth.drunkfletchintable

import com.leocth.drunkfletchintable.tweaks.ImprovedArrowEntityRenderer
import net.minecraft.block.FletchingTableBlock
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.util.math.Vector3f
import net.minecraft.entity.EntityType
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.potion.PotionUtil
import net.minecraft.potion.Potions
import net.minecraft.state.property.Properties

class FletchinTableBERenderer(
    dispatcher: BlockEntityRenderDispatcher
) : BlockEntityRenderer<FletchinTableBlockEntity>(dispatcher)
{
    private val fakeArrowEntity = ArrowEntity(EntityType.ARROW, dispatcher.world)
    private val arrowRenderer = ImprovedArrowEntityRenderer(MinecraftClient.getInstance().entityRenderDispatcher)

    //TODO remove hardcoding
    //TODO frapi
    private val craftingNaifModel = getModel("$MODID:naif#inventory")

    private val tippingUnloadedModel = getModel("$MODID:tipping_widget_unloaded#")
    private val tippingLoadedModel = getModel("$MODID:tipping_widget_loaded#")
    private val tippingPouringModel = getModel("$MODID:tipping_widget_pouring#")
    private val tippingLiquidModel = getModel("$MODID:tipping_widget_liquid#")
    private val tippingPouringLiquidModel = getModel("$MODID:tipping_widget_pouring_liquid#")
    private val tippingBottleModel = getModel("$MODID:tipping_widget_bottle#")
    private val tippingBottleMountedModel = getModel("$MODID:tipping_widget_bottle_mounted#")


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
            FletchinTableMode.CRAFTING -> renderCrafting(blockEntity, tickDelta, matrices, vertexConsumers, newLight, overlay)
            FletchinTableMode.TIPPING -> renderTipping(blockEntity, tickDelta, matrices, vertexConsumers, newLight, overlay)
            else -> {}
        }

        matrices.pop()
    }

    private fun renderCrafting(
        blockEntity: FletchinTableBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        setTransformationAndRender(matrices,
            tX = 0.1, tY = 1.015, tZ = 0.6,
            sX = 1.1f, sY = 1.1f , sZ = 1.1f,
            rX = 90f , rY = 0f   , rZ = -150f,
            degrees = true
        )
        {
            MinecraftClient.getInstance().itemRenderer.renderItem(
                ItemStack(Items.STICK), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers
            )
        }
        setTransformationAndRender(matrices,
            tX = 0.1, tY = 1.035, tZ = 0.55,
            sX = 1.2f, sY = 1.2f , sZ = 1.2f,
            rX = 86f , rY = 0f   , rZ = -110f,
            degrees = true
        )
        {
            MinecraftClient.getInstance().itemRenderer.renderItem(
                ItemStack(Items.FEATHER), ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers
            )
        }
        setTransformationAndRender(matrices,
            tX = 0.75, tY = 1.015, tZ = 0.85,
            sX = 1.0f, sY = 0.75f , sZ = 1.0f,
            rX = -90f , rY = 0f   , rZ = 40f,
            degrees = true
        )
        {
            MinecraftClient.getInstance().itemRenderer.renderItem(
                ItemStack(Items.FEATHER), ModelTransformation.Mode.GROUND, false,
                matrices, vertexConsumers, light, overlay, craftingNaifModel
            )
        }
    }

    private fun renderTipping(
        blockEntity: FletchinTableBlockEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
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

        setTransformationAndRender(matrices,
            tX = 1.125, tY = 1.0, tZ = 1.45,
            sX = 2f, sY = 2f, sZ = 2f,
            rX = 0f, rY = 180f, rZ = 0.0f,
            degrees = true
        )
        {
            val bottleModel = if (blockEntity.potion != Potions.EMPTY) tippingBottleMountedModel else tippingBottleModel
            val stateModel = when {
                blockEntity.tippingTicks > 0 -> tippingPouringModel
                blockEntity.potion != Potions.EMPTY -> tippingLoadedModel
                else -> tippingUnloadedModel
            }
            MinecraftClient.getInstance().blockRenderManager.modelRenderer.render(
                matrices.peek(),
                vertexConsumers.getBuffer(RenderLayer.getTranslucentMovingBlock()),
                null,
                bottleModel,
                1.0f,
                1.0f,
                1.0f,
                light,
                overlay
            )
            MinecraftClient.getInstance().blockRenderManager.modelRenderer.render(
                matrices.peek(),
                vertexConsumers.getBuffer(RenderLayer.getCutoutMipped()),
                null,
                stateModel,
                1.0f,
                1.0f,
                1.0f,
                light,
                overlay
            )
            val rgb = PotionUtil.getColor(blockEntity.potion)
            val r = (rgb shr 16 and 255) / 255f
            val g = (rgb shr 8 and 255) / 255f
            val b = (rgb and 255) / 255f
            if (blockEntity.potion != Potions.EMPTY) {
                MinecraftClient.getInstance().blockRenderManager.modelRenderer.render(
                    matrices.peek(),
                    vertexConsumers.getBuffer(RenderLayer.getCutoutMipped()),
                    null,
                    tippingLiquidModel,
                    r, g, b,
                    light,
                    overlay
                )
            }
            if (blockEntity.tippingTicks > 0) {
                MinecraftClient.getInstance().blockRenderManager.modelRenderer.render(
                    matrices.peek(),
                    vertexConsumers.getBuffer(RenderLayer.getSolid()),
                    null,
                    tippingPouringLiquidModel,
                    r, g, b,
                    light,
                    overlay
                )
            }
        }
        if (doRender) {
            rotateAtCenter(matrices, Vector3f.NEGATIVE_Y, 270f, true)
            setTransformationAndRender(
                matrices,
                tX = 0.425, tY = 1.06, tZ = 0.65,
                sX = 0.85f, sY = 0.85f, sZ = 0.85f,
                rX = 2f, rY = 0f, rZ = 0f, degrees = true
            )
            {
                arrowRenderer.render(fakeArrowEntity, 0.0f, tickDelta, matrices, vertexConsumers, light)
            }
        }
    }

    private fun getModel(id: String) = MinecraftClient.getInstance().bakedModelManager.getModel(ModelIdentifier(id))

}