package gg.norisk.ffa.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import gg.norisk.ffa.client.selector.ui.HeroSelectorScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @ModifyExpressionValue(
            method = "render",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/option/GameOptions;hudHidden:Z")
    )
    private boolean smoothCamera(boolean original) {
        if (MinecraftClient.getInstance().currentScreen instanceof HeroSelectorScreen) {
            return true;
        } else {
            return original;
        }
    }
}
