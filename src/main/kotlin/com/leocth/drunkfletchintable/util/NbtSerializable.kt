package com.leocth.drunkfletchintable.util

import net.minecraft.nbt.NbtCompound

interface NbtSerializable {
    fun readNbt(nbt: NbtCompound)

    fun writeNbt(nbt: NbtCompound)
}