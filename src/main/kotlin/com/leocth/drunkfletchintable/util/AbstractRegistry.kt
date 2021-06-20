package com.leocth.drunkfletchintable.util

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap

/**
 * A registry with an idiomatic, type-safe and Kotlin-friendly access interface.
 */
abstract class AbstractRegistry<K, V>(protected val map: BiMap<K, V> = HashBiMap.create()): MutableMap<K, V> by map {

    override operator fun get(key: K): V = find(key) ?: throw IllegalArgumentException("Missing mapping for key $key!")
    fun find(k: K): V? = map[k]

    fun getKey(v: V): K = findKey(v) ?: throw IllegalArgumentException("Missing mapping for type $v!")
    fun findKey(v: V): K? = map.inverse()[v]
}