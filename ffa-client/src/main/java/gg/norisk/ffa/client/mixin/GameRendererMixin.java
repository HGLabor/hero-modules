package gg.norisk.ffa.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import gg.norisk.ffa.client.selector.OrthoCamera;
import net.minecraft.client.render.GameRenderer;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @ModifyArg(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;setupFrustum(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/math/Vec3d;Lorg/joml/Matrix4f;)V"), index = 2)
    private Matrix4f orthoFrustumProjMat(Matrix4f projMat) {
        if (OrthoCamera.INSTANCE.isEnabled()) {
            return OrthoCamera.INSTANCE.createOrthoMatrix(1.0F, 20.0F);
        }
        return projMat;
    }

    @ModifyArg(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lorg/joml/Matrix4f;)V"

    ), index = 7)
    private Matrix4f orthoProjMat(Matrix4f projMat, @Local(argsOnly = true) float tickDelta) {
        if (OrthoCamera.INSTANCE.isEnabled()) {
            Matrix4f mat = OrthoCamera.INSTANCE.createOrthoMatrix(tickDelta, 0.0F);
            RenderSystem.setProjectionMatrix(mat, VertexSorter.BY_Z);
            return mat;
        }
        return projMat;
    }

    @ModifyArg(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V", ordinal = 2), index = 0)
    private Quaternionf modifyPitch(Quaternionf quaternion, @Local(argsOnly = true) float tickDelta) {
        if (OrthoCamera.INSTANCE.isEnabled()) {
            return OrthoCamera.INSTANCE.handlePitch(quaternion, tickDelta);
        }
        return quaternion;
    }

    @ModifyArg(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V", ordinal = 3), index = 0)
    private Quaternionf modifyYaw(Quaternionf quaternion, @Local(argsOnly = true) float tickDelta) {
        if (OrthoCamera.INSTANCE.isEnabled()) {
            return OrthoCamera.INSTANCE.handleYaw(quaternion, tickDelta);
        }
        return quaternion;
    }
}
