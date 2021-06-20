package com.leocth.drunkfletchintable

import com.leocth.drunkfletchintable.block.entity.FletchinTableBlockEntity
import com.leocth.drunkfletchintable.block.entity.modules.ModuleRegistry
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager

object DrunkFletchinTable: ModInitializer {
    const val MODID = "drunkfletchintable"
    val LOGGER = LogManager.getLogger()


    override fun onInitialize() {
        ModuleRegistry.registerBuiltins()

        DftBlocks.register()
        DftItems.register()

        Registry.register(Registry.BLOCK_ENTITY_TYPE, id("fletchin_table"), FletchinTableBlockEntity.TYPE)
    }

    @Suppress("NOTHING_TO_INLINE")
    inline fun id(path: String) = Identifier(MODID, path)
}