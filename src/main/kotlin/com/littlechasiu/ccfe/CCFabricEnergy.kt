package com.littlechasiu.ccfe

import dan200.computercraft.api.ComputerCraftAPI
import dan200.computercraft.api.peripheral.IPeripheral
import dan200.computercraft.api.peripheral.IPeripheralProvider
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import team.reborn.energy.api.EnergyStorage

object CCFabricEnergy {
  const val MODID: String = "cc-fabric-energy"
  val LOGGER: Logger = LogManager.getLogger(MODID)

  fun registerPeripheralProviders() {
    ComputerCraftAPI.registerPeripheralProvider(PeripheralProvider())
  }

  class PeripheralProvider : IPeripheralProvider {
    override fun getPeripheral(
      level: Level,
      pos: BlockPos,
      direction: Direction
    ): IPeripheral? =
      EnergyStorage.SIDED.find(level, pos, direction)
        ?.let { EnergyPeripheral(it, level.getBlockEntity(pos)!!) }
  }
}
