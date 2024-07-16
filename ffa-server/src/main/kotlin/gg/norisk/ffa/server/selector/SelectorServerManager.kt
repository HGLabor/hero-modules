package gg.norisk.ffa.server.selector

import gg.norisk.ffa.network.isFFA
import gg.norisk.ffa.network.selectorHeroPacket
import gg.norisk.ffa.network.selectorScreenPacket
import gg.norisk.ffa.server.world.WorldManager.findSpawnLocation
import gg.norisk.ffa.server.world.WorldManager.getCenter
import gg.norisk.heroes.common.hero.HeroManager
import gg.norisk.heroes.common.hero.setHero
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.GameMode

object SelectorServerManager : ServerEntityEvents.Load {
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
            player.giveItemStack(ItemStack(Items.PIG_SPAWN_EGG, 64))
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
        setHero(null)
        selectorScreenPacket.send(HeroManager.registeredHeroes.keys.toList(), this)
        val spawn = server.overworld.getCenter().toCenterPos()
        this.teleport(server.overworld, spawn.x, spawn.y, spawn.z, 0f, 0f)
    }
}
