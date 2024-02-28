package com.github.hoshinotented.minecraft.cft.event

import com.github.hoshinotented.minecraft.cft.FundamentalTheorem
import com.github.hoshinotented.minecraft.cft.network.Packet
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent

@Suppress("unused")
@Mod.EventBusSubscriber(modid = FundamentalTheorem.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
object NetworkEventHandler {
  @SubscribeEvent
  fun onRegisterPayload(event: RegisterPayloadHandlerEvent) {
    val registry = event.registrar(FundamentalTheorem.MODID)
    Packet.register(registry)
  }
}