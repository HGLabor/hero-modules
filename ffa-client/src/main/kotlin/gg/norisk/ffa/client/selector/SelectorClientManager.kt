package gg.norisk.ffa.client.selector

import gg.norisk.ffa.client.selector.ui.HeroSelectorScreen
import gg.norisk.ffa.network.FFA_KEY
import gg.norisk.ffa.network.isFFA
import gg.norisk.ffa.network.selectorScreenPacket
import gg.norisk.heroes.common.entity.syncedValueChangeEvent
import gg.norisk.heroes.common.hero.HeroManager
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.silkmc.silk.core.task.mcCoroutineTask

object SelectorClientManager {
    fun initClient() {
        selectorScreenPacket.receiveOnClient { heroIds, _ ->
            mcCoroutineTask(sync = true, client = true) {
                val heroes = buildList {
                    for (heroId in heroIds) {
                        add(HeroManager.getHero(heroId) ?: continue)
                    }
                }
                MinecraftClient.getInstance().setScreen(HeroSelectorScreen(heroes))
            }
        }
        syncedValueChangeEvent.listen { event ->
            println("Das kommt rein ${event.toString()}")
            val player = event.entity as? PlayerEntity ?: return@listen
            if (player != MinecraftClient.getInstance().player) return@listen

            if (event.key == FFA_KEY) {
                if (player.isFFA) {
                    if (MinecraftClient.getInstance().currentScreen is HeroSelectorScreen) {
                        MinecraftClient.getInstance().setScreen(null)
                    }
                }
            }
        }
    }
}
