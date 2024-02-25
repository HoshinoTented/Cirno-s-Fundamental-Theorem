package com.github.hoshinotented.minecraft.cft.util

import com.github.hoshinotented.minecraft.cft.FundamentalTheorem
import net.minecraft.server.MinecraftServer

fun getServerInstance(): MinecraftServer? {
  return FundamentalTheorem.server
}