package gg.norisk.ffa.server.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CommandManager.class)
public abstract class CommandManagerMixin {
    @WrapWithCondition(
            method = "<init>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/KillCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V")
    )
    private boolean dontAllowKillCommand(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        return false;
    }
}
