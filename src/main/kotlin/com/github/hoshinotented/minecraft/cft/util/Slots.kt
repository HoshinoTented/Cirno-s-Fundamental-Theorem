package com.github.hoshinotented.minecraft.cft.util

import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

/**
 * [Slots] provides a group of [Slot]s, behaviors like [net.minecraft.world.entity.player.Inventory] but not owns the [Slot]s
 */
data class Slots(val inner: List<Slot>) {
  /**
   * @return amount of transferred items
   * @see net.minecraft.world.entity.player.Inventory.add
   */
  fun addResource(stack: ItemStack): Int {
    val available = stack.count
    var slot: Slot? = slotWithRemainingSpace(stack) ?: freeSlot(stack)    // prefer existing stack
    
    while (slot != null && ! stack.isEmpty) {
      addResource(slot, stack)
      slot = slotWithRemainingSpace(stack) ?: freeSlot(stack)
    }
    
    return available - stack.count
  }
  
  fun freeSlot(stack: ItemStack): Slot? {
    return inner.firstOrNull { it.item.isEmpty && it.mayPlace(stack) }
  }
  
  /**
   * Getting the slot that have free space for [stack], exclude empty slot.
   */
  fun slotWithRemainingSpace(stack: ItemStack): Slot? {
    if (stack.isDamaged) return null
    return inner.asSequence()
      .filter { ! it.item.isEmpty }
      .firstOrNull {
        ItemStack.isSameItemSameTags(it.item, stack) && it.freeCount > 0
      }
  }
  
  /**
   * @return amount of transferred items
   * @see net.minecraft.world.entity.player.Inventory.addResource
   */
  fun addResource(slot: Slot, stack: ItemStack): Int {
    val old = stack.count
    val new = slot.safeInsert(stack).count
    
    return old - new
  }
}

class SlotsBuilder {
  private val slots: MutableList<Slot> = arrayListOf()
  
  operator fun Slot.unaryPlus(): Slot {
    val newSlot = Slot(this.container, this.slotIndex, 0, 0)
    
    newSlot.index = slots.size
    slots.add(newSlot)
    
    return newSlot
  }
  
  fun build(): Slots {
    return Slots(slots)
  }
}