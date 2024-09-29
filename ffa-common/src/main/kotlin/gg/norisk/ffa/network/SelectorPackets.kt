package gg.norisk.ffa.network

import gg.norisk.datatracker.entity.getSyncedData
import gg.norisk.datatracker.entity.setSyncedData
import gg.norisk.ffa.FFACommon.toId
import net.minecraft.entity.player.PlayerEntity
import net.silkmc.silk.network.packet.c2sPacket
import net.silkmc.silk.network.packet.s2cPacket

object SelectorPackets {
    val selectorScreenPacket = s2cPacket<List<String>>("selector-screen".toId())
    val selectorHeroPacket = c2sPacket<String>("selector-hero".toId())

    fun init() {}

    const val FFA_KEY = "hero-ffa"
    var PlayerEntity.isFFA: Boolean
        get() {
            return this.getSyncedData<Boolean>(FFA_KEY) ?: false
        }
        set(value) {
            this.setSyncedData(FFA_KEY, value)
        }
}
