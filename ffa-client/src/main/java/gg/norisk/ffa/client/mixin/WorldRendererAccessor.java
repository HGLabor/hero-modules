package gg.norisk.ffa.client.mixin;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {
    @Invoker("scheduleSectionRender")
    void invokeScheduleSectionRender(BlockPos blockPos, boolean bl);
}
