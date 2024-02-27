package com.github.hoshinotented.minecraft.cft.config

import net.minecraftforge.common.ForgeConfigSpec


class Axioms(builder: ForgeConfigSpec.Builder) {
  companion object {
    private val INSTANCE_AND_CONFIG_SPEC = ForgeConfigSpec.Builder()
      .configure(::Axioms)
    
    val INSTANCE: Axioms = INSTANCE_AND_CONFIG_SPEC.left
    val CONFIG_SPEC: ForgeConfigSpec = INSTANCE_AND_CONFIG_SPEC.right
  }
  
  private val keepUsingCoolDownValue: ForgeConfigSpec.ConfigValue<Int> = builder.comment("The cooldown of keep using (tick), default 0")
    .define("keepUsingCoolDown", 0)
  
  val keepUsingCoolDown: Int get() = keepUsingCoolDownValue.get()
}