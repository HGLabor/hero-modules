package gg.norisk.ffa.world

import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.fabric.FabricAdapter
import com.sk89q.worldedit.function.operation.Operation
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.session.ClipboardHolder
import gg.norisk.ffa.FFACommon.logger
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.world.World
import net.silkmc.silk.commands.command
import java.io.File
import kotlin.math.pow

object MapPlacer {
    val chunkSize = 1
    val mapSize = 512

    fun init() {
        if (FabricLoader.getInstance().isDevelopmentEnvironment) {
            command("placeffamap") {
                runs {
                    generateMap(this.source.world)
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
}
