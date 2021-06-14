package com.leocth.drunkfletchintable.old.widgets

import com.leocth.drunkfletchintable.FletchinTableBlockEntity.Companion.TIPPING_MAX_USES
import com.leocth.drunkfletchintable.FletchinTableScreenHandler
import com.leocth.drunkfletchintable.old.MODID
import com.leocth.drunkfletchintable.old.PACKET_C2S_FT_DRAIN
import com.leocth.drunkfletchintable.old.isShiftPressed
import com.mojang.blaze3d.systems.RenderSystem
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.effect.StatusEffectUtil
import net.minecraft.network.PacketByteBuf
import net.minecraft.potion.PotionUtil
import net.minecraft.potion.Potions
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import kotlin.math.abs

class TippingScreenWidget(
    private val client: MinecraftClient?,
    private val x: Int,
    private val y: Int,
    private val handler: FletchinTableScreenHandler
): FletchinScreenWidget() {

    companion object {
        val TEXTURE = Identifier(MODID, "textures/gui/fletchin_table/tipping.png")
    }

    override fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean? {
        val potion = handler.getPotion()
        if (potion != Potions.EMPTY) {
            val progress = handler.getDisplayProgress()
            val offset = (34 * progress / TIPPING_MAX_USES)
            val mx = mouseX.toInt()
            val my = mouseY.toInt()
            if (mx in x+90..x+145 && my in y+51-offset..y+51 && isShiftPressed()) {
                drainPotion()
                return true
            }
        }
        return null
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
            val offset = (34 * progress / TIPPING_MAX_USES)
            val r = (color shr 16 and 255) / 255f
            val g = (color shr 8  and 255) / 255f
            val b = (color        and 255) / 255f
            RenderSystem.color4f(r, g, b, 1.0f)
            drawTexture(matrices, x+90, y+51-offset, 45, 95-offset, 55, offset)
            if (prog > 0f) {
                drawTexture(matrices, x+167, y+42, 122, 86, 27, 36)
            }
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
            if (mouseX in x+90..x+145 && mouseY in y+51-offset..y+51) {
                val tooltip = potion.effects.map { effect ->
                    var text = TranslatableText(effect.translationKey)
                    if (effect.amplifier > 0) {
                        text = TranslatableText("potion.withAmplifier", text, TranslatableText("potion.potency.${effect.amplifier}"))
                    }
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
                    text
                }.toMutableList()
                tooltip.add(TranslatableText("tooltip.drunkfletchintable.tipping.drain"))
                client?.currentScreen?.renderTooltip(
                    matrices,
                    tooltip as List<Text>,
                    mouseX,
                    mouseY
                )
            }
        }
    }

    private fun drainPotion() {
        val buf = PacketByteBuf(Unpooled.buffer())
        buf.writeVarInt(handler.syncId)
        ClientSidePacketRegistry.INSTANCE.sendToServer(PACKET_C2S_FT_DRAIN, buf)
    }
}