package gg.norisk.ffa.server.world

import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.fabric.FabricAdapter
import com.sk89q.worldedit.function.operation.Operation
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.session.ClipboardHolder
import gg.norisk.ffa.server.FFAServer.logger
import kotlinx.coroutines.Job
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.network.packet.s2c.play.PositionFlag
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.network.SpawnLocating
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.silkmc.silk.commands.command
import net.silkmc.silk.core.Silk.server
import net.silkmc.silk.core.kotlin.ticks
import net.silkmc.silk.core.server.players
import net.silkmc.silk.core.task.infiniteMcCoroutineTask
import net.silkmc.silk.core.text.literal
import java.io.File
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.random.Random

object WorldManager {
    val chunkSize = 1
    val mapSize = 512
    var currentPair = Pair(0, 0)
    var mapReset = 10L
    var mapResetTask: Job? = null
    val usedMaps = mutableSetOf<Pair<Int, Int>>()

    fun mapResetCycle() {
        currentPair = getFreeMapPos()
        mapResetTask?.cancel()
        val counter = AtomicLong(mapReset)
        mapResetTask = infiniteMcCoroutineTask(period = 20.ticks) {
            val players = server?.players ?: emptyList()
            if (players.isEmpty()) {
                return@infiniteMcCoroutineTask
            }
            for (player in players) {
                player.sendMessage("MAP CHANGE IN ${counter.getAndDecrement()} $currentPair".literal, true)
            }
            if (counter.get() == 0L) {
                usedMaps.add(currentPair)
                mapResetCycle()
                server?.players?.forEach { player ->
                    player.teleportToNewMap(currentPair.first, currentPair.second)
                }
                setWorldBorder(server!!.overworld)
            }
        }
    }

    fun setWorldBorder(world: World) {
        world.worldBorder.setCenter(
            (currentPair.first * mapSize).toDouble() + mapSize / 2.0,
            (currentPair.second * mapSize).toDouble() + mapSize / 2.0
        )
        world.worldBorder.size = mapSize.toDouble()
    }

    fun getFreeMapPos(): Pair<Int, Int> {
        val pair = Pair(
            Random.nextInt(-chunkSize, chunkSize + 1),
            Random.nextInt(-chunkSize, chunkSize + 1)
        )
        return if (usedMaps.contains(pair)) {
            getFreeMapPos()
        } else {
            pair
        }
    }

    fun initServer() {
        ServerLifecycleEvents.SERVER_STARTED.register { server ->
            usedMaps.clear()
            mapResetCycle()
            setWorldBorder(server.overworld)
        }
        if (FabricLoader.getInstance().isDevelopmentEnvironment) {
            command("chunktest") {
                literal("generate") {
                    runs {
                        generateMap(this.source.world)
                    }
                }
                literal("teleport") {
                    argument<Int>("x") { x ->
                        argument<Int>("z") { z ->
                            runs {
                                this.source.playerOrThrow.teleportToNewMap(x(), z())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun generateMap(world: World) {
        val size = chunkSize * mapSize
        val file = File("config/worldedit/schematics/ffa_v3_512x.schem")
        if (!file.exists()) {
            logger.error("${file.name} doesn't exist")
            return
        }
        var counter = 0
        for (chunkX in -size..size step mapSize) {
            for (chunkZ in -size..size step mapSize) {
                val posX = chunkX + size
                val posZ = chunkZ + size
                counter++
                logger.info(
                    "Placing [$counter/${
                        (-chunkSize..chunkSize).count().toDouble().pow(2.0).toInt()
                    }] at ${posX} ${posZ}"
                )
                placeSchematic(
                    world,
                    file,
                    posX,
                    posZ,
                    80
                )
            }
        }
    }

    fun ServerPlayerEntity.teleportToNewMap(x: Int, z: Int, mapSize: Int = WorldManager.mapSize) {
        val world = this.serverWorld

        val relativeX = (this.blockPos.x and (mapSize - 1)) + this.x.fractional().absoluteValue
        val relativeZ = (this.blockPos.z and (mapSize - 1)) + this.z.fractional().absoluteValue

        //TODO bug dass es dings ist hä also z und x wird manchmal ganz seltsam

        val realCoordinateX = mapSize * x + relativeX
        val realCoordinateZ = mapSize * z + relativeZ
        this.teleport(
            world,
            realCoordinateX,
            this.y,
            realCoordinateZ,
            PositionFlag.VALUES,
            this.yaw,
            this.pitch
        )
    }

    fun Double.fractional(): Double {
        return this - this.toInt()
    }

    private fun placeSchematic(world: World, file: File, offsetX: Int, offsetZ: Int, y: Int) {
        val clipboard = ClipboardFormats.findByFile(file)?.getReader(file.inputStream())?.read()
        WorldEdit.getInstance().editSessionFactory.getEditSession(FabricAdapter.adapt(world), -1)
            .use { editSession ->
                val operation: Operation = ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(offsetX, y, offsetZ))
                    .ignoreAirBlocks(true)
                    .build()
                Operations.complete(operation)
                logger.info("Placed at $offsetX $offsetZ")
            }
    }

    fun ServerWorld.getCenter(): BlockPos {
        val x = (currentPair.first * mapSize).toDouble() + mapSize / 2.0
        val z = (currentPair.second * mapSize).toDouble() + mapSize / 2.0
        return BlockPos(x.toInt(), 64, z.toInt())
    }

    fun ServerWorld.findSpawnLocation(): BlockPos {
        val xRange = (currentPair.first * mapSize..currentPair.first * mapSize + mapSize)
        val zRange = (currentPair.second * mapSize..currentPair.second * mapSize + mapSize)
        return SpawnLocating.findOverworldSpawn(this, xRange.random(), zRange.random()) ?: this.findSpawnLocation()
    }
}
