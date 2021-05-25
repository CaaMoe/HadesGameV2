package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class TreatmentEvent extends ImplicitAbstractEvent {
    public TreatmentEvent() {
        super("treatment", "治疗", true, 60, 120);
    }

    @Override
    public void callEvent() {
        GameCore.INSTANCE.survivalPlayerHandler(player -> {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 100));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 100));
        });
    }
}
