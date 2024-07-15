package gg.norisk.ffa.server

import gg.norisk.ffa.server.selector.SelectorServerManager
import gg.norisk.ffa.server.world.WorldManager
import net.fabricmc.api.DedicatedServerModInitializer
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

object FFAServer : DedicatedServerModInitializer {
    private const val MOD_ID = "ffa-server"
    val logger = LogManager.getLogger(MOD_ID)
    fun String.toId(): Identifier = Identifier(MOD_ID, this)
    override fun onInitializeServer() {
        SelectorServerManager.initServer()
        WorldManager.initServer()
    }
}
