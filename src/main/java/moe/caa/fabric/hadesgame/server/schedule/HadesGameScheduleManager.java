package moe.caa.fabric.hadesgame.server.schedule;

import moe.caa.fabric.hadesgame.server.GameCore;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.*;

public class HadesGameScheduleManager {
    public static final HadesGameScheduleManager INSTANCE = new HadesGameScheduleManager();
    public final LinkedList<AbstractTick> timer = new LinkedList<>();
    public final LinkedList<AbstractTick> runTask = new LinkedList<>();
    public final Map<AbstractTick, Integer> delayTimer = Collections.synchronizedMap(new HashMap<>());
    public final Map<AbstractTick, Integer> delayRunTask = Collections.synchronizedMap(new HashMap<>());

    private HadesGameScheduleManager() {
    }

    public void tick() {

        Iterator<Map.Entry<AbstractTick, Integer>> timerItr = delayTimer.entrySet().iterator();
        while (timerItr.hasNext()) {
            Map.Entry<AbstractTick, Integer> next = timerItr.next();
            if (next.getValue() <= 0) {
                timer.offer(next.getKey());
                timerItr.remove();
            }
            next.setValue(next.getValue() - 1);
        }

        Iterator<Map.Entry<AbstractTick, Integer>> delayRunTaskItr = delayRunTask.entrySet().iterator();
        while (delayRunTaskItr.hasNext()) {
            Map.Entry<AbstractTick, Integer> next = delayRunTaskItr.next();
            if (next.getValue() <= 0) {
                if(!next.getKey().isCancel()){
                    GameCore.INSTANCE.runPrintException(next.getKey(), AbstractTick::tick0);
                }
                delayRunTaskItr.remove();
            }
            next.setValue(next.getValue() - 1);
        }

        AbstractTick tickTask;
        while ((tickTask = runTask.poll()) != null) {
            if(!tickTask.isCancel()){
                GameCore.INSTANCE.runPrintException(tickTask, AbstractTick::tick0);
            }
        }

        timer.removeIf(AbstractTick::isCancel);

        for (AbstractTick tick : timer) {
            GameCore.INSTANCE.runPrintException(tick, AbstractTick::tick0);
        }
    }

    public void init() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            tick();
        });
    }
}
