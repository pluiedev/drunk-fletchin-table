package com.leocth.drunkfletchintable.client.screen

import com.leocth.drunkfletchintable.DrunkFletchinTable
import com.leocth.drunkfletchintable.block.entity.modules.USES_PER_POTION_ITEM
import com.leocth.drunkfletchintable.screen.TippingScreenHandler
import com.leocth.drunkfletchintable.util.Argb
import com.leocth.drunkfletchintable.util.toArgb
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionUtil
import net.minecraft.potion.Potions
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.math.MathHelper

class TippingScreen(
    handler: TippingScreenHandler,
    inventory: PlayerInventory,
    title: Text
) : FletchinScreen<TippingScreenHandler>(handler, inventory, title) {

    private var data = CachedPotionData(handler.potion, handler.potionAmount)

    override fun tick() {
        super.tick()
        data.tryToUpdate(handler.potion, handler.potionAmount)
    }

    private fun hoveringOverPotion(mouseX: Int, mouseY: Int): Boolean {
        val relativeX = mouseX - x
        val relativeY = mouseY - y
        val yOffset = 51 - height

        return relativeX in 90..145 && relativeY in yOffset..51
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        val potion = data.potion

        if (potion != Potions.EMPTY) {
            if (hoveringOverPotion(mouseX.toInt(), mouseY.toInt()) && Screen.hasShiftDown()) {
                // TODO: implement custom packet when it is drained
                return true
            }
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
        super.drawBackground(matrices, delta, mouseX, mouseY)
        drawModuleBg(matrices, TEXTURE)

        val potion = data.potion
        if (potion != Potions.EMPTY)
            drawPotionOverlay(matrices, delta, mouseX, mouseY, potion)
    }

    private fun drawPotionOverlay(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int, potion: Potion) {
        // TODO: a lerp would be nice
        val height = data.height
        val hRounded = height.toInt()
        val (_, r, g, b) = data.cachedPotionColor

        // set color
        setShaderColor(r, g, b, 1f) {
            // liquid in bottle
            drawTexture(matrices, x+90, y+51-hRounded, 45, 95-hRounded, 55, hRounded)
            // currently pouring; draw stream
            if (handler.pouring)
                drawTexture(matrices, x+167, y+42, 122, 86, 27, 36)
        }

        if (hoveringOverPotion(mouseX, mouseY)) {
            client?.currentScreen?.renderTooltip(matrices, data.tooltip, mouseX, mouseY)
        }
    }


    companion object {
        private val TEXTURE = DrunkFletchinTable.id("textures/gui/fletchin_table/tipping.png")


        private inline fun setShaderColor(r: Float, g: Float, b: Float, a: Float, block: () -> Unit) {
            RenderSystem.setShaderColor(r, g, b, a)
            block()
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f) // reset color
        }
    }
}


internal class CachedPotionData(potion: Potion, amount: Int) {
    var potion: Potion = potion
        private set
    var amount: Int = amount
        private set
    var cachedPotionColor: Argb = PotionUtil.getColor(potion).toArgb()
        private set
    var tooltip: List<Text> = makeTooltip(potion)
        private set
    var height: Float = getHeightFromAmount(amount)
        private set

    fun tryToUpdate(potion: Potion, amount: Int) {
        if (this.potion != potion) {
            this.potion = potion

            cachedPotionColor = PotionUtil.getColor(potion).toArgb()
            tooltip = makeTooltip(potion)
        }
        if (this.amount != amount) {
            this.amount = amount

            height = getHeightFromAmount(amount)
        }
    }

    companion object {
        private const val MAX_HEIGHT = 34

        private fun getHeightFromAmount(amount: Int): Float
            = MAX_HEIGHT * amount / USES_PER_POTION_ITEM.toFloat()

        private fun makeTooltip(potion: Potion): List<Text> {
            val list = mutableListOf<Text>()

            for (effect in potion.effects) {
                var text = TranslatableText(effect.translationKey)
                if (effect.amplifier > 0) {
                    text = TranslatableText("potion.withAmplifier", text, TranslatableText("potion.potency.${effect.amplifier}"))
                }
                /* TODO: use HSV to ramp up the saturation
                text.styled {
                    // i need polish notation
                    val nr = abs(((r - 0.25f) * 1.7f) * 255).toInt()
                    val ng = abs(((g - 0.25f) * 1.7f) * 255).toInt()
                    val nb = abs(((b - 0.25f) * 1.7f) * 255).toInt()
                    val nrgb = (nr shl 16) or (ng shl 8) or nb
                    it.withColor(TextColor.fromRgb(nrgb)).withBold(effect.amplifier > 0)
                }
                if (effect.duration > 1) {
                    text = TranslatableText("potion.withDuration", text, StatusEffectUtil.durationToString(effect, 1.0f))
                }
                 */
                list.add(text)
            }
            list.add(TranslatableText("tooltip.drunkfletchintable.tipping.drain"))
            return list
        }
    }
}