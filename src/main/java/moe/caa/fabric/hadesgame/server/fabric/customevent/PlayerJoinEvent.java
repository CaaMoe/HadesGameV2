package moe.caa.fabric.hadesgame.server.fabric.customevent;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class PlayerJoinEvent {
    public static Event<PlayerJoinCallback> INSTANCE = EventFactory.createArrayBacked(PlayerJoinCallback.class, (callbacks) -> (playerEntity) -> {
        for (PlayerJoinCallback callback : callbacks) {
            callback.callback(playerEntity);
        }
    });

    @FunctionalInterface
    public interface PlayerJoinCallback {
        void callback(ServerPlayerEntity playerEntity);
    }
}
