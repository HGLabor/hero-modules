package gg.norisk.ffa.server.mixin;

import kotlin.random.Random;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SwordItem.class)
public abstract class SwordItemMixin {
    @Inject(method = "postDamageEntity", at = @At("HEAD"), cancellable = true)
    public void breakReduction(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfo ci) {
        if (Random.Default.nextBoolean()) {
            ci.cancel();
        }
    }
}