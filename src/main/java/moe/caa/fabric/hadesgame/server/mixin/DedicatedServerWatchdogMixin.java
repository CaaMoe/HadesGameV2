package moe.caa.fabric.hadesgame.server.mixin;

import net.minecraft.server.dedicated.DedicatedServerWatchdog;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DedicatedServerWatchdog.class)
public abstract class DedicatedServerWatchdogMixin {

    @Redirect(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/MinecraftDedicatedServer;isRunning()Z"))
    private boolean onSd(MinecraftDedicatedServer instance) {
        System.err.println("看门狗线程已关闭");
        return false;
    }
}
