package gg.norisk.ffa.client.selector

import gg.norisk.ffa.client.selector.ui.HeroSelectorScreen
import gg.norisk.ffa.utils.Animation
import gg.norisk.heroes.common.hero.HeroManager
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.RenderLayers
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RotationAxis
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.random.Random
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
                        MinecraftClient.getInstance()
                            .setScreen(HeroSelectorScreen(HeroManager.registeredHeroes.values.toList()))
                    }
                }
            }
        }
        ClientTickEvents.END_CLIENT_TICK.register(this)
    }

    fun renderSquare(matrixStack: MatrixStack, centerPos: BlockPos, state: BlockState, height: Int, sideLength: Int) {
        val camera = MinecraftClient.getInstance().gameRenderer.camera
        val renderer = MinecraftClient.getInstance().blockRenderManager
        val world = MinecraftClient.getInstance().world ?: return
        val vertexConsumer = MinecraftClient.getInstance().bufferBuilders.entityVertexConsumers.getBuffer(
            RenderLayers.getBlockLayer(state)
        )

        val halfSide = sideLength / 2

        for (x in -halfSide..halfSide) {
            for (z in -halfSide..halfSide) {
                val blockPos = BlockPos(centerPos.x + x, height, centerPos.z + z)
                matrixStack.push()
                matrixStack.translate(
                    blockPos.x - camera.getPos().x,
                    blockPos.y - camera.getPos().y,
                    blockPos.z - camera.getPos().z
                )
                matrixStack.scale(500f, 1f, 500f)
                renderer.renderBlock(state, blockPos, world, matrixStack, vertexConsumer, true, Random.create())
                matrixStack.pop()
            }
        }
    }

    fun renderBlock(matrixStack: MatrixStack, pos: Vec3d, state: BlockState, blockPos: BlockPos) {
        val camera = MinecraftClient.getInstance().gameRenderer.camera
        val renderer = MinecraftClient.getInstance().blockRenderManager
        val world = MinecraftClient.getInstance().world ?: return
        val vertexConsumer = MinecraftClient.getInstance().bufferBuilders.entityVertexConsumers.getBuffer(
            RenderLayers.getBlockLayer(state)
        )

        matrixStack.push()
        matrixStack.translate(
            blockPos.x - camera.getPos().x,
            blockPos.y - camera.getPos().y,
            blockPos.z - camera.getPos().z
        )
        renderer.renderBlock(state, blockPos, world, matrixStack, vertexConsumer, true, Random.create())
        matrixStack.pop()
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

    fun handlePitch(quaternion: Quaternionf, tickDelta: Float): Quaternionf {
        return RotationAxis.POSITIVE_X.rotationDegrees(30f)
    }

    fun handleYaw(quaternion: Quaternionf, tickDelta: Float): Quaternionf {
        if (yawAnimation.isDone) {
            yawAnimation.reset()
        }
        return RotationAxis.POSITIVE_Y.rotationDegrees(yawAnimation.get())
    }

    override fun onEndTick(client: MinecraftClient) {
    }
}
