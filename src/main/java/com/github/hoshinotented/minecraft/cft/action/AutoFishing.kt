package com.github.hoshinotented.minecraft.cft.action

import com.github.hoshinotented.minecraft.cft.FundamentalTheorem
import com.github.hoshinotented.minecraft.cft.event.HookBiteEvent
import com.github.hoshinotented.minecraft.cft.event.KeyBindings
import com.github.hoshinotented.minecraft.cft.util.AbstractJob
import com.github.hoshinotented.minecraft.cft.util.Job
import com.github.hoshinotented.minecraft.cft.util.firstHandedItem
import com.github.hoshinotented.minecraft.cft.util.lastHandedItem
import net.minecraft.client.Minecraft
import net.minecraft.world.item.FishingRodItem
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

private class AutoFishingJob : AbstractJob<HookBiteEvent>("auto.fishing") {
  override var isCancelled: Boolean = false
  
  init {
    initialize()
  }
  
  @SubscribeEvent
  override fun onTick(event: HookBiteEvent) {
    val hook = event.hook
    val player = hook.playerOwner
    if (player == null) {
      FundamentalTheorem.LOGGER.warn("hook.playerOwner == null")
      return
    }
    
    val gameMode = Minecraft.getInstance().gameMode
    if (gameMode == null) {
      FundamentalTheorem.LOGGER.warn("gameMode == null")
      return
    }
    
    val preferMainHand = player.firstHandedItem { it.item is FishingRodItem }
    if (preferMainHand == null) {
      FundamentalTheorem.LOGGER.warn("Player doesn't hand a fishing rod")
      return
    }
    
    val preferCoMainHand = player.lastHandedItem { it.item is FishingRodItem }!!
    
    gameMode.useItem(player, preferMainHand)    // end fish
    gameMode.useItem(player, preferCoMainHand)    // start fish
  }
}

object AutoFishing {
  var job: Job<HookBiteEvent>? = null
  
  private fun onToggle(e: TickEvent.ClientTickEvent) {
    val oldJob = job
    
    if (oldJob != null && ! oldJob.isCancelled) {
      oldJob.cancel()
      job = null
    } else {
      job = AutoFishingJob()
    }
  }
  
  fun initialize() {
    KeyBindings.addListener(KeyBindings.keyAutoFishing, AutoFishing::onToggle)
  }
}