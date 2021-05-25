package moe.caa.fabric.hadesgame.server.fabric.customevent;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class PlayerQuitEvent {
    public static Event<PlayerQuitCallback> INSTANCE = EventFactory.createArrayBacked(PlayerQuitCallback.class, (callbacks) -> (playerEntity) -> {
        for (PlayerQuitCallback callback : callbacks) {
            callback.callback(playerEntity);
        }
    });

    @FunctionalInterface
    public interface PlayerQuitCallback {
        void callback(ServerPlayerEntity playerEntity);
    }
}
