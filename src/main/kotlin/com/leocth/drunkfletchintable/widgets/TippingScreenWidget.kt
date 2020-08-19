package com.leocth.drunkfletchintable.widgets

import com.leocth.drunkfletchintable.FletchinTableBlockEntity.Companion.TIPPING_MAX_USES
import com.leocth.drunkfletchintable.FletchinTableScreenHandler
import com.leocth.drunkfletchintable.MODID
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.PotionItem
import net.minecraft.potion.PotionUtil
import net.minecraft.potion.Potions
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import net.minecraft.util.registry.Registry

class TippingScreenWidget(
    private val client: MinecraftClient?,
    private val x: Int,
    private val y: Int,
    private val handler: FletchinTableScreenHandler
): DrawableHelper(), Drawable {

    companion object {
        val TEXTURE = Identifier(MODID, "textures/gui/fletchin_table/tipping.png")
    }

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        client?.textureManager?.bindTexture(TEXTURE)
        drawTexture(matrices, x+45, y+17, 0, 0, 160, 61)
        val potion = handler.getPotion()
        if (potion != Potions.EMPTY) {
            val color = PotionUtil.getColor(potion)
            val progress = handler.getDisplayProgress()
            val prog = handler.getTippingProgress()
            val offset = (34 * progress / TIPPING_MAX_USES).toInt()
            val r = (color shr 16 and 255) / 255f
            val g = (color shr 8  and 255) / 255f
            val b = (color        and 255) / 255f
            RenderSystem.color4f(r, g, b, 1.0f)
            drawTexture(matrices, x+45, y+51-offset, 0, 95-offset, 100, offset)
            if (prog > 0f) {
                drawTexture(matrices, x+167, y+42, 122, 86, 27, 36)
            }
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        }
    }
}