package moe.caa.fabric.hadesgame.server.fabric.customevent;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.ActionResult;

public abstract class PacketSendEvent {
    public static Event<PacketSendCallback> INSTANCE = EventFactory.createArrayBacked(PacketSendCallback.class, (callbacks) -> (playerEntity, packet) -> {
        for (PacketSendCallback callback : callbacks) {
            ActionResult result = callback.callback(playerEntity, packet);
            if (result != ActionResult.PASS) {
                return result;
            }
        }
        return ActionResult.PASS;
    });

    @FunctionalInterface
    public interface PacketSendCallback {
        ActionResult callback(ClientConnection playerEntity, Packet<?> packet);
    }
}

