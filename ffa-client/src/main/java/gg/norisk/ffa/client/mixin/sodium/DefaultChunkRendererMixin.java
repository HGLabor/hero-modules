package gg.norisk.ffa.client.mixin.sodium;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import gg.norisk.ffa.client.selector.ui.HeroSelectorScreen;
import me.jellysquid.mods.sodium.client.gl.device.RenderDevice;
import me.jellysquid.mods.sodium.client.render.chunk.DefaultChunkRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.ShaderChunkRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexType;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DefaultChunkRenderer.class)
public abstract class DefaultChunkRendererMixin extends ShaderChunkRenderer {
    public DefaultChunkRendererMixin(RenderDevice device, ChunkVertexType vertexType) {
        super(device, vertexType);
    }

    @ModifyExpressionValue(
            remap = false,
            method = "render",
            at = @At(value = "FIELD", target = "Lme/jellysquid/mods/sodium/client/gui/SodiumGameOptions$PerformanceSettings;useBlockFaceCulling:Z")
    )
    private boolean blockFaceCulling(boolean original) {
        return original && !(MinecraftClient.getInstance().currentScreen instanceof HeroSelectorScreen);
    }
}
