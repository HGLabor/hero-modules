package gg.norisk.ffa.server.mixin;

import net.minecraft.entity.player.HungerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
    @ModifyConstant(method = "update", constant = @Constant(floatValue = 1f, ordinal = 0))
    private float injected(float constant) {
        return 0.25f;
    }
}
