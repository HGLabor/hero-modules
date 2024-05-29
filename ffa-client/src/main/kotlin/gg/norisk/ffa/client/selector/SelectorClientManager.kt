package gg.norisk.ffa.client.selector

import gg.norisk.ffa.client.selector.ui.HeroSelectorScreen
import gg.norisk.ffa.event.EntityEvents
import gg.norisk.ffa.network.ffaTracker
import gg.norisk.ffa.network.isFFA
import gg.norisk.ffa.network.selectorScreenPacket
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.silkmc.silk.core.task.mcCoroutineTask

object SelectorClientManager {
    fun initClient() {
        selectorScreenPacket.receiveOnClient { heroes, _ ->
            mcCoroutineTask(sync = true, client = true) {
                println("Heroes: $heroes")
                MinecraftClient.getInstance().setScreen(HeroSelectorScreen(heroes))
            }
        }
        EntityEvents.onTrackedDataSetEvent.listen { event ->
            val player = event.entity as? PlayerEntity ?: return@listen
            if (player != MinecraftClient.getInstance().player) return@listen

            if (ffaTracker.equals(event.data)) {
                if (player.isFFA) {
                    if (MinecraftClient.getInstance().currentScreen is HeroSelectorScreen) {
                        MinecraftClient.getInstance().setScreen(null)
                    }
                }
            }
        }
    }
}
