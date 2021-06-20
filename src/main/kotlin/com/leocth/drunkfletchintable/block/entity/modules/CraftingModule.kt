package com.leocth.drunkfletchintable.block.entity.modules

import com.leocth.drunkfletchintable.block.entity.FletchinTableBlockEntity

class CraftingModule(blockEntity: FletchinTableBlockEntity) : FletchinModule(blockEntity) {
    override val type: ModuleType<*> = TYPE

    companion object {
        val TYPE = ModuleType(::CraftingModule)
    }
}
