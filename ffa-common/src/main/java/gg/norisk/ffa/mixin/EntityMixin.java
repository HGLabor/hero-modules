package gg.norisk.ffa.mixin;

import gg.norisk.ffa.event.EntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.TrackedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "onTrackedDataSet", at = @At("TAIL"))
    private void injected(TrackedData<?> trackedData, CallbackInfo ci) {
        EntityEvents.INSTANCE.getOnTrackedDataSetEvent().invoke(new EntityEvents.EntityTrackedDataSetEvent((Entity) (Object) this, trackedData));
    }
}
