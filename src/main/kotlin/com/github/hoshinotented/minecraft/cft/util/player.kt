package com.github.hoshinotented.minecraft.cft.util

import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

inline fun Player.firstHandedItem(predicate: (ItemStack) -> Boolean): InteractionHand? {
  return InteractionHand.entries.firstOrNull {
    predicate(getItemInHand(it))
  }
}

inline fun Player.lastHandedItem(predicate: (ItemStack) -> Boolean): InteractionHand? {
  return InteractionHand.entries.reversed().firstOrNull {
    predicate(getItemInHand(it))
  }
}