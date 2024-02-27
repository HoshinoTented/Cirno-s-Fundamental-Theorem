package com.github.hoshinotented.minecraft.cft.action

object JobManager {
  val containers: List<JobContainer<*>> = listOf(
    AutoFishing, Keep
  )
  
  fun cancelAll() {
    containers.forEach { it.cancel() }
  }
}