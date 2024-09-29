package gg.norisk.ffa

import gg.norisk.ffa.network.SelectorPackets
import net.fabricmc.api.EnvType
import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

object FFACommon : ModInitializer {
    override fun onInitialize() {
        SelectorPackets.init()
    }

    val modId = "ffa-common"
    val logger = LogManager.getLogger(modId)
    fun String.toId(): Identifier = Identifier.of(modId, this)

    val isServer get() = (FabricLoader.getInstance().isDevelopmentEnvironment || FabricLoader.getInstance().environmentType == EnvType.SERVER)
    val isClient get() = (FabricLoader.getInstance().isDevelopmentEnvironment || FabricLoader.getInstance().environmentType == EnvType.CLIENT)
}
