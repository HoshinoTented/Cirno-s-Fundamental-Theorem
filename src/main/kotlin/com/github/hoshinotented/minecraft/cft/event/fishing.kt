package com.github.hoshinotented.minecraft.cft.event

import com.github.hoshinotented.minecraft.cft.FundamentalTheorem
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.projectile.FishingHook
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.TickEvent
import net.neoforged.neoforge.event.entity.EntityEvent
import java.lang.reflect.Field

data class HookBiteEvent(val hook: FishingHook) : EntityEvent(hook)

@Mod.EventBusSubscriber(value = [Dist.CLIENT], modid = FundamentalTheorem.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object HookBiteBroadcast {
  private var lastBiting: Boolean = false
  
  @SubscribeEvent
  @Suppress("unused")
  fun onClientTick(event: TickEvent.ClientTickEvent) {
    val mc = Minecraft.getInstance() ?: return
    val player = mc.player ?: return
    val hook = player.fishing ?: return
    val biting = hook.biting
    
    if (!lastBiting && biting) {
      NeoForge.EVENT_BUS.post(HookBiteEvent(hook))
    }
    
    lastBiting = biting
  }
}