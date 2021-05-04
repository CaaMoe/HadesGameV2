package moe.caa.fabric.hadesgame.server.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

public interface LivingEntityChangeHealthCallback {
    Event<LivingEntityChangeHealthCallback> EVENT = EventFactory.createArrayBacked(LivingEntityChangeHealthCallback.class, (callbacks) -> (livingEntity, newHealth) -> {
        for (LivingEntityChangeHealthCallback callback : callbacks) {
            callback.register(livingEntity, newHealth);
        }
    });

    void register(LivingEntity livingEntity, float newHealth);
}
