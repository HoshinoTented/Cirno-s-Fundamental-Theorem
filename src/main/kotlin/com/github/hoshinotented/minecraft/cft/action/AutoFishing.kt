package com.github.hoshinotented.minecraft.cft.action

import com.github.hoshinotented.minecraft.cft.event.HookBiteEvent
import com.github.hoshinotented.minecraft.cft.event.KeyBindings
import com.github.hoshinotented.minecraft.cft.util.AbstractJob
import com.github.hoshinotented.minecraft.cft.util.Job
import com.github.hoshinotented.minecraft.cft.util.firstHandedItem
import com.github.hoshinotented.minecraft.cft.util.lastHandedItem
import net.minecraft.client.Minecraft
import net.minecraft.world.item.FishingRodItem
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.TickEvent

private class AutoFishingJob : AbstractJob<HookBiteEvent>("auto.fishing") {
  override var isCancelled: Boolean = false
  
  init {
    initialize()
  }
  
  @SubscribeEvent
  override fun onTick(event: HookBiteEvent) {
    val hook = event.hook
    val player = hook.playerOwner ?: return
    val gameMode = Minecraft.getInstance()?.gameMode ?: return
    
    val preferMainHand = player.firstHandedItem { it.item is FishingRodItem } ?: return
    val preferCoMainHand = player.lastHandedItem { it.item is FishingRodItem }!!
    
    gameMode.useItem(player, preferMainHand)    // end fish
    gameMode.useItem(player, preferCoMainHand)    // start fish
  }
}

object AutoFishing : JobContainer<HookBiteEvent> {
  override var job: Job<HookBiteEvent>? = null
  
  private fun onToggle(e: TickEvent.ClientTickEvent) {
    val oldJob = job
    
    cancel()
    
    if (oldJob == null) {
      job = AutoFishingJob()
    }
  }
  
  fun initialize() {
    KeyBindings.addListener(KeyBindings.keyAutoFishing, AutoFishing::onToggle)
  }
  
  override fun cancel() {
    val job = job
    if (job != null && ! job.isCancelled) {
      job.cancel()
    }
    
    this.job = null
  }
}