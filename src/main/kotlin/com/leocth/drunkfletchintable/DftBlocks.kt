package com.leocth.drunkfletchintable

import com.leocth.drunkfletchintable.block.TinyTaterrBlokk
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

object DftBlocks {
    val TINY_TATER = TinyTaterrBlokk()

    fun register() {
        registerBlocks(
            "tatertater" to TINY_TATER
        )
    }

    private fun registerBlocks(vararg pairs: Pair<String, Block>) {
        for ((id, item) in pairs) {
            Registry.register(Registry.BLOCK, DrunkFletchinTable.id(id), item)
        }
    }
}