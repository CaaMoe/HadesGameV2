package moe.caa.fabric.hadesgame.server.fabric.customevent;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

public abstract class LivingEntityChangeHealthEvent {
    public static Event<LivingEntityChangeHealthCallback> INSTANCE = EventFactory.createArrayBacked(LivingEntityChangeHealthCallback.class, (callbacks) -> (livingEntity, newHealth) -> {
        for (LivingEntityChangeHealthCallback callback : callbacks) {
            callback.callback(livingEntity, newHealth);
        }
    });

    @FunctionalInterface
    public interface LivingEntityChangeHealthCallback {
        void callback(LivingEntity livingEntity, float newHealth);
    }
}

