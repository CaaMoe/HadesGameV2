package moe.caa.fabric.hadesgame.server.gameevent.coreevent;

import moe.caa.fabric.hadesgame.server.gameevent.AbstractEvent;

public abstract class CoreAbstractEvent extends AbstractEvent {
    public CoreAbstractEvent(String id, String event_name, boolean should_countdown, int countdown_random_min, int countdown_random_max) {
        super(id, event_name, should_countdown, countdown_random_min, countdown_random_max);
    }

    @Override
    public String getFormatEventName(int countdown) {
        return EVENT_NAME;
    }

    @Override
    public double getFakeEventProb() {
        return 0.0;
    }
}
