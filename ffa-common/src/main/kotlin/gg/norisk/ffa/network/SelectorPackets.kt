package gg.norisk.ffa.network

import gg.norisk.datatracker.entity.getSyncedData
import gg.norisk.datatracker.entity.setSyncedData
import net.minecraft.entity.player.PlayerEntity

object SelectorPackets {
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
