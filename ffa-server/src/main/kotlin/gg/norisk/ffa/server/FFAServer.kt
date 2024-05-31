package gg.norisk.ffa.server

import gg.norisk.ffa.server.selector.SelectorServerManager
import gg.norisk.ffa.server.world.WorldManager
import net.fabricmc.api.DedicatedServerModInitializer
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

object FFAServer : DedicatedServerModInitializer {
    val modId = "ffa-server"
    val logger = LogManager.getLogger(modId)
    fun String.toId(): Identifier = Identifier(modId, this)
    override fun onInitializeServer() {
        logger.info("MinecraftStuff: ${PlayerEntity::class.java.canonicalName}")
        logger.info("MinecraftStuff: ${PlayerEntity.ID_KEY}")
        logger.info("MinecraftStuff: ${PlayerEntity.PASSENGERS_KEY}")
        //GIBT ES
        logger.info("Exists???: ${Class.forName("net.minecraft.class_2943").getField("field_13323")}")
        logger.info("Error: ${TrackedDataHandlerRegistry.BOOLEAN}")
        SelectorServerManager.initServer()
        WorldManager.initServer()
    }
}
