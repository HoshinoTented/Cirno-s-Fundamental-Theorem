package com.github.hoshinotented.minecraft.cft.util

import net.neoforged.bus.api.Event
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.common.NeoForge

interface Job<E : Event> {
  val isCancelled: Boolean
  
  fun onTick(event : E)
  fun cancel()
  
  /**
   * Never call this function directly
   */
  fun initialize() {
    NeoForge.EVENT_BUS.register(this)
  }
  
  /**
   * Never call this function directly
   */
  fun dispose() {
    NeoForge.EVENT_BUS.unregister(this)
  }
}