package com.leocth.drunkfletchintable

import com.leocth.drunkfletchintable.block.entity.modules.CraftingModule
import com.leocth.drunkfletchintable.block.entity.modules.TippingModule
import com.leocth.drunkfletchintable.item.AttachmentItem
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

object DftItems {
    val CRAFTING_ATTACHMENT = AttachmentItem(defaultSettings, ::CraftingModule)
    val TIPPING_ATTACHMENT = AttachmentItem(defaultSettings, ::TippingModule)

    val TINY_TATER = BlockItem(DftBlocks.TINY_TATER, defaultSettings)

    fun register() {
        registerItems(
            "crafting_attachment" to CRAFTING_ATTACHMENT,
            "tipping_attachment" to TIPPING_ATTACHMENT,
            "tatertater" to TINY_TATER
        )
    }

    private fun registerItems(vararg pairs: Pair<String, Item>) {
        for ((id, item) in pairs) {
            Registry.register(Registry.ITEM, DrunkFletchinTable.id(id), item)
        }
    }

    private val defaultSettings get() = FabricItemSettings()
}