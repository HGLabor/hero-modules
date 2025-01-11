package gg.norisk.ffa.server

import gg.norisk.ffa.FFACommon.isServer
import gg.norisk.ffa.server.selector.SelectorServerManager
import gg.norisk.ffa.server.world.WorldManager
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

object FFAServer : ModInitializer {
    private const val MOD_ID = "ffa-server"
    val logger = LogManager.getLogger(MOD_ID)
    fun String.toId(): Identifier = Identifier.of(MOD_ID, this)

    override fun onInitialize() {
        if (!isServer) return
        SelectorServerManager.initServer()
        WorldManager.initServer()
    }
}
