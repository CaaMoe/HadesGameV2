package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.HadesGame;
import moe.caa.fabric.hadesgame.server.schedule.AbstractTick;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class RainAnvilEvent extends AbstractEvent {
    public RainAnvilEvent() {
        super("rainAnvil", "铁砧雨", true, 60, 120);
    }

    @Override
    public String getFormatEventName(int countdown) {
        if (countdown >= 10)
            return "\u00a7k********";
        return super.EVENT_NAME;
    }

    @Override
    public void callEvent() {
        Random random = new Random();
        ServerWorld world = HadesGame.server.get().getOverworld();


        GameCore.INSTANCE.survivalPlayerHandler(player -> {
            double x = player.getPos().x - 2 + random.nextInt(4);
            double z = player.getPos().z - 2 + random.nextInt(4);

            world.setBlockState(new BlockPos(x, 300, z), Blocks.ANVIL.getDefaultState());
            world.updateNeighbors(new BlockPos(x, 300, z), Blocks.ANVIL);

            HadesGameScheduleManager.INSTANCE.delayRunTask.put(new AbstractTick() {
                @Override
                protected void tick() {
                    double x = player.getPos().x - 2 + random.nextInt(4);
                    double z = player.getPos().z - 2 + random.nextInt(4);

                    world.setBlockState(new BlockPos(x, 250, z), Blocks.ANVIL.getDefaultState());

                    world.updateNeighbors(new BlockPos(x, 250, z), Blocks.ANVIL);
                }
            }, 20);

            HadesGameScheduleManager.INSTANCE.delayRunTask.put(new AbstractTick() {
                @Override
                protected void tick() {
                    double x = player.getPos().x - 2 + random.nextInt(4);
                    double z = player.getPos().z - 2 + random.nextInt(4);

                    world.setBlockState(new BlockPos(x, 250, z), Blocks.ANVIL.getDefaultState());

                    world.updateNeighbors(new BlockPos(x, 250, z), Blocks.ANVIL);
                }
            }, 40);

            HadesGameScheduleManager.INSTANCE.delayRunTask.put(new AbstractTick() {
                @Override
                protected void tick() {
                    double x = player.getPos().x - 2 + random.nextInt(4);
                    double z = player.getPos().z - 2 + random.nextInt(4);

                    world.setBlockState(new BlockPos(x, 250, z), Blocks.ANVIL.getDefaultState());

                    world.updateNeighbors(new BlockPos(x, 250, z), Blocks.ANVIL);
                }
            }, 60);

            HadesGameScheduleManager.INSTANCE.delayRunTask.put(new AbstractTick() {
                @Override
                protected void tick() {
                    double x = player.getPos().x - 2 + random.nextInt(4);
                    double z = player.getPos().z - 2 + random.nextInt(4);

                    world.setBlockState(new BlockPos(x, 250, z), Blocks.ANVIL.getDefaultState());

                    world.updateNeighbors(new BlockPos(x, 250, z), Blocks.ANVIL);
                }
            }, 80);

            HadesGameScheduleManager.INSTANCE.delayRunTask.put(new AbstractTick() {
                @Override
                protected void tick() {
                    double x = player.getPos().x - 2 + random.nextInt(4);
                    double z = player.getPos().z - 2 + random.nextInt(4);

                    world.setBlockState(new BlockPos(x, 250, z), Blocks.ANVIL.getDefaultState());

                    world.updateNeighbors(new BlockPos(x, 250, z), Blocks.ANVIL);
                }
            }, 100);
        });
    }
}
