package gg.norisk.ffa.network

import gg.norisk.ffa.FFACommon.toId
import gg.norisk.heroes.common.entity.getSyncedData
import gg.norisk.heroes.common.entity.setSyncedData
import net.minecraft.entity.player.PlayerEntity
import net.silkmc.silk.network.packet.c2sPacket
import net.silkmc.silk.network.packet.s2cPacket

val selectorScreenPacket = s2cPacket<List<String>>("selector-screen".toId())
val selectorHeroPacket = c2sPacket<String>("selector-hero".toId())

const val FFA_KEY = "hero-ffa"
var PlayerEntity.isFFA: Boolean
    get() {
        return this.getSyncedData<Boolean>(FFA_KEY) ?: false
    }
    set(value) {
        this.setSyncedData(FFA_KEY, value)
    }
