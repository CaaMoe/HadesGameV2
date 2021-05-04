package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.HadesGame;
import net.minecraft.entity.effect.StatusEffectInstance;

public class RandomEffectEvent extends AbstractEvent {
    public RandomEffectEvent() {
        super("randomEffect", "随机效果", true, 60, 120);
    }

    @Override
    public String getFormatEventName(int countdown) {
        if (countdown >= 10)
            return "\u00a7k********";
        return super.EVENT_NAME;
    }

    @Override
    public void callEvent() {
        GameCore.INSTANCE.survivalPlayerHandler(player -> player.addStatusEffect(new StatusEffectInstance(HadesGame.STATUS_EFFECT_WEIGHT_RANDOM_ARRAY_LIST.randomGet(), 20 * 60 * 2, 1)));
    }
}