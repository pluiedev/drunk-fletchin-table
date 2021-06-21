package com.leocth.drunkfletchintable.client.screen

import com.leocth.drunkfletchintable.DrunkFletchinTable
import com.leocth.drunkfletchintable.screen.TippingScreenHandler
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text

//TODO
class TippingScreen(
    handler: TippingScreenHandler,
    inventory: PlayerInventory,
    title: Text
) : FletchinScreen<TippingScreenHandler>(handler, inventory, title) {

    companion object {
        val TEXTURE = DrunkFletchinTable.id("textures/gui/fletchin_table/tipping.png")
    }

    override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
        super.drawBackground(matrices, delta, mouseX, mouseY)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, TEXTURE)
        drawTexture(matrices, x+45, y+17, 0, 0, 160, 61)
    }

}