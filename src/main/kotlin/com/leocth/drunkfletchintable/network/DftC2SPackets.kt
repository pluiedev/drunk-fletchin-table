package com.leocth.drunkfletchintable.network

import com.leocth.drunkfletchintable.DrunkFletchinTable.id
import com.leocth.drunkfletchintable.screen.TippingScreenHandler
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object DftC2SPackets {
    val DRAIN = id("drain")

    fun registerListeners() {
        ServerPlayNetworking.registerGlobalReceiver(DRAIN) { server, player, handler, buf, sender ->
            server.execute {
                val screenHandler = player.currentScreenHandler
                if (screenHandler is TippingScreenHandler) {
                    screenHandler.drain()
                }
            }
        }
    }

    fun sendDrainPacket() {
        ClientPlayNetworking.send(DRAIN, PacketByteBufs.empty())
    }
}