package com.leocth.drunkfletchintable.widgets

import com.leocth.drunkfletchintable.MODID
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier

class CraftingScreenWidget(
    private val client: MinecraftClient?,
    private val x: Int,
    private val y: Int
): FletchinScreenWidget() {

    companion object {
        val TEXTURE = Identifier(MODID, "textures/gui/fletchin_table/crafting.png")
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean? = null

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        client?.textureManager?.bindTexture(TEXTURE)
        drawTexture(matrices, x+45, y+17, 0, 0, 160, 61)
    }
}