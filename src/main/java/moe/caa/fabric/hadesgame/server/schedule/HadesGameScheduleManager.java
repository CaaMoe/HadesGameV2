package moe.caa.fabric.hadesgame.server.schedule;

import moe.caa.fabric.hadesgame.server.GameCore;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class HadesGameScheduleManager {
    public static final HadesGameScheduleManager INSTANCE = new HadesGameScheduleManager();
    public final List<AbstractTick> timer = Collections.synchronizedList(new LinkedList<>());
    public final LinkedList<AbstractTick> runTask = new LinkedList<>();
    public final Map<AbstractTick, Integer> delayTimer = Collections.synchronizedMap(new HashMap<>());
    public final Map<AbstractTick, Integer> delayRunTask = Collections.synchronizedMap(new HashMap<>());

    private HadesGameScheduleManager() {
    }

    public static AbstractTick runTask(Runnable runnable) {
        AbstractTick ret = new AbstractTick() {
            @Override
            protected void tick() {
                runnable.run();
            }
        };
        runTask(ret);
        return ret;
    }

    public static void runTaskLater(AbstractTick task, int later) {
        runTask(() -> INSTANCE.delayRunTask.put(task, later));
    }

    public static AbstractTick runTaskLater(Runnable runnable, int later) {
        AbstractTick ret = new AbstractTick() {
            @Override
            protected void tick() {
                runnable.run();
            }
        };
        runTaskLater(ret, later);
        return ret;
    }

    public static void runTask(AbstractTick task) {
        INSTANCE.runTask.add(task);
    }

    public static void runTaskTimer(AbstractTick task) {
        runTask(() -> INSTANCE.timer.add(task));
    }

    public static void runTaskTimerLater(AbstractTick task, int later) {
        INSTANCE.delayTimer.put(task, later);
    }

    public static AbstractTick runTaskTimer(Runnable runnable, int period) {
        AbstractTick ret = new AbstractTick(period) {
            @Override
            protected void tick() {
                runnable.run();
            }
        };
        runTaskTimer(ret);
        return ret;
    }

    public static AbstractTick runTaskTimerLater(Runnable runnable, int later, int period) {
        AbstractTick ret = new AbstractTick(period) {
            @Override
            protected void tick() {
                runnable.run();
            }
        };
        runTaskTimerLater(ret, later);
        return ret;
    }

    public void tick() {

        Iterator<Map.Entry<AbstractTick, Integer>> timerItr = delayTimer.entrySet().iterator();
        while (timerItr.hasNext()) {
            Map.Entry<AbstractTick, Integer> next = timerItr.next();
            if (next.getValue() <= 0) {
                timer.add(next.getKey());
                timerItr.remove();
            }
            next.setValue(next.getValue() - 1);
        }

        Iterator<Map.Entry<AbstractTick, Integer>> delayRunTaskItr = delayRunTask.entrySet().iterator();
        while (delayRunTaskItr.hasNext()) {
            Map.Entry<AbstractTick, Integer> next = delayRunTaskItr.next();
            if (next.getValue() <= 0) {
                if (!next.getKey().isCancel()) {
                    GameCore.INSTANCE.runPrintException(next.getKey(), AbstractTick::tick0);
                }
                delayRunTaskItr.remove();
            }
            next.setValue(next.getValue() - 1);
        }

        AbstractTick tickTask;
        while ((tickTask = runTask.poll()) != null) {
            if (!tickTask.isCancel()) {
                GameCore.INSTANCE.runPrintException(tickTask, AbstractTick::tick0);
            }
        }

        timer.removeIf(AbstractTick::isCancel);

        for (AbstractTick tick : timer) {
            if (!tick.isCancel()) {
                GameCore.INSTANCE.runPrintException(tick, AbstractTick::tick0);
            }
        }
    }

    public void init() {
        AtomicLong lastTick = new AtomicLong(System.currentTimeMillis());
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            try {
                final long currentTimeMillis = System.currentTimeMillis();
                if (lastTick.get() >= currentTimeMillis - 48) {
                    return;
                }
                lastTick.set(currentTimeMillis);
                tick();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }
}
