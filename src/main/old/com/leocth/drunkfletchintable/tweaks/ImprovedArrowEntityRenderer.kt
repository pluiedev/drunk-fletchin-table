package com.leocth.drunkfletchintable.old.tweaks

import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.ArrowEntityRenderer
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.ProjectileEntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.util.math.Vector3f
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Matrix3f
import net.minecraft.util.math.Matrix4f

/**
 * tipped arrows and normal arrows look exactly the same in vanilla.
 * :mojank:
 */

class ImprovedArrowEntityRenderer(
    entityRenderDispatcher: EntityRenderDispatcher
) : ProjectileEntityRenderer<ArrowEntity>(entityRenderDispatcher)
{
    override fun render(
        arrowEntity: ArrowEntity,
        pitchDelta: Float,
        yawDelta: Float,
        matrixStack: MatrixStack,
        vertexConsumerProvider: VertexConsumerProvider,
        light: Int
    ) {
        matrixStack.push()
        matrixStack.multiply(
            Vector3f.POSITIVE_Y.getDegreesQuaternion(
                MathHelper.lerp(
                    yawDelta,
                    arrowEntity.prevYaw,
                    arrowEntity.yaw
                ) - 90.0f
            )
        )
        matrixStack.multiply(
            Vector3f.POSITIVE_Z.getDegreesQuaternion(
                MathHelper.lerp(
                    yawDelta,
                    arrowEntity.prevPitch,
                    arrowEntity.pitch
                )
            )
        )
        val s = arrowEntity.shake.toFloat() - yawDelta
        if (s > 0.0f) {
            val t = -MathHelper.sin(s * 3.0f) * s
            matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(t))
        }
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(45.0f))
        matrixStack.scale(0.05625f, 0.05625f, 0.05625f)
        matrixStack.translate(-4.0, 0.0, 0.0)
        val vertexConsumer =
            vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(getTexture(arrowEntity)))
        val entry = matrixStack.peek()
        val matrix4f = entry.model
        val matrix3f = entry.normal
        draw(matrix4f, matrix3f, vertexConsumer, -7, -2, -2, 0.0f, 0.15625f, -1, 0, 0, light)
        draw(matrix4f, matrix3f, vertexConsumer, -7, -2, 2, 0.15625f, 0.15625f, -1, 0, 0, light)
        draw(matrix4f, matrix3f, vertexConsumer, -7, 2, 2, 0.15625f, 0.3125f, -1, 0, 0, light)
        draw(matrix4f, matrix3f, vertexConsumer, -7, 2, -2, 0.0f, 0.3125f, -1, 0, 0, light)
        draw(matrix4f, matrix3f, vertexConsumer, -7, 2, -2, 0.0f, 0.15625f, 1, 0, 0, light)
        draw(matrix4f, matrix3f, vertexConsumer, -7, 2, 2, 0.15625f, 0.15625f, 1, 0, 0, light)
        draw(matrix4f, matrix3f, vertexConsumer, -7, -2, 2, 0.15625f, 0.3125f, 1, 0, 0, light)
        draw(matrix4f, matrix3f, vertexConsumer, -7, -2, -2, 0.0f, 0.3125f, 1, 0, 0, light)
        for (u in 0..3) {
            matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0f))
            draw(matrix4f, matrix3f, vertexConsumer, -8, -2, 0, 0.0f, 0.0f, 0, 1, 0, light)
            draw(matrix4f, matrix3f, vertexConsumer, 4, -2, 0, 0.375f, 0.0f, 0, 1, 0, light)
            draw(matrix4f, matrix3f, vertexConsumer, 4, 2, 0, 0.375f, 0.15625f, 0, 1, 0, light)
            draw(matrix4f, matrix3f, vertexConsumer, -8, 2, 0, 0.0f, 0.15625f, 0, 1, 0, light)

            draw(matrix4f, matrix3f, vertexConsumer, 4, -2, 0, 0.40625f, 0.0f, 0, 1, 0, light, arrowEntity.color)
            draw(matrix4f, matrix3f, vertexConsumer, 8, -2, 0, 0.5f, 0.0f, 0, 1, 0, light, arrowEntity.color)
            draw(matrix4f, matrix3f, vertexConsumer, 8, 2, 0, 0.5f, 0.15625f, 0, 1, 0, light, arrowEntity.color)
            draw(matrix4f, matrix3f, vertexConsumer, 4, 2, 0, 0.40625f, 0.15625f, 0, 1, 0, light, arrowEntity.color)
        }
        matrixStack.pop()
    }

    override fun getTexture(arrowEntity: ArrowEntity): Identifier
        = if (arrowEntity.color > 0) ArrowEntityRenderer.TIPPED_TEXTURE else ArrowEntityRenderer.TEXTURE

    /**
     * just to protect my sanity.
     */
    fun draw(
        model: Matrix4f,
        normal: Matrix3f,
        vertexConsumer: VertexConsumer,
        x: Int,
        y: Int,
        z: Int,
        u: Float,
        v: Float,
        nX: Int,
        nY: Int,
        nZ: Int,
        light: Int,
        color: Int = -1
    ) {
        vertexConsumer
            .vertex(model, x.toFloat(), y.toFloat(), z.toFloat())
            .color(
                color shr 16 and 255,
                color shr 8 and 255,
                color and 255,
                255
            )
            .texture(u, v)
            .overlay(OverlayTexture.DEFAULT_UV)
            .light(light)
            .normal(normal, nX.toFloat(), nY.toFloat(), nZ.toFloat())
            .next()
    }
}