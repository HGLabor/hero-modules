package gg.norisk.ffa.server.selector

import gg.norisk.datatracker.entity.setSyncedData
import gg.norisk.ffa.network.SelectorPackets.isFFA
import gg.norisk.ffa.server.mechanics.Scoreboard
import gg.norisk.ffa.server.mechanics.Tracker
import gg.norisk.ffa.server.selector.SelectorServerManager.setSelectorReady
import gg.norisk.ffa.server.world.WorldManager.findSpawnLocation
import gg.norisk.ffa.server.world.WorldManager.getCenter
import gg.norisk.heroes.common.events.HeroEvents
import gg.norisk.heroes.common.hero.HeroManager
import gg.norisk.heroes.common.hero.setHero
import gg.norisk.heroes.common.networking.Networking
import gg.norisk.heroes.common.networking.dto.HeroSelectorPacket
import gg.norisk.utils.DevUtils.uniqueId
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.GameMode
import net.silkmc.silk.core.task.mcCoroutineTask
import net.silkmc.silk.game.sideboard.Sideboard
import java.util.*

object SelectorServerManager {
    private val scoreboards = mutableMapOf<UUID, Sideboard>()

    fun initServer() {
        HeroEvents.heroSelectEvent.listen { event ->
            event.canSelect = true
            val player = event.player as? ServerPlayerEntity ?: return@listen
            val server = player.server
            player.changeGameMode(GameMode.SURVIVAL)
            player.isFFA = true
            val spawn = server.overworld.findSpawnLocation().toCenterPos()
            player.teleport(server.overworld, spawn.x, spawn.y, spawn.z, 0f, 0f)
            player.setArenaReady()
        }
        ServerLivingEntityEvents.ALLOW_DEATH.register { entity, _, _ ->
            val player = entity as? ServerPlayerEntity ?: return@register true
            player.setSelectorReady()
            return@register false
        }
        ServerPlayConnectionEvents.JOIN.register(ServerPlayConnectionEvents.Join { handler, sender, server ->
            handler.player.setSelectorReady()
        })
    }

    private fun ServerPlayerEntity.setArenaReady() {
        setSyncedData("duels:OLD_PVP", true)
        getAttributeInstance(EntityAttributes.GENERIC_ATTACK_SPEED)?.baseValue = 100.0
        inventory.setStack(0, Items.STONE_SWORD.defaultStack)
        repeat(36) {
            giveItemStack(Items.MUSHROOM_STEW.defaultStack)
        }
        inventory.setStack(8, Tracker.tracker)
        inventory.setStack(13, ItemStack(Items.BOWL, 32))
        inventory.setStack(14, ItemStack(Items.RED_MUSHROOM, 32))
        inventory.setStack(15, ItemStack(Items.BROWN_MUSHROOM, 32))
        scoreboards.computeIfAbsent(this.uuid) { Scoreboard.getScoreboardForPlayer(this) }.displayToPlayer(this)
    }

    private fun ServerPlayerEntity.setSelectorReady() {
        this.health = this.maxHealth
        isFFA = false
        changeGameMode(GameMode.SPECTATOR)
        setHero(null)
        Networking.s2cHeroSelectorPacket.send(
            HeroSelectorPacket(HeroManager.registeredHeroes.keys.toList(), true),
            this
        )
        val spawn = server.overworld.getCenter().toCenterPos()
        this.teleport(server.overworld, spawn.x, spawn.y, spawn.z, 0f, 0f)
        scoreboards.computeIfAbsent(this.uuid) { Scoreboard.getScoreboardForPlayer(this) }.hideFromPlayer(this)
    }
}
