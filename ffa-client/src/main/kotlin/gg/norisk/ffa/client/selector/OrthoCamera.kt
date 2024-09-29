package gg.norisk.ffa.client.selector

import gg.norisk.ffa.utils.Animation
import gg.norisk.ffa.client.selector.ui.HeroSelectorScreen
import gg.norisk.heroes.common.hero.HeroManager
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.MinecraftClient
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.RotationAxis
import net.silkmc.silk.commands.clientCommand
import net.silkmc.silk.core.kotlin.ticks
import net.silkmc.silk.core.task.mcCoroutineTask
import org.joml.Matrix4f
import org.joml.Quaternionf
import kotlin.math.max
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration


object OrthoCamera : ClientTickEvents.EndTick {
    val isEnabled get() = MinecraftClient.getInstance().currentScreen is HeroSelectorScreen
    var yawAnimation = Animation(0f, 360f, 2.minutes.toJavaDuration())

    fun initClient() {
        if (FabricLoader.getInstance().isDevelopmentEnvironment) {
            clientCommand("heroscreen") {
                runs {
                    mcCoroutineTask(delay = 1.ticks, sync = true, client = true) {
                        MinecraftClient.getInstance().setScreen(HeroSelectorScreen(HeroManager.registeredHeroes.values.toList()))
                    }
                }
            }
        }
        ClientTickEvents.END_CLIENT_TICK.register(this)
    }

    fun createOrthoMatrix(delta: Float, minScale: Float): Matrix4f {
        val client: MinecraftClient = MinecraftClient.getInstance()
        val scale = 100f
        val width = max(minScale, scale * client.window.framebufferWidth / client.window.framebufferHeight)
        val height = max(minScale, scale)
        return Matrix4f().setOrtho(
            -width, width,
            -height, height,
            -1000.0F, 1000.0f
        )
    }

    fun handlePitch(quaternion: Quaternionf, tickDelta: Float): Float {
        return 30f * MathHelper.RADIANS_PER_DEGREE
    }

    fun handleYaw(quaternion: Quaternionf, tickDelta: Float): Float {
        if (yawAnimation.isDone) {
            yawAnimation.reset()
        }
        return yawAnimation.get() * MathHelper.RADIANS_PER_DEGREE
    }

    override fun onEndTick(client: MinecraftClient) {
    }
}
