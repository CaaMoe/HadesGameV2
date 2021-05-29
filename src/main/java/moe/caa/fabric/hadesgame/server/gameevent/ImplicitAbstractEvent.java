package moe.caa.fabric.hadesgame.server.gameevent;


/**
 * 继承该类的事件直到快要发生时才会在计分板显示自己的名称
 *
 * @see moe.caa.fabric.hadesgame.server.gameevent.AbstractEvent
 */
public abstract class ImplicitAbstractEvent extends AbstractEvent {
    public ImplicitAbstractEvent(String id, String event_name, boolean should_countdown, int countdown_random_min, int countdown_random_max) {
        super(id, event_name, should_countdown, countdown_random_min, countdown_random_max);
    }

    @Override
    public String getFormatEventName(int countdown) {
        return getFormatEventName(countdown, 10);
    }

    public String getFormatEventName(int countdown, int limit) {
        if (countdown >= limit)
            return "\u00a7k********";
        return EVENT_NAME;
    }

    @Override
    public double getFakeEventProb() {
        return 0.1;
    }
}
