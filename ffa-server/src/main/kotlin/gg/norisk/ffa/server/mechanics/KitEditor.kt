package gg.norisk.ffa.server.mechanics

import gg.norisk.ffa.network.SelectorPackets.isFFA
import gg.norisk.ffa.server.selector.SelectorServerManager.setSelectorReady
import gg.norisk.ffa.server.selector.SelectorServerManager.setSoupItems
import gg.norisk.ffa.server.selector.SelectorServerManager.setUHCItems
import gg.norisk.heroes.common.events.HeroEvents
import gg.norisk.heroes.common.ffa.KitEditorManager
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity

object KitEditor {
    val mode
        get() = System.getProperty(
            "ffa_mode",
            "SOUP"
        )

    fun initServer() {
        HeroEvents.preKitEditorEvent.listen { event ->
            if (event.player.isFFA) {
                event.isCancelled.set(true)
            }
        }
        KitEditorManager.onBack = {
            it.setSelectorReady()
        }
        KitEditorManager.resetInventory = {
            handleKit(it, mode)
        }
    }

    fun handleKit(player: PlayerEntity, mode: String = this.mode) {
        when (KitEditor.mode) {
            "SOUP" -> handleSoupKit(player)
            "UHC" -> handleUHCKit(player)
        }
    }

    private fun handleSoupKit(player: PlayerEntity) {
        player.inventory.clear()
        player.setSoupItems()
    }

    private fun handleUHCKit(player: PlayerEntity) {
        player.inventory.clear()
        player.setUHCItems()
    }
}