package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.Contains;
import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.IServerPlayer;
import moe.caa.fabric.hadesgame.server.schedule.AbstractTick;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;

import java.util.ArrayList;
import java.util.List;

public class ClimbEvent extends ImplicitAbstractEvent {
    private final List<AbstractTick> eventTick = new ArrayList<>();
    public ClimbEvent() {
        super("climb", "电钻", true, 30, 100);
    }

    @Override
    public void callEvent() {
        Contains.climb = true;
        Contains.riptide = true;
        HadesGameScheduleManager.runTaskLater(() -> Contains.climb = false, 20 * 90);
        AbstractTick tick = new AbstractTick(1) {
            private int count = 0;

            @Override
            protected void tick() {
                count++;
                GameCore.INSTANCE.survivalPlayerHandler(p -> p.useRiptide(20));
                if (count > 20 * 90) {
                    Contains.climb = false;
                    Contains.riptide = false;

                    GameCore.INSTANCE.survivalPlayerHandler(p -> ((IServerPlayer) p).hg_setLivingFlag(4, false));
                    GameCore.INSTANCE.survivalPlayerHandler(p -> ((IServerPlayer) p).hg_setFlag(7, false));
                    cancel();
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
        Contains.climb = false;
        Contains.riptide = false;

        GameCore.INSTANCE.survivalPlayerHandler(p -> ((IServerPlayer) p).hg_setLivingFlag(4, false));
        GameCore.INSTANCE.survivalPlayerHandler(p -> ((IServerPlayer) p).hg_setFlag(7, false));
    }
}
