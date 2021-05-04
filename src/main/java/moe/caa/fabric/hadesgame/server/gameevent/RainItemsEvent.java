package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.HadesGame;
import moe.caa.fabric.hadesgame.server.schedule.AbstractTick;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class RainItemsEvent extends AbstractEvent {
    public RainItemsEvent() {
        super("rainItem", "物品雨", true, 60, 120);
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

        GameCore.INSTANCE.survivalPlayerHandler(p -> {
            double x = p.getX();
            double z = p.getZ();

            for (int i = 0; i < 10; i++) {
                double spawnX = x - 5 + random.nextInt(10);
                double spawnZ = z - 5 + random.nextInt(10);
                p.world.spawnEntity(new ItemEntity(p.world, spawnX, 250, spawnZ, new ItemStack(HadesGame.ITEM_WEIGHT_RANDOM_ARRAY_LIST.randomGet())));
            }

            HadesGameScheduleManager.INSTANCE.delayRunTask.put(new AbstractTick() {
                @Override
                protected void tick() {
                    double x = p.getX();
                    double z = p.getZ();

                    for (int i = 0; i < 10; i++) {
                        double spawnX = x - 5 + random.nextInt(10);
                        double spawnZ = z - 5 + random.nextInt(10);
                        p.world.spawnEntity(new ItemEntity(p.world, spawnX, 250, spawnZ, new ItemStack(HadesGame.ITEM_WEIGHT_RANDOM_ARRAY_LIST.randomGet())));
                    }
                }
            }, 40);

            HadesGameScheduleManager.INSTANCE.delayRunTask.put(new AbstractTick() {
                @Override
                protected void tick() {
                    double x = p.getX();
                    double z = p.getZ();

                    for (int i = 0; i < 10; i++) {
                        double spawnX = x - 5 + random.nextInt(10);
                        double spawnZ = z - 5 + random.nextInt(10);
                        p.world.spawnEntity(new ItemEntity(p.world, spawnX, 250, spawnZ, new ItemStack(HadesGame.ITEM_WEIGHT_RANDOM_ARRAY_LIST.randomGet())));
                    }
                }
            }, 80);
        });
    }
}
