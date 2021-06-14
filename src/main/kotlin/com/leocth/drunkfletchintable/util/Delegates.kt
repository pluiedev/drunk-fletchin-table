package com.leocth.drunkfletchintable.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class OverwritableProperty<V>(initialValue: V) : ReadWriteProperty<Any?, V> {
    private var value = initialValue

    protected abstract fun onChange(property: KProperty<*>, oldValue: V, newValue: V): V

    override fun getValue(thisRef: Any?, property: KProperty<*>): V = value

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: V) {
        this.value = onChange(property, this.value, value)
    }
}

inline fun <V> overwritable(initialValue: V, crossinline onChange: (property: KProperty<*>, oldValue: V, newValue: V) -> V):
        ReadWriteProperty<Any?, V> =
    object : OverwritableProperty<V>(initialValue) {
        override fun onChange(property: KProperty<*>, oldValue: V, newValue: V): V = onChange(property, oldValue, newValue)
    }