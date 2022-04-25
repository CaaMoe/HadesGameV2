package moe.caa.fabric.hadesgame.server.mixin;

import moe.caa.fabric.hadesgame.server.Contains;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {

    @Mutable
    @Shadow
    private long timeReference;

    @Shadow
    protected abstract void runTasksTillTickEnd();

    @Redirect(method = "runServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;runTasksTillTickEnd()V"))
    private void onSleep(MinecraftServer instance) {
        if (Contains.tickSpeedUp) {
            timeReference -= 50;
        }
        runTasksTillTickEnd();
    }
}
