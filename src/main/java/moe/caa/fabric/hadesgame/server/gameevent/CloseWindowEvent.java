package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.schedule.AbstractTick;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class CloseWindowEvent extends ImplicitAbstractEvent {
    private final List<AbstractTick> eventTick = new ArrayList<>();

    public CloseWindowEvent() {
        super("closeWindow", "行为管制", true, 30, 100);
    }

    @Override
    public void callEvent() {
        AbstractTick tick = new AbstractTick(1) {
            private int count = 0;

            @Override
            protected void tick() {
                count++;
                GameCore.INSTANCE.survivalPlayerHandler(player -> generate(player));
                if (count > 20 * 60) cancel();
            }
        };
        HadesGameScheduleManager.runTaskTimer(tick);
        eventTick.add(tick);
    }

    public void generate(ServerPlayerEntity entity) {
        entity.closeHandledScreen();
    }

    @Override
    public void gameEnd() {
        eventTick.forEach(AbstractTick::cancel);
        eventTick.clear();
    }
}
