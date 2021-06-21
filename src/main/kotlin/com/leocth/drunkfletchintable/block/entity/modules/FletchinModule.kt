package com.leocth.drunkfletchintable.block.entity.modules

import com.leocth.drunkfletchintable.block.entity.FletchinTableBlockEntity
import com.leocth.drunkfletchintable.client.screen.FletchinScreen
import com.leocth.drunkfletchintable.util.NbtSerializable
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.TexturedButtonWidget
import net.minecraft.nbt.NbtCompound
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.Identifier

abstract class FletchinModule(val blockEntity: FletchinTableBlockEntity): NbtSerializable, NamedScreenHandlerFactory {
    open fun serverTick() {}

    abstract val type: ModuleType<*>

    override fun readNbt(nbt: NbtCompound) {}
    override fun writeNbt(nbt: NbtCompound) {}
    open fun readClientNbt(nbt: NbtCompound) {}
    open fun writeClientNbt(nbt: NbtCompound) {}

    val screenHandlerContext: ScreenHandlerContext
        get() = ScreenHandlerContext.create(blockEntity.world, blockEntity.pos)

    @Environment(EnvType.CLIENT)
    abstract fun createButton(x: Int, y: Int): Button

    @Environment(EnvType.CLIENT)
    open inner class Button(
        x: Int, y: Int,
        u: Int, v: Int,
        w: Int = WIDTH,
        h: Int = HEIGHT,
        texture: Identifier = FletchinScreen.TEXTURE
    ): TexturedButtonWidget(
        x, y, w, h, u, v, texture, {
            val player = MinecraftClient.getInstance().player
            player?.openHandledScreen(this)
        }
    )
    companion object {
        const val WIDTH = 28
        const val HEIGHT = 14
    }
}
