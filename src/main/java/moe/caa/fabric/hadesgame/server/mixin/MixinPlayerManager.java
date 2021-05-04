package moe.caa.fabric.hadesgame.server.mixin;

import moe.caa.fabric.hadesgame.server.callback.PlayerJoinCallback;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class MixinPlayerManager {

    @Inject(method = "onPlayerConnect", at = @At("HEAD"))
    private void onConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        PlayerJoinCallback.EVENT.invoker().register(player);
    }
}
