package gg.norisk.ffa.client.mixin;

import gg.norisk.ffa.client.selector.OrthoCamera;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//CREDITS TO https://github.com/DimasKama/OrthoCamera/blob/master/src/main/java/com/dimaskama/orthocamera/mixin/CameraMixin.java
@Mixin(Camera.class)
public abstract class CameraMixin {

    @ModifyVariable(method = "moveBy", at = @At("HEAD"), index = 1, argsOnly = true)
    private float ffa$moveByHeadX(float value) {
        return OrthoCamera.INSTANCE.isEnabled() ? 0.0f : value;
    }

    @ModifyVariable(method = "moveBy", at = @At("HEAD"), index = 3, argsOnly = true)
    private float ffa$moveByHeadZ(float value) {
        return OrthoCamera.INSTANCE.isEnabled() ? 0.0f : value;
    }
}
