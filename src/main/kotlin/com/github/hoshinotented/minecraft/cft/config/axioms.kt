package com.github.hoshinotented.minecraft.cft.config

import net.neoforged.neoforge.common.ModConfigSpec


class Axioms(builder: ModConfigSpec.Builder) {
  companion object {
    private val INSTANCE_AND_CONFIG_SPEC = ModConfigSpec.Builder()
      .configure(::Axioms)
    
    val INSTANCE: Axioms = INSTANCE_AND_CONFIG_SPEC.left
    val CONFIG_SPEC: ModConfigSpec = INSTANCE_AND_CONFIG_SPEC.right
  }
  
  private val keepUsingCoolDownValue: ModConfigSpec.ConfigValue<Int> = builder.comment("The cooldown of keep using (tick), default 0")
    .define("keepUsingCoolDown", 0)
  
  val keepUsingCoolDown: Int get() = keepUsingCoolDownValue.get()
}