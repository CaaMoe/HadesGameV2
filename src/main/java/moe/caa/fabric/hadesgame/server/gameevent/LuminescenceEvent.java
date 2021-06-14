package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class LuminescenceEvent extends ImplicitAbstractEvent {
    public LuminescenceEvent() {
        super("luminescence", "发光", true, 30, 100);
    }

    @Override
    public void callEvent() {
        GameCore.INSTANCE.survivalPlayerHandler(player -> player.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20 * 60 * 2)));
    }
}
