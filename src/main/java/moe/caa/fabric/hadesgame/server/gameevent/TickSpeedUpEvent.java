package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.Contains;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;

public class TickSpeedUpEvent extends ImplicitAbstractEvent {

    public TickSpeedUpEvent() {
        super("tickSpeedUp", "沧海桑田", true, 30, 100);
    }

    @Override
    public void callEvent() {
        Contains.tickSpeedUp = true;
        HadesGameScheduleManager.runTaskLater(() -> Contains.tickSpeedUp = false, 20 * 90);
    }

    @Override
    public void gameEnd() {
        Contains.tickSpeedUp = false;
    }
}
