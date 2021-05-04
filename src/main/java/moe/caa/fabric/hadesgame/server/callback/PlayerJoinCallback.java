package moe.caa.fabric.hadesgame.server.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public interface PlayerJoinCallback {
    Event<PlayerJoinCallback> EVENT = EventFactory.createArrayBacked(PlayerJoinCallback.class, (callbacks) -> (playerEntity) -> {
        for (PlayerJoinCallback callback : callbacks) {
            callback.register(playerEntity);
        }
    });

    void register(ServerPlayerEntity playerEntity);
}
