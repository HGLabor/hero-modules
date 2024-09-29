package gg.norisk.ffa.client

import gg.norisk.ffa.FFACommon.isClient
import gg.norisk.ffa.client.selector.OrthoCamera
import gg.norisk.ffa.client.selector.SelectorClientManager
import net.fabricmc.api.ModInitializer
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

object FFAClient : ModInitializer {
    const val MOD_ID = "ffa-client"
    val logger = LogManager.getLogger(MOD_ID)
    fun String.toId(): Identifier = Identifier.of(MOD_ID, this)

    override fun onInitialize() {
        if (!isClient) return
        SelectorClientManager.initClient()
        OrthoCamera.initClient()
    }
}
