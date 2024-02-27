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
  private const val OBFUSCATED: String = "f_37099_"
  private const val FIELD_NAME: String = "biting"
  
  /**
   * See [FishingHook.biting]
   */
  private val FIELD: Field? by lazy {
    try {
      try {
        FishingHook::class.java.getDeclaredField(OBFUSCATED)
      } catch (e: NoSuchFieldException) {
        FishingHook::class.java.getDeclaredField(FIELD_NAME)
      }
    } catch (e: Exception) {
      null
    }
  }
  
  private var didWarn: Boolean = false
  private var lastBiting: Boolean = false
  
  @SubscribeEvent
  @Suppress("unused")
  fun onClientTick(event: TickEvent.ClientTickEvent) {
    val field = FIELD
    
    if (field == null) {
      if (! didWarn) {
        didWarn = true
        FundamentalTheorem.LOGGER.warn("FishingHook#biting not found, probably the obfuscated field name is deprecated.")
      }
      
      return
    }
    
    val mc = Minecraft.getInstance() ?: return
    val player = mc.player ?: return
    val hook = player.fishing ?: return
    
    if (field.trySetAccessible()) {
      val biting = field.getBoolean(hook)
      if (! lastBiting && biting) {
        MinecraftForge.EVENT_BUS.post(HookBiteEvent(hook))
      }
      
      lastBiting = biting
    }
  }
}