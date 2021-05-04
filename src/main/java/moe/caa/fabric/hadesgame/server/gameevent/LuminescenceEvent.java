package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class LuminescenceEvent extends AbstractEvent {
    public LuminescenceEvent() {
        super("luminescence", "发光", true, 60, 120);
    }

    @Override
    public String getFormatEventName(int countdown) {
        if (countdown >= 10)
            return "\u00a7k********";
        return super.EVENT_NAME;
    }

    @Override
    public void callEvent() {
        GameCore.INSTANCE.survivalPlayerHandler(player -> player.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20 * 60 * 2)));
    }
}
