package com.github.hoshinotented.minecraft.cft.event

import com.github.hoshinotented.minecraft.cft.FundamentalTheorem
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.projectile.FishingHook
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.EntityEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import java.lang.reflect.Field

data class HookBiteEvent(val hook: FishingHook) : EntityEvent(hook)

@Mod.EventBusSubscriber(value = [ Dist.CLIENT ], modid = FundamentalTheorem.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object HookBiteBroadcast {
  private var lastBiting: Boolean = false
  
  @SubscribeEvent
  @Suppress("unused")
  fun onClientTick(event: TickEvent.ClientTickEvent) {
    val mc = Minecraft.getInstance() ?: return
    val player = mc.player ?: return
    val hook = player.fishing ?: return
    val biting = hook.biting
    
    if (! lastBiting && biting) {
      MinecraftForge.EVENT_BUS.post(HookBiteEvent(hook))
    }
    
    lastBiting = biting
  }
}