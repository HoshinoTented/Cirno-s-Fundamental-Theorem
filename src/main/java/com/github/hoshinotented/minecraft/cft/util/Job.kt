package com.github.hoshinotented.minecraft.cft.util

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.Event


interface Job<E : Event> {
  val isCancelled: Boolean
  
  fun onTick(event : E)
  fun cancel()
  
  /**
   * Never call this function directly
   */
  fun initialize() {
    MinecraftForge.EVENT_BUS.register(this)
  }
  
  /**
   * Never call this function directly
   */
  fun dispose() {
    MinecraftForge.EVENT_BUS.unregister(this)
  }
}