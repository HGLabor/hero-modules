package gg.norisk.ffa.client.selector

import gg.norisk.ffa.client.selector.ui.HeroSelectorScreen
import gg.norisk.ffa.network.SelectorPackets.FFA_KEY
import gg.norisk.ffa.network.SelectorPackets.isFFA
import gg.norisk.ffa.network.SelectorPackets.selectorScreenPacket
import gg.norisk.heroes.common.entity.syncedValueChangeEvent
import gg.norisk.heroes.common.hero.HeroManager
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import net.silkmc.silk.core.task.mcCoroutineTask
import net.silkmc.silk.core.text.literalText

object SelectorClientManager {
    @OptIn(ExperimentalSilkApi::class)
    fun initClient() {
        selectorScreenPacket.receiveOnClient { heroIds, _ ->
            MinecraftClient.getInstance().inGameHud.chatHud.addMessage(literalText("selector screen packet"))
            mcCoroutineTask(sync = true, client = true) {
                openHeroScreen(heroIds)
            }
        }
        syncedValueChangeEvent.listen { event ->
            MinecraftClient.getInstance().inGameHud.chatHud.addMessage(literalText("sync value change event"))
            MinecraftClient.getInstance().inGameHud.chatHud.addMessage(literalText("Key: ${event.key}"))
            val player = event.entity as? PlayerEntity ?: return@listen
            if (player != MinecraftClient.getInstance().player) return@listen

            if (event.key == FFA_KEY) {
                MinecraftClient.getInstance().inGameHud.chatHud.addMessage(literalText("is FFA"))
                if (player.isFFA) {
                    MinecraftClient.getInstance().inGameHud.chatHud.addMessage(literalText("player is FFA"))
                    if (MinecraftClient.getInstance().currentScreen is HeroSelectorScreen) {
                        MinecraftClient.getInstance().setScreen(null)
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
