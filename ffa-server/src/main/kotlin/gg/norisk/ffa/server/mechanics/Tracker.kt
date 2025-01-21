package gg.norisk.ffa.server.mechanics

import gg.norisk.ffa.network.SelectorPackets.isFFA
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.network.packet.s2c.play.PlayerSpawnPositionS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.silkmc.silk.core.item.itemStack
import net.silkmc.silk.core.item.setCustomName
import net.silkmc.silk.core.server.players
import net.silkmc.silk.core.text.sendText
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import kotlin.math.sqrt

object Tracker {
    val tracker
        get() = itemStack(Items.COMPASS) {
            setCustomName {
                text("Tracker") {
                    bold = true
                    italic = false
                }
            }
        }


    fun onTrackerUse(
        playerEntity: PlayerEntity,
        stack: ItemStack,
        cir: CallbackInfoReturnable<TypedActionResult<ItemStack>>,
        world: World,
        hand: Hand
    ) {
        val player = playerEntity as? ServerPlayerEntity ?: return
        if (ItemStack.areItemsAndComponentsEqual(tracker, stack)) {
            val nearestPlayer = player.nearestPlayerInfo()?.first
            if (nearestPlayer != null) {
                val distance = player.nearestPlayerInfo()?.second?.toInt()
                player.sendText {
                    text(nearestPlayer.name.string)
                    text(" ist ")
                    text(distance.toString())
                    text(" Blöcke entfernt")
                }
                player.networkHandler.sendPacket(
                    PlayerSpawnPositionS2CPacket(
                        BlockPos(
                            nearestPlayer.x.toInt(),
                            nearestPlayer.y.toInt(),
                            nearestPlayer.z.toInt()
                        ), 0.0F
                    )
                )
            } else {
                player.sendText("Es konnte kein Spieler gefunden werden") {
                    color = 0xFF4B4B
                }
            }
        }
    }

    private fun ServerPlayerEntity.nearestPlayerInfo(): Pair<ServerPlayerEntity, Double>? {
        val playerDistances: MutableMap<ServerPlayerEntity, Double> = mutableMapOf()
        for (player in server.players) {
            if (!player.isFFA) continue
            if (world != player.world) continue
            val distance = sqrt(this.squaredDistanceTo(Vec3d(player.x, this.y, player.z)))
            if (distance > 10) {
                playerDistances[player] = distance
            }
        }
        return playerDistances.minByOrNull { it.value }?.toPair()
    }
}