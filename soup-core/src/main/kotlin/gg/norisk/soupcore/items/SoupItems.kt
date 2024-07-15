package gg.norisk.soupcore.items

import gg.norisk.soupcore.SoupCore.toId
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object SoupItems {
    val MUSHROOM_SOUP = SoupItem()
    val CACTUS_SOUP = SoupItem()
    val CHOCOLATE_MILK = SoupItem()

    fun initialize() {
        register(MUSHROOM_SOUP, "mushroom_soup")
        register(CACTUS_SOUP, "cactus_soup")
        register(CHOCOLATE_MILK, "chocolate_milk")
    }

    private fun register(instance: Item, path: String): Item {
        return Registry.register(Registries.ITEM, path.toId(), instance)
    }
}