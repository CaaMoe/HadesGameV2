package moe.caa.fabric.hadesgame.server.gameevent.coreevent;

public class GameWaitingEvent extends CoreAbstractEvent {
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
}
