package com.leocth.drunkfletchintable.old.widgets

import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.DrawableHelper

abstract class FletchinScreenWidget: DrawableHelper(), Drawable {
    abstract fun onMouseClick(mouseX: Double, mouseY: Double, button: Int): Boolean?
}