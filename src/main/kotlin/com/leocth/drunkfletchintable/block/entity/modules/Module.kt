package com.leocth.drunkfletchintable.block.entity.modules

import com.leocth.drunkfletchintable.block.entity.FletchinTableBlockEntity
import com.leocth.drunkfletchintable.util.NbtSerializable
import net.minecraft.nbt.NbtCompound

abstract class Module(val blockEntity: FletchinTableBlockEntity): NbtSerializable {
    open fun serverTick() {}

    override fun readNbt(nbt: NbtCompound) {}
    override fun writeNbt(nbt: NbtCompound) {}
    open fun readClientNbt(nbt: NbtCompound) {}
    open fun writeClientNbt(nbt: NbtCompound) {}
}
