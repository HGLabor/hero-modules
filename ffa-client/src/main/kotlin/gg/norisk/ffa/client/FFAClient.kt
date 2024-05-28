package gg.norisk.ffa.client

import gg.norisk.ffa.client.selector.SelectorClientManager
import net.fabricmc.api.ClientModInitializer
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

object FFAClient : ClientModInitializer {
    val modId = "ffa-client"
    val logger = LogManager.getLogger(modId)
    fun String.toId(): Identifier = Identifier(modId, this)
    override fun onInitializeClient() {
        println("Hello Client")
        SelectorClientManager.initClient()
    }
}
