package moe.caa.fabric.hadesgame.server.gameevent.coreevent;

import moe.caa.fabric.hadesgame.server.gameevent.AbstractEvent;

public class GameWaitingEvent extends AbstractEvent {
    public GameWaitingEvent() {
        super("waiting", "等待指令", false, 0, 0);
    }

    @Override
    public String getFormatCountDown(int countdown) {
        return "00:00";
    }

    @Override
    public void callEvent() {

    }

    @Override
    public double getFakeEventProb() {
        return 0;
    }
}
