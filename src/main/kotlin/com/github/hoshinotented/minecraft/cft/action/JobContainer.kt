package com.github.hoshinotented.minecraft.cft.action

import com.github.hoshinotented.minecraft.cft.util.Job
import net.neoforged.bus.api.Event

interface JobContainer<T : Event> {
  val job: Job<T>?
  fun cancel()
}