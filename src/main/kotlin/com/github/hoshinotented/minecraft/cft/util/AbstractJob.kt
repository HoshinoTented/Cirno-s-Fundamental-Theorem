package com.github.hoshinotented.minecraft.cft.util

import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.neoforged.bus.api.Event

abstract class AbstractJob<E : Event>(val id: String) : Job<E> {
  override var isCancelled = false
    protected set
  
  override fun initialize() {
    super.initialize()
    
    Minecraft.getInstance().player?.sendSystemMessage(Component.translatable(ofMessage("$id.initialize")))
  }
  
  override fun dispose() {
    super.dispose()
    
    Minecraft.getInstance().player?.sendSystemMessage(Component.translatable(ofMessage("$id.dispose")))
  }
  
  override fun cancel() {
    this.isCancelled = true
    dispose()
  }
}