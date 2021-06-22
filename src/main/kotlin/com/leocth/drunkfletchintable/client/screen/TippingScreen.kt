package com.leocth.drunkfletchintable.client.screen

import com.leocth.drunkfletchintable.DrunkFletchinTable
import com.leocth.drunkfletchintable.block.entity.modules.USES_PER_POTION_ITEM
import com.leocth.drunkfletchintable.screen.TippingScreenHandler
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.potion.Potion
import net.minecraft.potion.Potions
import net.minecraft.text.Text
import net.minecraft.util.math.MathHelper

//TODO
class TippingScreen(
    handler: TippingScreenHandler,
    inventory: PlayerInventory,
    title: Text
) : FletchinScreen<TippingScreenHandler>(handler, inventory, title) {

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val potion = handler.potion
        val amount = handler.potionAmount
        if (potion != Potions.EMPTY) {
            val height = getHeightFromAmount(amount)
            val mX = mouseX - x
            val mY = mouseY - y
            val yOffset = 51.0 - height

            if (mX in 90.0..145.0 && mY in yOffset..51.0 && Screen.hasShiftDown()) {
                handler.potionAmount = 0
                return true
            }
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
        super.drawBackground(matrices, delta, mouseX, mouseY)
        drawModuleBg(matrices, TEXTURE)

        val potion = handler.potion
        if (potion != Potions.EMPTY)
            drawPotionOverlay(matrices, delta, mouseX, mouseY, potion)
    }

    private fun drawPotionOverlay(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int, potion: Potion) {
        val amount = handler.potionAmount

        val height = getHeightFromAmount(amount)
        MathHelper.
    }

    companion object {
        private val TEXTURE = DrunkFletchinTable.id("textures/gui/fletchin_table/tipping.png")
        private const val MAX_HEIGHT = 34

        private fun getHeightFromAmount(amount: Int): Float
            = MAX_HEIGHT * amount / USES_PER_POTION_ITEM.toFloat()
    }

}