package com.github.hoshinotented.minecraft.cft.network

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handlers.ServerPayloadHandler
import net.neoforged.neoforge.network.handling.IPlayPayloadHandler
import net.neoforged.neoforge.network.handling.PlayPayloadContext
import net.neoforged.neoforge.network.registration.IDirectionAwarePayloadHandlerBuilder
import net.neoforged.neoforge.network.registration.IPayloadRegistrar

interface IPayloadHandler<T: CustomPacketPayload> {
  val id: ResourceLocation
  fun encoder(payload: T, buf: FriendlyByteBuf)
  fun decoder(buf: FriendlyByteBuf): T
  
  fun handleClient(payload: T, context: PlayPayloadContext) {}
  fun handleServer(payload: T, context: PlayPayloadContext) {}
  
  fun register(registry: IPayloadRegistrar) {
    registry.play(id, ::decoder) { it : IDirectionAwarePayloadHandlerBuilder<T, IPlayPayloadHandler<T>> ->
      it.server(::handleServer)
        .client(::handleClient)
    }
  }
}