package com.github.hoshinotented.minecraft.cft

import com.github.hoshinotented.minecraft.cft.config.Axioms
import com.mojang.logging.LogUtils
import net.minecraft.server.MinecraftServer
import net.neoforged.fml.ModLoadingContext
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import org.slf4j.Logger

@Mod(FundamentalTheorem.MODID)
class FundamentalTheorem {
  companion object {
    const val MODID = "cirno_fundamental_theorem"
    val LOGGER: Logger = LogUtils.getLogger()
  }
  
  init {
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Axioms.CONFIG_SPEC)
  }
}