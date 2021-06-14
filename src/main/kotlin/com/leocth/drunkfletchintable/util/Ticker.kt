package com.leocth.drunkfletchintable.util

import net.minecraft.nbt.NbtCompound

class Ticker(
    private var duration: Int,
    val completedCallback: () -> Unit
): NbtSerializable {
    var current: Int = 0
        private set

    fun tick() {
        if (current >= duration) {
            completedCallback()
            reset()
            return
        } else {
            current++
        }
    }

    fun reset() {
        current = 0
    }

    override fun readNbt(nbt: NbtCompound) {
        current = nbt.getInt("cur")
        duration = nbt.getInt("dur")
    }

    override fun writeNbt(nbt: NbtCompound) {
        nbt.putInt("cur", current)
        nbt.putInt("dur", duration)
    }
}