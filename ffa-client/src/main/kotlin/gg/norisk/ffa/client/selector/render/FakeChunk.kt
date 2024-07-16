package gg.norisk.ffa.client.selector.render

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.chunk.WorldChunk

class FakeChunk(world: World, chunkPos: ChunkPos) : WorldChunk(world, chunkPos) {
    override fun getBlockState(blockPos: BlockPos): BlockState {
        if (blockPos.y == 64) {
            return Blocks.DIAMOND_BLOCK.defaultState
        }
        return super.getBlockState(blockPos)
    }
}
