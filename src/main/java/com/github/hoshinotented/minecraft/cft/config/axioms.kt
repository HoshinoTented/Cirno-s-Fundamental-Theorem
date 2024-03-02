package com.github.hoshinotented.minecraft.cft.config

import com.github.hoshinotented.minecraft.cft.event.HookBiteBroadcast
import net.minecraftforge.common.ForgeConfigSpec


class Axioms(builder: ForgeConfigSpec.Builder) {
  companion object {
    private val INSTANCE_AND_CONFIG_SPEC = ForgeConfigSpec.Builder()
      .configure(::Axioms)
    
    val INSTANCE: Axioms = INSTANCE_AND_CONFIG_SPEC.left
    val CONFIG_SPEC: ForgeConfigSpec = INSTANCE_AND_CONFIG_SPEC.right
  }
  
  private val keepUsingCoolDownValue: ForgeConfigSpec.ConfigValue<Int> = builder
    .comment("The cooldown of keep using (tick), default 0")
    .define("keepUsingCoolDown", 0)
  
  private val hookBiteStrategyValue: ForgeConfigSpec.EnumValue<HookBiteBroadcast.Strategy> = builder
    .comment("""
      Determine how to detect hook biting, default "DataBased", available value:
      * DataBased: depends on FishingHook.biting, may not work well on server play
      * SoundBased: depends on sound, may report other player's hook biting
      """.trimIndent())
    .defineEnum("hookBiteStrategy", HookBiteBroadcast.Strategy.DataBased)
  
  val keepUsingCoolDown: Int get() = keepUsingCoolDownValue.get()
  val hookBiteStrategy: HookBiteBroadcast.Strategy get() = hookBiteStrategyValue.get()
}