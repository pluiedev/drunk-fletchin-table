package com.leocth.drunkfletchintable.block.entity.modules

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.leocth.drunkfletchintable.DrunkFletchinTable.id
import com.leocth.drunkfletchintable.block.entity.FletchinTableBlockEntity
import com.leocth.drunkfletchintable.util.AbstractRegistry
import net.minecraft.util.Identifier

object ModuleRegistry: AbstractRegistry<Identifier, ModuleType<*>>() {
    fun makeModule(blockEntity: FletchinTableBlockEntity, id: Identifier): FletchinModule {
        val factory = map[id] ?: throw IllegalStateException("module factory not found: $id!")
        return factory.factory(blockEntity)
    }

    fun registerBuiltins() {
        put(id("crafting"), CraftingModule.TYPE)
        put(id("tipping"), TippingModule.TYPE)
    }
}

class ModuleType<T: FletchinModule>(
    val factory: (FletchinTableBlockEntity) -> T
)