package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.schedule.AbstractTick;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;

import java.util.ArrayList;
import java.util.List;

public class FreezeEffectEvent extends ImplicitAbstractEvent {
    private final List<AbstractTick> eventTick = new ArrayList<>();

    public FreezeEffectEvent() {
        super("freeze", "凛冬将至",true, 30, 100);
    }


    @Override
    public void callEvent() {
        AbstractTick tick = new AbstractTick(100) {
            private int count = 0;

            @Override
            protected void tick() {
                count++;
                GameCore.INSTANCE.survivalPlayerHandler(player -> player.setFrozenTicks(Integer.MAX_VALUE));
                if (count > 3) {
                    cancel();
                    GameCore.INSTANCE.allPlayerHandler(player -> player.setFrozenTicks(player.getMinFreezeDamageTicks()));
                }
            }
        };

        HadesGameScheduleManager.runTaskTimer(tick);
        eventTick.add(tick);
    }

    @Override
    public void gameEnd() {
        eventTick.forEach(AbstractTick::cancel);
        eventTick.clear();
    }
}
