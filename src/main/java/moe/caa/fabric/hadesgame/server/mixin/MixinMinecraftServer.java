package moe.caa.fabric.hadesgame.server.mixin;

import moe.caa.fabric.hadesgame.server.Contains;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {

    @Inject(method = "shouldKeepTicking", at = @At("HEAD"), cancellable = true)
    private void onShouldKeepTicking(CallbackInfoReturnable<Boolean> cir) {
        if (Contains.tickSpeedUp) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
