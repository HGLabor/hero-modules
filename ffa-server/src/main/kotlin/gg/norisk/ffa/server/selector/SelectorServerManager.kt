package gg.norisk.ffa.server.selector

import gg.norisk.ffa.network.SelectorPackets.isFFA
import gg.norisk.ffa.network.SelectorPackets.selectorHeroPacket
import gg.norisk.ffa.network.SelectorPackets.selectorScreenPacket
import gg.norisk.ffa.server.world.WorldManager.findSpawnLocation
import gg.norisk.ffa.server.world.WorldManager.getCenter
import gg.norisk.heroes.common.hero.HeroManager
import gg.norisk.heroes.common.hero.setHero
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.GameMode

object SelectorServerManager {
    fun initServer() {
        selectorHeroPacket.receiveOnServer { heroId, context ->
            val player = context.player
            val server = context.server
            player.changeGameMode(GameMode.SURVIVAL)
            player.isFFA = true
            val spawn = server.overworld.findSpawnLocation().toCenterPos()
            player.teleport(server.overworld, spawn.x, spawn.y, spawn.z, 0f, 0f)
            val hero = HeroManager.getHero(heroId)
            player.setHero(hero)
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

    private fun ServerPlayerEntity.setSelectorReady() {
        this.health = this.maxHealth
        isFFA = false
        changeGameMode(GameMode.SPECTATOR)
        setHero(null)
        selectorScreenPacket.send(HeroManager.registeredHeroes.keys.toList(), this)
        val spawn = server.overworld.getCenter().toCenterPos()
        this.teleport(server.overworld, spawn.x, spawn.y, spawn.z, 0f, 0f)
    }
}
