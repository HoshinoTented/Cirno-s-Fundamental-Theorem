@file:JvmName("ItemUtil")

package com.github.hoshinotented.minecraft.cft.util

import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import kotlin.math.min

val ItemStack.freeCount : Int get() = this.maxStackSize - this.count
val Slot.freeCount : Int get() = min(this.container.maxStackSize, this.item.maxStackSize) - this.item.count