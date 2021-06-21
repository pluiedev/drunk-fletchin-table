package com.leocth.drunkfletchintable.client.screen

import com.leocth.drunkfletchintable.DrunkFletchinTable
import com.leocth.drunkfletchintable.block.entity.FletchinTableBlockEntity
import com.leocth.drunkfletchintable.block.entity.modules.FletchinModule
import com.leocth.drunkfletchintable.screen.CraftingScreenHandler
import com.leocth.drunkfletchintable.screen.FletchinScreenHandler
import com.leocth.drunkfletchintable.screen.TippingScreenHandler
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.widget.TexturedButtonWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.potion.Potions
import net.minecraft.text.Text
import net.minecraft.util.Identifier

class CraftingScreen(
    handler: CraftingScreenHandler,
    inventory: PlayerInventory,
    title: Text
) : FletchinScreen<CraftingScreenHandler>(handler, inventory, title) {

    companion object {
        val TEXTURE = DrunkFletchinTable.id("textures/gui/fletchin_table/crafting.png")
    }

    override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
        super.drawBackground(matrices, delta, mouseX, mouseY)
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.setShaderTexture(0, TEXTURE)
        drawTexture(matrices, x+45, y+17, 0, 0, 160, 61)
    }

}