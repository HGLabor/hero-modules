package gg.norisk.ffa.client.selector

import gg.norisk.datatracker.entity.syncedValueChangeEvent
import gg.norisk.ffa.client.selector.ui.HeroSelectorScreen
import gg.norisk.ffa.network.SelectorPackets.FFA_KEY
import gg.norisk.ffa.network.SelectorPackets.isFFA
import gg.norisk.ffa.network.SelectorPackets.selectorScreenPacket
import gg.norisk.heroes.common.hero.HeroManager
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import net.silkmc.silk.core.task.mcCoroutineTask

object SelectorClientManager {
    @OptIn(ExperimentalSilkApi::class)
    fun initClient() {
        selectorScreenPacket.receiveOnClient { heroIds, _ ->
            mcCoroutineTask(sync = true, client = true) {
                openHeroScreen(heroIds)
            }
        }
        syncedValueChangeEvent.listen { event ->
            mcCoroutineTask(sync = true, client = true) {
                val player = event.entity as? PlayerEntity ?: return@mcCoroutineTask
                if (player != MinecraftClient.getInstance().player) return@mcCoroutineTask

                when (event.key) {
                    FFA_KEY -> {
                        if (player.isFFA) {
                            if (MinecraftClient.getInstance().currentScreen is HeroSelectorScreen) {
                                MinecraftClient.getInstance().setScreen(null)
                            }
                        }
                    }
                }
            }
        }
    }

    fun openHeroScreen(heroIds: List<String>) {
        val heroes = buildList {
            for (heroId in heroIds) {
                add(HeroManager.getHero(heroId) ?: continue)
            }
        }
        MinecraftClient.getInstance().setScreen(HeroSelectorScreen(heroes))
    }
}
