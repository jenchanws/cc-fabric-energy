package com.littlechasiu.ccfe

import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.peripheral.IPeripheral
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import team.reborn.energy.api.EnergyStorage

class EnergyPeripheral(val storage: EnergyStorage, private val entity: BlockEntity): IPeripheral {
  override fun getType() = BlockEntityType.getKey(entity.type).toString()

  override fun getAdditionalTypes() = mutableSetOf("energy_storage")

  override fun equals(other: IPeripheral?) = other is EnergyPeripheral && storage == other.storage

  @LuaFunction(mainThread = true)
  fun getEnergy(): Long = storage.amount

  @LuaFunction(mainThread = true)
  fun getEnergyCapacity(): Long = storage.capacity
}
