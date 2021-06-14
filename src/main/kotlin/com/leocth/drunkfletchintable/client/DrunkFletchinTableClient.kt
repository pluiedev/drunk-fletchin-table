package com.leocth.drunkfletchintable.client

import com.leocth.drunkfletchintable.DrunkFletchinTable
import com.leocth.drunkfletchintable.block.TinyTaterrBlokk
import com.leocth.drunkfletchintable.block.entity.FletchinTableBlockEntity
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.block.Blocks
import net.minecraft.client.render.RenderLayer
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object DrunkFletchinTableClient: ClientModInitializer {
    override fun onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), DrunkFletchinTable.TINY_TATER)
    }
}