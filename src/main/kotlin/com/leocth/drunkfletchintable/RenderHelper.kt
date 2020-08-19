package com.leocth.drunkfletchintable

import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.util.math.Vector3f
import net.minecraft.util.math.Quaternion

fun setTransformationAndRender(
    matrices: MatrixStack,
    tX: Double = 0.5, tY: Double = 0.5, tZ: Double = 0.5,
    sX: Float  = 1.0f, sY: Float  = 1.0f, sZ: Float  = 1.0f,
    rX: Float  = 0.0f, rY: Float  = 0.0f, rZ: Float  = 0.0f,
    degrees: Boolean = false,
    func: (MatrixStack) -> Unit
)
{
    matrices.push()
    matrices.translate(tX, tY, tZ)
    matrices.scale(sX, sY, sZ)
    matrices.multiply(Quaternion(Vector3f.POSITIVE_X, rX, degrees))
    matrices.multiply(Quaternion(Vector3f.POSITIVE_Y, rY, degrees))
    matrices.multiply(Quaternion(Vector3f.POSITIVE_Z, rZ, degrees))
    func(matrices)
    matrices.pop()
}

fun rotateAtCenter(
    matrices: MatrixStack,
    axis: Vector3f,
    angle: Float,
    degrees: Boolean
)
{
    matrices.translate(0.5, 0.5, 0.5)
    matrices.multiply(Quaternion(axis, angle, degrees))
    matrices.translate(-0.5, -0.5, -0.5)
}