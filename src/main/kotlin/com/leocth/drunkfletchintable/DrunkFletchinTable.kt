package com.leocth.drunkfletchintable

import com.leocth.drunkfletchintable.block.TinyTaterrBlokk
import com.leocth.drunkfletchintable.block.entity.FletchinTableBlockEntity
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.BlockItem
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object DrunkFletchinTable: ModInitializer {
    const val MODID = "drunkfletchintable"

    val TINY_TATER = TinyTaterrBlokk()

    override fun onInitialize() {
        Registry.register(Registry.BLOCK, id("tatertater"), TINY_TATER)
        Registry.register(Registry.ITEM, id("tatertater"), BlockItem(TINY_TATER, FabricItemSettings()))
        Registry.register(Registry.BLOCK_ENTITY_TYPE, id("fletchin_table"), FletchinTableBlockEntity.TYPE)
    }

    private fun id(path: String) = Identifier(MODID, path)
}