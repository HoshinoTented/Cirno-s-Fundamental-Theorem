package com.github.hoshinotented.minecraft.cft.event

import com.github.hoshinotented.minecraft.cft.FundamentalTheorem
import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
import net.neoforged.neoforge.client.settings.KeyConflictContext
import net.neoforged.neoforge.event.TickEvent
import org.lwjgl.glfw.GLFW

@Suppress("unused")
@Mod.EventBusSubscriber(modid = FundamentalTheorem.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object KeyBindings {
  val listeners: MutableMap<KeyMapping, MutableSet<(TickEvent.ClientTickEvent) -> Unit>> = mutableMapOf()
  
  const val CATEGORIES: String = "key.categories.${FundamentalTheorem.MODID}"
  
  fun of(id: String): String {
    return "key.${FundamentalTheorem.MODID}.$id"
  }
  
  val keyQuickMove = KeyMapping(
    of("invoke.proof"),
    KeyConflictContext.IN_GAME,
    InputConstants.Type.KEYSYM,
    GLFW.GLFW_KEY_C,
    CATEGORIES
  )
  
  val keyKeepUsing = KeyMapping(
    of("keep.using"),
    KeyConflictContext.IN_GAME,
    InputConstants.Type.KEYSYM,
    GLFW.GLFW_KEY_P,
    CATEGORIES
  )
  
  val keyAutoFishing = KeyMapping(
    of("auto.fishing"),
    KeyConflictContext.IN_GAME,
    InputConstants.Type.KEYSYM,
    GLFW.GLFW_KEY_F,
    CATEGORIES
  )
  
  val keyMappings = listOf(keyQuickMove, keyKeepUsing, keyAutoFishing)
  
  @SubscribeEvent
  fun registerKeyBinding(event: RegisterKeyMappingsEvent) {
    keyMappings.map { event.register(it) }
  }
  
  fun addListener(keyMapping: KeyMapping, listener: (TickEvent.ClientTickEvent) -> Unit): Boolean {
    return listeners.getOrPut(keyMapping) { mutableSetOf() }
      .add(listener)
  }
}

@Suppress("unused")
@Mod.EventBusSubscriber(modid = FundamentalTheorem.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = [Dist.CLIENT])
object KeyConsumer {
  @SubscribeEvent
  fun onClientTick(event: TickEvent.ClientTickEvent) {
    if (event.phase === TickEvent.Phase.END) {
      KeyBindings.listeners.forEach { (keyMapping, listener) ->
        while (keyMapping.consumeClick()) {
          listener.forEach { it(event) }
        }
      }
    }
  }
}