package gg.norisk.ffa.client.mixin;

import gg.norisk.ffa.client.selector.ui.HeroSelectorScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {
    @Inject(method = "getPerspective", at = @At("RETURN"), cancellable = true)
    private void injected(CallbackInfoReturnable<Perspective> cir) {
        if (MinecraftClient.getInstance().currentScreen instanceof HeroSelectorScreen) {
            cir.setReturnValue(Perspective.THIRD_PERSON_FRONT);
        }
    }
}
