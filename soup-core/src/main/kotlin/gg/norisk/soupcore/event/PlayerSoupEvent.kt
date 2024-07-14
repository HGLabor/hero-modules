package gg.norisk.soupcore.event

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import net.silkmc.silk.core.event.Event

class PlayerSoupEvent(val player: PlayerEntity, val itemStack: ItemStack) {
    val overhealed: Boolean = (player.health + 7) > player.maxHealth
}

@OptIn(ExperimentalSilkApi::class)
val playerSoupEvent = Event.onlySync<PlayerSoupEvent>()
