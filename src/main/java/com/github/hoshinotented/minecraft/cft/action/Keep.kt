package com.github.hoshinotented.minecraft.cft.action

import com.github.hoshinotented.minecraft.cft.config.Axioms
import com.github.hoshinotented.minecraft.cft.event.KeyBindings
import com.github.hoshinotented.minecraft.cft.util.AbstractJob
import com.github.hoshinotented.minecraft.cft.util.Job
import net.minecraft.client.Minecraft
import net.minecraft.world.InteractionHand
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.TickEvent

/**
 * The [Job] for keeping use item.
 */
private class UsingJob : AbstractJob<TickEvent.ClientTickEvent>("keep.using") {
  private var cooldown: Int = 0
  
  init {
    initialize()
  }
  
  @SubscribeEvent
  override fun onTick(event: TickEvent.ClientTickEvent) {
    if (isCancelled) return
    if (cooldown > 0) {
      cooldown --
      return
    }
    
    val mc = Minecraft.getInstance()
    val gameMode = mc.gameMode ?: return
    val player = mc.player ?: return
    
    val isHalt = gameMode.isDestroying || player.isHandsBusy
    if (isHalt) {
      cancel()
      return
    }
    
    for (hand in InteractionHand.entries) {
      if (onUse(hand)) {
        // success, reset cool down
        cooldown = Axioms.INSTANCE.keepUsingCoolDown
        return
      }
    }
  }
  
  private fun onUse(hand: InteractionHand): Boolean {
    if (isCancelled) return false
    
    val mc = Minecraft.getInstance()
    val hitResult = mc.hitResult
    val gameMode = mc.gameMode ?: return false
    val player = mc.player ?: return false
    val level = mc.level ?: return false
    
    if (hitResult is BlockHitResult && ! level.getBlockState(hitResult.blockPos).isAir) {
      // use on block
      return gameMode.useItemOn(player, hand, hitResult).consumesAction()
    } else {
      // use item
      // TODO: use empty hand on air? sounds weird.
      if (player.getItemInHand(hand).isEmpty) return false
      return gameMode.useItem(player, hand).consumesAction()
    }
  }
}

object Keep {
  var job: Job<TickEvent.ClientTickEvent>? = null
  
  
  private fun onToggleUsing(event: TickEvent.ClientTickEvent) {
    val oldJob = job
    
    reset()
    
    if (oldJob == null || oldJob !is UsingJob) {
      using(event)
    }
  }
  
  fun using(event: TickEvent.ClientTickEvent) {
    job = UsingJob()
  }
  
  fun reset() {
    val job = job
    if (job != null && !job.isCancelled) {
      job.cancel()
      this.job = null
    }
  }
  
  fun initialize() {
    KeyBindings.addListener(KeyBindings.keyKeepUsing, this::onToggleUsing)
  }
}