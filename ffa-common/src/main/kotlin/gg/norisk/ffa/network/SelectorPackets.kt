package gg.norisk.ffa.network

import gg.norisk.ffa.FFACommon.toId
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.silkmc.silk.network.packet.c2sPacket
import net.silkmc.silk.network.packet.s2cPacket

val selectorScreenPacket = s2cPacket<List<String>>("selector-screen".toId())
val selectorHeroPacket = c2sPacket<String>("selector-hero".toId())

val ffaTracker = DataTracker.registerData(PlayerEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
var PlayerEntity.isFFA: Boolean
    get() {
        return this.dataTracker.get(ffaTracker)
    }
    set(value) {
        this.dataTracker.set(ffaTracker, value)
    }
