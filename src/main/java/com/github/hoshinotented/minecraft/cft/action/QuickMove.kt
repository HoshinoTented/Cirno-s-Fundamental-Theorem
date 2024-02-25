package com.github.hoshinotented.minecraft.cft.action

import com.github.hoshinotented.minecraft.cft.event.KeyBindings
import com.github.hoshinotented.minecraft.cft.network.Packet
import com.github.hoshinotented.minecraft.cft.util.Slots
import net.minecraft.client.Minecraft
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.SimpleContainer
import net.minecraft.world.inventory.AbstractFurnaceMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.neoforged.neoforge.common.CommonHooks
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.registration.NetworkChannel

object QuickMove {
  private const val rayTraceDistance = 20.0
  
  /**
   * Move the main hand item of [player] to the furnace
   *
   * Note that this function won't work well with mod furnace,
   * for example, TODO: the furnace that has more than 1 input/fuel slot.
   */
  private fun doQuickMove(player: ServerPlayer, entity: AbstractFurnaceBlockEntity) {
    val mainHand = player.mainHandItem
    if (mainHand.isEmpty) {
      // quick take
      // 2 is AbstractFurnaceBlockEntity.SLOT_RESULT which is protected
      val resultItems = entity.getItem(2)
      player.inventory.add(resultItems)
    } else {
      var success = false
      if (canSmelt(player.level(), mainHand)) {
        val slots = Slots(listOf(Slot(entity, 0, 0, 0)))
        val result = slots.addResource(mainHand)
        
        success = result > 0
      }
      
      if (! success && isFuel(mainHand)) {
        val slots = Slots(listOf(Slot(entity, 1, 0, 0)))
        slots.addResource(mainHand)
      }
    }
  }
  
  fun doQuickMove(player: ServerPlayer) {
    val result = player.pick(rayTraceDistance, 0.0F, false)
    if (result is BlockHitResult) {
      val blockEntity = player.level().getBlockEntity(result.blockPos) ?: return
      if (blockEntity is AbstractFurnaceBlockEntity) {
        doQuickMove(player, blockEntity)
      }
    }
  }
  
  /**
   * @see AbstractFurnaceMenu.isFuel
   */
  fun isFuel(item: ItemStack): Boolean {
    return CommonHooks.getBurnTime(item, RecipeType.SMELTING) > 0
  }
  
  /**
   * @see AbstractFurnaceMenu.canSmelt
   */
  fun canSmelt(where: Level, stack: ItemStack): Boolean {
    return where.recipeManager
      .getRecipeFor(RecipeType.SMELTING, SimpleContainer(stack), where)
      .isPresent
  }
  
  fun initialize() {
    KeyBindings.addListener(KeyBindings.keyQuickMove) {
      val mc = Minecraft.getInstance()
      val player = mc.player ?: return@addListener
      val pointed = mc.hitResult ?: return@addListener
      if (pointed.type != HitResult.Type.BLOCK) return@addListener
      
      
      PacketDistributor.SERVER.noArg().send(Packet(player.uuid))
    }
  }
}