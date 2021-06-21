package com.leocth.drunkfletchintable.client

import com.leocth.drunkfletchintable.block.DftBlocks
import com.leocth.drunkfletchintable.client.screen.CraftingScreen
import com.leocth.drunkfletchintable.screen.CraftingScreenHandler
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry
import net.minecraft.client.render.RenderLayer

object DrunkFletchinTableClient: ClientModInitializer {
    override fun onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), DftBlocks.TINY_TATER)

        ScreenRegistry.register(CraftingScreenHandler.TYPE, ::CraftingScreen)
    }
}