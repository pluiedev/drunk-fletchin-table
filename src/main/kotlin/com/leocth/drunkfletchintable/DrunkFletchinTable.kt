package com.leocth.drunkfletchintable

import com.leocth.drunkfletchintable.block.DftBlocks
import com.leocth.drunkfletchintable.block.entity.FletchinTableBlockEntity
import com.leocth.drunkfletchintable.block.entity.modules.ModuleRegistry
import com.leocth.drunkfletchintable.item.DftItemTags
import com.leocth.drunkfletchintable.item.DftItems
import com.leocth.drunkfletchintable.network.DftC2SPackets
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager

object DrunkFletchinTable: ModInitializer {
    const val MODID = "drunkfletchintable"
    val LOGGER = LogManager.getLogger()


    override fun onInitialize() {
        ModuleRegistry.registerBuiltins()

        DftItemTags.register()

        DftBlocks.register()
        DftItems.register()

        DftC2SPackets.registerListeners()

        Registry.register(Registry.BLOCK_ENTITY_TYPE, id("fletchin_table"), FletchinTableBlockEntity.TYPE)
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun id(path: String) = Identifier(MODID, path)
}