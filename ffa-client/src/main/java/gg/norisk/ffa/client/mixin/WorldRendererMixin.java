package gg.norisk.ffa.client.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.chunk.light.LightingProvider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow
    @Final
    private ObjectArrayList<ChunkBuilder.BuiltChunk> field_45616;

    @Shadow
    private @Nullable ClientWorld world;


    @Inject(method = "updateChunks", at = @At("HEAD"))
    private void injected(Camera camera, CallbackInfo ci) {
        LightingProvider lightingProvider = this.world.getLightingProvider();
        for (ChunkBuilder.BuiltChunk builtChunk : this.field_45616) {
            ChunkSectionPos chunkSectionPos = ChunkSectionPos.from(builtChunk.getOrigin());
           // System.out.println(chunkSectionPos + " " + builtChunk.needsRebuild() + " " + lightingProvider.isLightingEnabled(chunkSectionPos));
        }
    }
}
