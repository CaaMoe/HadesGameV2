package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.schedule.AbstractTick;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;

import java.util.ArrayList;
import java.util.List;

public class RainAnvilEvent extends ImplicitAbstractEvent {
    private final List<AbstractTick> eventTick = new ArrayList<>();

    public RainAnvilEvent() {
        super("rainAnvil", "铁砧雨", true, 30, 100);
    }

    @Override
    public void callEvent() {
        AbstractTick tick = new AbstractTick(20) {
            private int count = 0;

            @Override
            protected void tick() {
                count++;
                GameCore.INSTANCE.survivalPlayerHandler(player -> generateAnvil(player));
                if (count > 30) cancel();
            }
        };

        HadesGameScheduleManager.runTaskTimer(tick);
        eventTick.add(tick);
    }

    public void generateAnvil(ServerPlayerEntity entity) {
        ServerWorld world = entity.getWorld();
        final int x = (int) entity.getX() + (int) (Math.random() * 10) - 5;
        final int z = (int) entity.getZ() + (int) (Math.random() * 10) - 5;

        int spawnY = (int) Math.max(entity.getY() + 10, world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z) + 10);
        if (spawnY > entity.getY() + 50) return;


        world.setBlockState(new BlockPos(x, spawnY, z), Blocks.ANVIL.getDefaultState());
        world.updateNeighbors(new BlockPos(x, spawnY, z), Blocks.ANVIL);
    }

    @Override
    public void gameEnd() {
        eventTick.forEach(AbstractTick::cancel);
        eventTick.clear();
    }
}
