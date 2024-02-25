package com.github.hoshinotented.minecraft.cft.event

import com.github.hoshinotented.minecraft.cft.FundamentalTheorem
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.event.server.ServerStartedEvent

@Suppress("unused")
@Mod.EventBusSubscriber(modid = FundamentalTheorem.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object CommonForgeEventHandler {
  @SubscribeEvent
  fun onServerStarted(event: ServerStartedEvent) {
    FundamentalTheorem.server = event.server!!
  }
}