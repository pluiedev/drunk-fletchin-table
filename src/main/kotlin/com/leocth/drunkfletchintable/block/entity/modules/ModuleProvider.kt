package com.leocth.drunkfletchintable.block.entity.modules

import com.leocth.drunkfletchintable.block.entity.FletchinTableBlockEntity

fun interface ModuleProvider<M: FletchinModule> {
    fun getModule(blockEntity: FletchinTableBlockEntity): M
}