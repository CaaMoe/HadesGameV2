package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.schedule.AbstractTick;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TNTRunEvent extends ImplicitAbstractEvent{
    private final List<AbstractTick> eventTick = new ArrayList<>();

    public TNTRunEvent() {
        super("tntRun", "梯恩梯润", true, 30, 100);
    }

    @Override
    public void callEvent() {
        AbstractTick tick = new AbstractTick(5) {
            int count = 0;

            @Override
            protected void tick() {
                if (count++ >= 120) this.cancel();
                for (ServerPlayerEntity p : GameCore.INSTANCE.getSurvivalPlayers()) {
                    BlockPos location = p.getBlockPos().add(0, -1, 0);
                    World world = p.getWorld();
                    BlockState state = world.getBlockState(location);
                    if (state == null || state.getBlock() == Blocks.AIR) continue;
                    HadesGameScheduleManager.runTaskLater(() -> {
                        world.breakBlock(location, true);
                    }, 5);

                }
            }
        };
        HadesGameScheduleManager.runTaskTimer(tick);
        eventTick.add(tick);
    }

    @Override
    public void gameEnd() {
        eventTick.forEach(AbstractTick::cancel);
        eventTick.clear();
    }
}
