package com.leocth.drunkfletchintable.block.entity.modules

import com.leocth.drunkfletchintable.block.entity.FletchinTableBlockEntity
import com.leocth.drunkfletchintable.screen.CraftingScreenHandler
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText

class CraftingModule(blockEntity: FletchinTableBlockEntity) : FletchinModule(blockEntity) {
    override val type: ModuleType<*> = TYPE

    @Environment(EnvType.CLIENT)
    override fun createButton(x: Int, y: Int): Button
        = Button(x, y, 0, 182)


    override fun createMenu(syncId: Int, inv: PlayerInventory, player: PlayerEntity): ScreenHandler
        = CraftingScreenHandler(syncId, inv, screenHandlerContext)

    override fun getDisplayName(): Text = TranslatableText("screen.drunkfletchintable.crafting")

    companion object {
        val TYPE = ModuleType(::CraftingModule)
    }
}
