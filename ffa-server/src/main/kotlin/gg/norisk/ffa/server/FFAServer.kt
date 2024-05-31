package gg.norisk.ffa.server

import gg.norisk.ffa.server.selector.SelectorServerManager
import gg.norisk.ffa.server.world.WorldManager
import kotlinx.serialization.Serializable
import net.fabricmc.api.DedicatedServerModInitializer
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

object FFAServer : DedicatedServerModInitializer {
    val modId = "ffa-server"
    val logger = LogManager.getLogger(modId)
    fun String.toId(): Identifier = Identifier(modId, this)
    override fun onInitializeServer() {
        SelectorServerManager.initServer()
        WorldManager.initServer()
    }

    @Serializable
    data class xd(val lol: Int)
}
