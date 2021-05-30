package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.HadesGame;
import moe.caa.fabric.hadesgame.server.schedule.AbstractTick;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RainItemsEvent extends ImplicitAbstractEvent {
    private final List<AbstractTick> eventTick = new ArrayList<>();
    public RainItemsEvent() {
        super("rainItem", "物品雨", true, 60, 120);
    }

    @Override
    public void callEvent() {
        Random random = new Random();

        AbstractTick tick = new AbstractTick(40) {
            private int count = 0;

            @Override
            protected void tick() {
                count++;
                GameCore.INSTANCE.survivalPlayerHandler(player -> generateItems(random, player));
                if(count > 5) cancel();
            }
        };

        HadesGameScheduleManager.runTaskTimer(tick);
        eventTick.add(tick);
    }

    private void generateItems(Random random, ServerPlayerEntity p) {
        double x = p.getX();
        double z = p.getZ();

        for (int i = 0; i < 10; i++) {
            double spawnX = x - 5 + random.nextInt(10);
            double spawnZ = z - 5 + random.nextInt(10);
            p.world.spawnEntity(new ItemEntity(p.world, spawnX, 250, spawnZ, new ItemStack(HadesGame.ITEM_WEIGHT_RANDOM_ARRAY_LIST.randomGet())));
        }
    }

    @Override
    public void gameEnd() {
        eventTick.forEach(AbstractTick::cancel);
        eventTick.clear();
    }
}
