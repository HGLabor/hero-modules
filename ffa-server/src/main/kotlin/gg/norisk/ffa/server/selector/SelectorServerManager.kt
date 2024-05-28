package gg.norisk.ffa.server.selector

import gg.norisk.ffa.network.isFFA
import gg.norisk.ffa.network.selectorHeroPacket
import gg.norisk.ffa.network.selectorScreenPacket
import gg.norisk.ffa.server.world.WorldManager.findSpawnLocation
import gg.norisk.ffa.server.world.WorldManager.getCenter
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.minecraft.entity.Entity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.GameMode
import net.silkmc.silk.core.text.literal

object SelectorServerManager : ServerEntityEvents.Load {
    fun initServer() {
        selectorHeroPacket.receiveOnServer { hero, context ->
            val player = context.player
            val server = context.server
            player.changeGameMode(GameMode.SURVIVAL)
            player.isFFA = true
            val spawn = server.overworld.findSpawnLocation().toCenterPos()
            player.sendMessage("Du bist jetzt $hero $spawn".literal)
            player.teleport(server.overworld, spawn.x, spawn.y, spawn.z, 0f, 0f)
        }
        ServerLivingEntityEvents.ALLOW_DEATH.register { entity, _, _ ->
            val player = entity as? ServerPlayerEntity ?: return@register true
            player.setSelectorReady()
            return@register false
        }
        ServerEntityEvents.ENTITY_LOAD.register(this)
    }

    override fun onLoad(entity: Entity, world: ServerWorld) {
        val player = entity as? ServerPlayerEntity ?: return
        if (world == player.server.overworld) {
            player.setSelectorReady()
        }
    }

    private fun ServerPlayerEntity.setSelectorReady() {
        this.health = this.maxHealth
        isFFA = false
        changeGameMode(GameMode.SPECTATOR)
        selectorScreenPacket.send(Unit, this)
        val spawn = server.overworld.getCenter().toCenterPos()
        this.teleport(server.overworld, spawn.x, spawn.y, spawn.z, 0f, 0f)
    }
}
