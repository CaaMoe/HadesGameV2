package moe.caa.fabric.hadesgame.server.mixin;

import moe.caa.fabric.hadesgame.server.fabric.customevent.PacketSendEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommonNetworkHandler.class)
public class MixinServerCommonNetworkHandler {

    @Shadow @Final protected ClientConnection connection;

    @Inject(method = "send", at = @At("HEAD"), cancellable = true)
    private void onSend(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci){

        ActionResult result = PacketSendEvent.INSTANCE.invoker().callback(connection, packet);
        if (result == ActionResult.FAIL) {
            ci.cancel();
        }
    }
}
