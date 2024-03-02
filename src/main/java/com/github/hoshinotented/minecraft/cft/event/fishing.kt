package com.github.hoshinotented.minecraft.cft.event

import com.github.hoshinotented.minecraft.cft.FundamentalTheorem
import com.github.hoshinotented.minecraft.cft.config.Axioms
import net.minecraft.client.Minecraft
import net.minecraft.core.Direction
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.projectile.FishingHook
import net.minecraft.world.phys.Vec3
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.PlayLevelSoundEvent
import net.minecraftforge.event.PlayLevelSoundEvent.AtPosition
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.EntityEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import java.util.EnumSet
import kotlin.math.abs

data class HookBiteEvent(val hook: FishingHook) : EntityEvent(hook)

@Mod.EventBusSubscriber(value = [ Dist.CLIENT ], modid = FundamentalTheorem.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object HookBiteBroadcast {
  
  /**
   * The strategy of how to detect hook biting.
   * * DataBased: Detect hook biting by [FishingHook.biting],
   *   this is a _more_ sound way, but it may not work well on multi-play.
   * * SoundBased: Detect hook biting by [PlayLevelSoundEvent.AtPosition],
   *   this is a _more_ complete way, but it may report other player's hook biting.
   * * Mix: Detect hook biting by both, unsupported yet.
   */
  enum class Strategy {
    DataBased,
    SoundBased,
    // Mix
  }
  
  private var lastBiting: Boolean = false
  private val strategy: Strategy get() = Axioms.INSTANCE.hookBiteStrategy
  private val AXIS : EnumSet<Direction.Axis> = EnumSet.allOf(Direction.Axis::class.java)
  
  @SubscribeEvent
  @Suppress("unused")
  fun onClientTick(event: TickEvent.ClientTickEvent) {
    if (strategy != Strategy.DataBased) return
    
    val mc = Minecraft.getInstance() ?: return
    val player = mc.player ?: return
    val hook = player.fishing ?: return
    val biting = hook.biting
    
    if (! lastBiting && biting) {
      MinecraftForge.EVENT_BUS.post(HookBiteEvent(hook))
    }
    
    lastBiting = biting
  }
  
  /**
   * Check whether two sound position is equal
   * @see net.minecraft.network.protocol.game.ClientboundSoundPacket
   */
  private fun checkEqual(left: Vec3, right: Vec3): Boolean {
    // val planckSize = 8.0
    val leftPos = listOf(left.x, left.y, left.z).map { it.toInt() }
    val rightPos = listOf(right.x, right.y, right.z).map { it.toInt() }
    val diff = leftPos.zip(rightPos).map { abs( it.first - it.second) }
    val isAround = diff.all { it <= 1 }
    
    return isAround
  }
  
  /**
   * @see net.minecraft.world.entity.Entity.playSound
   */
  @SubscribeEvent
  fun onSoundEvent(event: AtPosition) {
    if (strategy != Strategy.SoundBased) return
    if (event.sound?.get() != SoundEvents.FISHING_BOBBER_SPLASH) return
    if (! event.level.isClientSide) return
    
    val mc = Minecraft.getInstance() ?: return
    val player = mc.player ?: return
    val hook = player.fishing ?: return
    val pos = hook.position()
    val soundPos = event.position
    
    if (checkEqual(pos, soundPos)) {
      MinecraftForge.EVENT_BUS.post(HookBiteEvent(hook))
    }
  }
}