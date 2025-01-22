package gg.norisk.ffa.server.mixin;

import gg.norisk.ffa.server.world.WorldManager;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.silkmc.silk.core.Silk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onPlayerAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/server/world/ServerWorld;)V", shift = At.Shift.AFTER), cancellable = true)
    private void cancelOnPlayerAction(PlayerActionC2SPacket playerActionC2SPacket, CallbackInfo ci) {
        if (!WorldManager.INSTANCE.isInKitEditorWorld(player)) {
            return;
        }
        switch (playerActionC2SPacket.getAction()) {
            case DROP_ITEM, DROP_ALL_ITEMS -> {
                updateInv();
                ci.cancel();
            }
        }
    }

    @Inject(method = "onClickSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/server/world/ServerWorld;)V", shift = At.Shift.AFTER), cancellable = true)
    private void cancelOnClickSlot(ClickSlotC2SPacket clickSlotC2SPacket, CallbackInfo ci) {
        if (!WorldManager.INSTANCE.isInKitEditorWorld(player)) {
            return;
        }
        if (clickSlotC2SPacket.getActionType() == SlotActionType.THROW) {
            updateInv();
            ci.cancel();
            return;
        }
        if (clickSlotC2SPacket.getSlot() == -999) {
            //player.sendMessage(Text.of("Action" + clickSlotC2SPacket.getActionType()));
            // player.sendMessage(Text.of("Button" + clickSlotC2SPacket.getButton()));
            // player.sendMessage(Text.of("Slot: " + clickSlotC2SPacket.getSlot()));
            if (clickSlotC2SPacket.getActionType() == SlotActionType.QUICK_CRAFT) {
                return;
            }
            updateInv();
            ci.cancel();
        }
    }

    @Unique
    private void updateInv() {
        player.getInventory().updateItems();
        Silk.INSTANCE.getServerOrThrow().getPlayerManager().sendPlayerStatus(player);
    }
}
