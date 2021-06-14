package com.leocth.drunkfletchintable.old

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

@Environment(EnvType.CLIENT)
fun isShiftPressed(): Boolean {
    val handle = MinecraftClient.getInstance().window.handle
    return InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_SHIFT) || InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_RIGHT_SHIFT)
}