package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.Contains;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;

public class ClimbEvent extends ImplicitAbstractEvent {
    public ClimbEvent() {
        super("climb", "爬", true, 30, 100);
    }

    @Override
    public void callEvent() {
        Contains.climb = true;
        HadesGameScheduleManager.runTaskLater(() -> Contains.climb = false, 20 * 90);
    }

    @Override
    public void gameEnd() {
        Contains.climb = false;
    }
}
