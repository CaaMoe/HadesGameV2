package moe.caa.fabric.hadesgame.server.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public interface PlayerQuitCallback {
    Event<PlayerQuitCallback> EVENT = EventFactory.createArrayBacked(PlayerQuitCallback.class, (callbacks) -> (playerEntity) -> {
        for (PlayerQuitCallback callback : callbacks) {
            callback.register(playerEntity);
        }
    });

    void register(ServerPlayerEntity playerEntity);
}
