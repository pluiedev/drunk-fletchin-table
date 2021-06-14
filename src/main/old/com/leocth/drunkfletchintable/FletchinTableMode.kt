package com.leocth.drunkfletchintable.old

import net.minecraft.text.Text
import net.minecraft.text.TranslatableText

enum class FletchinTableMode(
    val displayText: Text
) {
    NONE(id("none")),
    CRAFTING(id("crafting")),
    TIPPING(id("tipping"));
}

/// some kotlin oddity. idk
private fun id(str: String) = TranslatableText("container.drunkfletchintable.fletchin_table.$str")