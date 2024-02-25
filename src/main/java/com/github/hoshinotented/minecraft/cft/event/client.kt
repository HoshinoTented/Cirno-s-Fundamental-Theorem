package com.github.hoshinotented.minecraft.cft.event

import com.github.hoshinotented.minecraft.cft.FundamentalTheorem
import com.github.hoshinotented.minecraft.cft.action.AutoFishing
import com.github.hoshinotented.minecraft.cft.action.Keep
import com.github.hoshinotented.minecraft.cft.action.QuickMove
import com.github.hoshinotented.minecraft.cft.config.Axioms
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent

@Mod.EventBusSubscriber(value = [ Dist.CLIENT ], modid = FundamentalTheorem.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
object ClientEventHandler {
  @SubscribeEvent
  fun onClientSetup(event: FMLClientSetupEvent) {
    val axioms = Axioms.INSTANCE
    
    Keep.initialize()
    QuickMove.initialize()
    AutoFishing.initialize()
  }
}