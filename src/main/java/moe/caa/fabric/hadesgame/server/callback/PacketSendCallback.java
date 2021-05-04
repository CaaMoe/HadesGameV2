package moe.caa.fabric.hadesgame.server.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public interface PacketSendCallback {
    Event<PacketSendCallback> EVENT = EventFactory.createArrayBacked(PacketSendCallback.class, (callbacks) -> (playerEntity, packet) -> {
        for (PacketSendCallback callback : callbacks) {
            ActionResult result = callback.register(playerEntity, packet);
            if (result != ActionResult.PASS) {
                return result;
            }
        }
        return ActionResult.PASS;
    });

    ActionResult register(ServerPlayerEntity playerEntity, Packet<?> packet);
}
