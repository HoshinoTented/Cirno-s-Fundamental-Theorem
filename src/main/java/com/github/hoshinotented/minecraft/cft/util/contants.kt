package com.github.hoshinotented.minecraft.cft.util

import com.github.hoshinotented.minecraft.cft.FundamentalTheorem.Companion.MODID


fun ofMessage(id: String): String {
  return "message.$MODID.$id"
}

fun ofKey(id: String): String {
  return "key.$MODID.$id"
}