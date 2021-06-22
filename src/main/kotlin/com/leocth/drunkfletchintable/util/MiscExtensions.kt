package com.leocth.drunkfletchintable.util

import net.minecraft.item.ItemStack

val ItemStack.isFull: Boolean get() = count >= maxCount