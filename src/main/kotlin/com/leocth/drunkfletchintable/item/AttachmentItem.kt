package com.leocth.drunkfletchintable.item

import com.leocth.drunkfletchintable.block.entity.modules.FletchinModule
import com.leocth.drunkfletchintable.block.entity.modules.ModuleProvider
import net.minecraft.item.Item

class AttachmentItem<M: FletchinModule>(
    settings: Settings,
    provider: ModuleProvider<M>
) : Item(settings), ModuleProvider<M> by provider