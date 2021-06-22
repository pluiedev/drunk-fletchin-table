package com.leocth.drunkfletchintable.item

import com.leocth.drunkfletchintable.DrunkFletchinTable.id
import net.fabricmc.fabric.api.tag.TagRegistry
import net.minecraft.util.Identifier

object DftItemTags {
    val POTIONS = TagRegistry.item(c("potions"))

    @Suppress("NOTHING_TO_INLINE")
    private inline fun c(path: String) = Identifier("c", path)

    fun register() { /* NO-OP */ }
}