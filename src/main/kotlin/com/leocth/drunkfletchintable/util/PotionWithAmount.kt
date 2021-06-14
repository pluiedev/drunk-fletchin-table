package com.leocth.drunkfletchintable.util

import net.minecraft.nbt.NbtCompound
import net.minecraft.potion.Potion
import net.minecraft.potion.Potions
import net.minecraft.util.registry.Registry

class PotionWithAmount(type: Potion, amount: Int): NbtSerializable {
    private var _type: Potion = type
    private var _amount: Int = amount

    var type: Potion
        get() = _type
        set(value) {
            _type = if (value != Potions.EMPTY) value else {
                _amount = 0
                Potions.EMPTY
            }
        }
    var amount: Int
        get() = _amount
        set(value) {
            _amount = if (value >= 0) value else {
                _type = Potions.EMPTY
                0
            }
        }

    val isEmpty get() = type == Potions.EMPTY && amount >= 0

    override fun readNbt(nbt: NbtCompound) {
        type = Registry.POTION.get(nbt.getIdentifier("type"))
        amount = nbt.getInt("amount")
    }

    override fun writeNbt(nbt: NbtCompound) {
        nbt.putIdentifier("type", Registry.POTION.getId(type))
        nbt.putInt("amount", amount)
    }
}