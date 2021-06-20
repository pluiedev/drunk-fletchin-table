package com.leocth.drunkfletchintable.block.entity.modules

import com.leocth.drunkfletchintable.block.entity.FletchinTableBlockEntity
import com.leocth.drunkfletchintable.util.NbtSerializable
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.nbt.NbtCompound
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler

abstract class FletchinModule(val blockEntity: FletchinTableBlockEntity): NbtSerializable, NamedScreenHandlerFactory {
    open fun serverTick() {}

    abstract val type: ModuleType<*>

    override fun readNbt(nbt: NbtCompound) {}
    override fun writeNbt(nbt: NbtCompound) {}
    open fun readClientNbt(nbt: NbtCompound) {}
    open fun writeClientNbt(nbt: NbtCompound) {}
}
