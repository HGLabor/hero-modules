package gg.norisk.soupcore

import gg.norisk.soupcore.items.SoupItems
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier

object SoupCore : ModInitializer {
    override fun onInitialize() {
        println("Initializing Soup Core")
        SoupItems.initialize()
    }

    val modId = "soupcore"
    fun String.toId(): Identifier = Identifier(modId, this)
}
