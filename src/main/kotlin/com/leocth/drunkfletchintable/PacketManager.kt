package com.leocth.drunkfletchintable

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.util.Identifier

val PACKET_C2S_FT_MODE = Identifier(MODID, "c2s/ft_mode")
val PACKET_C2S_FT_DRAIN = Identifier(MODID, "c2s/ft_drain")

fun registerC2SPackets() {
    ServerSidePacketRegistry.INSTANCE.register(PACKET_C2S_FT_MODE)
    { context, buf ->
        val syncId = buf.readVarInt()
        val mode = buf.readVarInt()
        context.taskQueue.execute {
            val screenHandler = context.player.currentScreenHandler
            if (screenHandler is FletchinTableScreenHandler && screenHandler.syncId == syncId) {
                screenHandler.setDelegatedMode(FletchinTableMode.values()[mode])
            }
        }
    }
    ServerSidePacketRegistry.INSTANCE.register(PACKET_C2S_FT_DRAIN)
    { context, buf ->
        val syncId = buf.readVarInt()
        context.taskQueue.execute {
            val screenHandler = context.player.currentScreenHandler
            if (screenHandler is FletchinTableScreenHandler && screenHandler.syncId == syncId) {
                screenHandler.drainPotion()
            }
        }
    }
}

fun registerS2CPackets() {}