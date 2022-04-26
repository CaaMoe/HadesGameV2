package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.Contains;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;

public class SuperMinersEvent extends ImplicitAbstractEvent {

    public SuperMinersEvent() {
        super("superMiners", "超级矿工", true, 30, 100);
    }

    @Override
    public void callEvent() {
        Contains.superMiners = true;
        HadesGameScheduleManager.runTaskLater(() -> Contains.superMiners = false, 20 * 90);
    }

    @Override
    public void gameEnd() {
        Contains.superMiners = false;
    }
}
