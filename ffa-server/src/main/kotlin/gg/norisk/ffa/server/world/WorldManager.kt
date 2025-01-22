package gg.norisk.ffa.server.world

import gg.norisk.ffa.network.SelectorPackets.isFFA
import gg.norisk.ffa.server.FFAServer.logger
import gg.norisk.ffa.server.world.MapPlacer.chunkSize
import gg.norisk.ffa.server.world.MapPlacer.mapSize
import kotlinx.coroutines.Job
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.packet.s2c.play.PositionFlag
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.network.SpawnLocating
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.silkmc.silk.core.event.Events
import net.silkmc.silk.core.event.Server
import net.silkmc.silk.core.kotlin.ticks
import net.silkmc.silk.core.task.infiniteMcCoroutineTask
import net.silkmc.silk.core.text.literal
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.absoluteValue
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

object WorldManager {
    var currentPair = Pair(0, 0)
    var mapReset = 30 * 60L
    var mapResetTask: Job? = null
    val usedMaps = mutableSetOf<Pair<Int, Int>>()
    val maxCount get() = (-chunkSize..chunkSize).count()
    val counter = AtomicLong(mapReset)

    fun mapResetCycle(server: MinecraftServer) {
        currentPair = getFreeMapPos()
        mapResetTask?.cancel()
        counter.set(mapReset)
        mapResetTask = infiniteMcCoroutineTask(period = 20.ticks, sync = true, client = false) {
            val players = server.playerManager.playerList
            if (players.isEmpty()) {
                return@infiniteMcCoroutineTask
            }
            //logger.info("Map Reset In: ${counter.get()}")
            counter.decrementAndGet()
            //if (counter.get() < 300) {
            for (player in players) {
                if (player.isFFA) {
                    player.sendMessage("Map Reset ${counter.getTimeAsString()}".literal, true)
                }
            }
            //}
            if (counter.get() == 0L) {
                usedMaps.add(currentPair)
                mapResetCycle(server)
                server.overworld.players.forEach { player ->
                    player.teleportToNewMap(currentPair.first, currentPair.second)
                }
                setWorldBorder(server.overworld)
            }
        }
    }

    fun PlayerEntity.isInKitEditorWorld(): Boolean {
        return this.world.registryKey.value == Identifier.of("hero-api", "kit-editor")
    }

    fun AtomicLong.getTimeAsString(): String {
        val builder = StringBuilder()
        get().seconds.toComponents { days, hours, minutes, seconds, _ ->
            if (days > 0) builder.append(days).append("d ")
            if (hours > 0) builder.append(hours).append("h ")
            if (minutes > 0) builder.append(minutes).append("m ")
            builder.append(seconds).append("s")
        }
        return builder.toString()
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

        //TODO bypass für volle map sollte aber eig nicht passieren da große map
        if (usedMaps.size == maxCount) {
            return pair
        }

        return if (usedMaps.contains(pair)) {
            getFreeMapPos()
        } else {
            pair
        }
    }

    fun initServer() {
        Events.Server.postStart.listen { event ->
            //MapPlacer.generateMap(Silk.serverOrThrow.overworld)
        }
        ServerLifecycleEvents.SERVER_STARTED.register { server ->
            logger.info("Init Map Reset Cycle...")
            usedMaps.clear()
            mapResetCycle(server)
            setWorldBorder(server.overworld)
        }
    }

    fun ServerPlayerEntity.teleportToNewMap(x: Int, z: Int, mapSize: Int = MapPlacer.mapSize) {
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
