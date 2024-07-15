package gg.norisk.soupcore.items

import gg.norisk.soupcore.event.PlayerSoupEvent
import gg.norisk.soupcore.event.playerSoupEvent
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import net.silkmc.silk.core.annotations.ExperimentalSilkApi
import kotlin.math.min

class SoupItem: Item(Settings()) {

    @OptIn(ExperimentalSilkApi::class)
    override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (hand == Hand.OFF_HAND) return TypedActionResult.fail(player.offHandStack)
        val itemStack = player.getStackInHand(hand)
        player.apply {
            if (health >= maxHealth - 0.4 && hungerManager.foodLevel >= 20) return@apply
            playerSoupEvent.invoke(PlayerSoupEvent(player, itemStack))
            health = min(maxHealth, health + 7)
            hungerManager.foodLevel = min(20, hungerManager.foodLevel + 6)
            hungerManager.saturationLevel = min(8f, hungerManager.saturationLevel + 6)
            itemStack.count -= 1
            player.giveItemStack(Items.BOWL.defaultStack)
        }
        return TypedActionResult.pass(player.getStackInHand(hand))
    }
}