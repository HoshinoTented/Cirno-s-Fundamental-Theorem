package com.github.hoshinotented.minecraft.cft.event

import com.github.hoshinotented.minecraft.cft.FundamentalTheorem
import com.github.hoshinotented.minecraft.cft.action.AutoFishing
import com.github.hoshinotented.minecraft.cft.action.Keep
import com.github.hoshinotented.minecraft.cft.config.Axioms
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

@Mod.EventBusSubscriber(value = [ Dist.CLIENT ], modid = FundamentalTheorem.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
object ClientEventHandler {
  @SubscribeEvent
  fun onClientSetup(event: FMLClientSetupEvent) {
    Keep.initialize()
    AutoFishing.initialize()
  }
}