package gg.norisk.ffa

import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

object FFACommon : ModInitializer {
    override fun onInitialize() {
        println("Hello Common")
    }

    val modId = "ffa-common"
    val logger = LogManager.getLogger(modId)
    fun String.toId(): Identifier = Identifier(modId, this)
}
