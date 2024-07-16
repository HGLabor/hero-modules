package gg.norisk.ffa.client.mixin;

import gg.norisk.ffa.client.selector.render.FakeChunk;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientChunkManager.class)
public abstract class ClientChunkManagerMixin {
    @Shadow
    @Final
    private WorldChunk emptyChunk;

    @Shadow
    @Final
    ClientWorld world;

    @Inject(method = "getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/WorldChunk;", at = @At("RETURN"), cancellable = true)
    private void bobbyGetChunk(int x, int z, ChunkStatus chunkStatus, boolean orEmpty, CallbackInfoReturnable<WorldChunk> ci) {
        // Did we find a live chunk?
        //var chunk = new FakeChunk(world, new ChunkPos(x, z));
       // ci.setReturnValue(chunk);
        if (ci.getReturnValue() != (orEmpty ? emptyChunk : null)) {
            return;
        }
        //load(x, z, chunk);
        //load(x, z, chunk);



        /*if (bobbyChunkManager == null) {
            return;
        }

        // Otherwise, see if we've got one
        WorldChunk chunk = bobbyChunkManager.getChunk(x, z);
        if (chunk != null) {
            ci.setReturnValue(chunk);
        }*/
    }

    @Unique
    public void load(int x, int z, WorldChunk chunk) {
        world.resetChunkColor(new ChunkPos(x, z));
        for (int i = world.getBottomSectionCoord(); i < world.getTopSectionCoord(); i++) {
            world.scheduleBlockRenders(x, i, z);
        }
    }
}

