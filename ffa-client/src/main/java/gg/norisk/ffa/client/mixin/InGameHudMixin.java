package gg.norisk.ffa.client.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    //we are in spectator mode so its fine, but this still exists if we need it in future
   /* @ModifyExpressionValue(
            method = "render",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;hudHidden:Z")
    )
    private boolean smoothCamera(boolean original) {
        if (MinecraftClient.getInstance().currentScreen instanceof HeroSelectorScreen) {
            return true;
        } else {
            return original;
        }
    }*/
}
