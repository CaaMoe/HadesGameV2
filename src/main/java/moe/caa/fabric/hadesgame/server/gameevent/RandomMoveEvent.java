package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.schedule.AbstractTick;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class RandomMoveEvent extends ImplicitAbstractEvent{
    private final List<AbstractTick> eventTick = new ArrayList<>();

    public RandomMoveEvent() {
        super("randomMove", "昏乱", true, 60, 120);
    }

    @Override
    public void callEvent() {
        AbstractTick tick = new AbstractTick(10) {
            private int count = 0;

            @Override
            protected void tick() {
                count++;
                GameCore.INSTANCE.survivalPlayerHandler(player -> generateMove(player));
                if (count > 90) cancel();
            }
        };
        HadesGameScheduleManager.runTaskTimer(tick);
        eventTick.add(tick);
    }

    private void generateMove(ServerPlayerEntity player) {
        int i = 3;
        player.addVelocity((-1 + Math.random() * 2) / i, (-1 + Math.random() * 2) / i, (-1 + Math.random() * 2) / i);
        player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
    }

    @Override
    public void gameEnd() {
        eventTick.forEach(AbstractTick::cancel);
        eventTick.clear();
    }
}
