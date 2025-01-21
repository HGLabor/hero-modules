package gg.norisk.ffa.server.mechanics

import gg.norisk.heroes.common.db.ExperienceManager
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable


object SoupHealing {
    const val SOUP_HEAL = 7F
    fun onPotentialSoupUse(
        player: PlayerEntity, item: Item,
        cir: CallbackInfoReturnable<TypedActionResult<ItemStack>>,
        world: World,
        hand: Hand
    ) {
        val foodData = player.hungerManager

        if (!item.isStew || player.health >= player.maxHealth && !foodData.isNotFull) return

        var consumedSoup = false

        if (player.health < player.maxHealth) {
            player.heal(item.soupHealing)
            if (item == Items.SUSPICIOUS_STEW) {
                //player.addEffect(MobEffectInstance(MobEffects.BLINDNESS, 60, 1))
                //player.addEffect(MobEffectInstance(MobEffects.WEAKNESS, 60, 1))
            }
            consumedSoup = true
        } else if (foodData.isNotFull) {
            foodData.foodLevel += item.restoredFood
            consumedSoup = true
        }

        if (consumedSoup) {
            (player as? ServerPlayerEntity?)?.apply {
                ExperienceManager.addXp(this, ExperienceManager.SOUP_EATEN)
            }
            cir.returnValue = TypedActionResult.pass(ItemStack(Items.BOWL))
        }
    }

    private val Item.isStew: Boolean
        get() = when (this) {
            Items.MUSHROOM_STEW -> true
            Items.BEETROOT_SOUP -> true
            Items.RABBIT_STEW -> true
            Items.SUSPICIOUS_STEW -> true
            else -> false
        }

    val Item.soupHealing: Float
        get() = when (this) {
            Items.MUSHROOM_STEW -> 7.0f
            Items.BEETROOT_SOUP -> 3.0f // mushroom cow nerf
            Items.RABBIT_STEW -> 8.0f // used in perfect kit
            Items.SUSPICIOUS_STEW -> 2.0f // spit
            else -> 0f
        }

    private val Item.restoredFood: Int
        get() = 7 // this.foodProperties?.nutrition ?: 0

    private val Item.restoredSaturation: Float
        get() = 3f //this.foodProperties?.saturationModifier ?: 0f
}
