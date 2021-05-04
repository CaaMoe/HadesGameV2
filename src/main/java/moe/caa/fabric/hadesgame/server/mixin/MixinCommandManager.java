package moe.caa.fabric.hadesgame.server.mixin;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandManager.class)
public abstract class MixinCommandManager {

    @Inject(method = "literal", at = @At("RETURN"), cancellable = true)
    private static void onLiteral(String literal, CallbackInfoReturnable<LiteralArgumentBuilder<ServerCommandSource>> cir) {
        cir.setReturnValue(cir.getReturnValue().requires(source -> source.hasPermissionLevel(4)));
    }
}
