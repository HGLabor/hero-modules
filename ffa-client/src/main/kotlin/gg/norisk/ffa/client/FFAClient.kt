package gg.norisk.ffa.client

import gg.norisk.ffa.client.mixin.WorldRendererAccessor
import gg.norisk.ffa.client.selector.OrthoCamera
import gg.norisk.ffa.client.selector.SelectorClientManager
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.util.Identifier
import net.silkmc.silk.core.math.geometry.filledSpherePositionSet
import org.apache.logging.log4j.LogManager

object FFAClient : ClientModInitializer {
    val modId = "ffa-client"
    val logger = LogManager.getLogger(modId)
    fun String.toId(): Identifier = Identifier(modId, this)
    override fun onInitializeClient() {
        SelectorClientManager.initClient()
        OrthoCamera.initClient()
        ClientTickEvents.START_CLIENT_TICK.register { _ ->
            val player = MinecraftClient.getInstance().player ?: return@register
            val worldRenderer = MinecraftClient.getInstance().worldRenderer as WorldRendererAccessor
            player.blockPos.filledSpherePositionSet(5).forEach {
                worldRenderer.invokeScheduleSectionRender(it, true)
            }
        }
    }
}
