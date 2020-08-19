package com.leocth.drunkfletchintable

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.util.Identifier

val PACKET_C2S_FT_MODE = Identifier(MODID, "c2s/ft_mode")

fun registerC2SPackets() {
    ServerSidePacketRegistry.INSTANCE.register(PACKET_C2S_FT_MODE)
    { context, buf ->
        val pos = buf.readVarInt()
        context.taskQueue.execute {
            if (context.player.currentScreenHandler is FletchinTableScreenHandler) {
                (context.player.currentScreenHandler as FletchinTableScreenHandler).setDelegatedMode(FletchinTableMode.values()[pos])
            }
        }
    }
}

fun registerS2CPackets() {
}