package gg.norisk.ffa.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import gg.norisk.ffa.client.selector.OrthoCamera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;


/*
    credits to https://github.com/DimasKama/OrthoCamera/blob/1.21/src/main/java/com/dimaskama/orthocamera/mixin/GameRendererMixin.java
 */
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    public abstract void tick();

    // TODO keine ahnung es will nxi so wie ich will T_T
    @ModifyArg(
            method = "renderWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;setupFrustum(Lnet/minecraft/util/math/Vec3d;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V"
            ),
            index = 2
    )
    private Matrix4f ffa$orthoFrustumProjMat(Matrix4f projMat) {
        if (OrthoCamera.INSTANCE.isEnabled()) {
            return OrthoCamera.INSTANCE.createOrthoMatrix(1.0F, 20.0F);
        }
        return projMat;
    }

    @ModifyArg(
            method = "renderWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/render/RenderTickCounter;ZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V"

            ),
            index = 6
    )
    private Matrix4f ffa$orthoProjMat(Matrix4f projMat, @Local(argsOnly = true) RenderTickCounter tickCounter) {
        if (OrthoCamera.INSTANCE.isEnabled()) {
            float tickDelta = tickCounter.getTickDelta(true);
            Matrix4f mat = OrthoCamera.INSTANCE.createOrthoMatrix(tickDelta, 0.0F);
            RenderSystem.setProjectionMatrix(mat, VertexSorter.BY_Z);
            return mat;
        }
        return projMat;
    }

    @ModifyExpressionValue(
            method = "renderWorld",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Quaternionf;conjugate(Lorg/joml/Quaternionf;)Lorg/joml/Quaternionf;"
            )
    )
    private Quaternionf ffa$modifyRotation(Quaternionf original, @Local(argsOnly = true) RenderTickCounter tickCounter) {
        if (!OrthoCamera.INSTANCE.isEnabled()) {
            return original;
        }
        return original.rotationXYZ(
                OrthoCamera.INSTANCE.handlePitch(original, tickCounter.getTickDelta(false)),
                OrthoCamera.INSTANCE.handleYaw(original, tickCounter.getTickDelta(false)),
                0.0F
        );
    }
}
