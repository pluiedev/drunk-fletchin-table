package com.leocth.drunkfletchintable.util

import net.minecraft.item.ItemStack

val ItemStack.isFull: Boolean get() = count >= maxCount

data class Argb(
    val a: Float,
    val r: Float,
    val g: Float,
    val b: Float,
)

fun Int.toArgb() = Argb(
    (this shr 24 and 255) / 256f,
    (this shr 16 and 255) / 256f,
    (this shr 8 and 255) / 256f,
    (this and 255) / 256f
)