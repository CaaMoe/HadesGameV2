package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.schedule.AbstractTick;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Heightmap;

import java.util.Random;

public class ThunderstormEvent extends AbstractEvent {
    public ThunderstormEvent() {
        super("thunderstorm", "雷暴", true, 60, 120);
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

        GameCore.INSTANCE.survivalPlayerHandler(player -> {
            double x = player.getPos().x - 2 + random.nextInt(4);
            double z = player.getPos().z - 2 + random.nextInt(4);

            CompoundTag nbt = new CompoundTag();
            int y = player.world.getTopY(Heightmap.Type.MOTION_BLOCKING, (int) x, (int) z);

            nbt.putString("id", "minecraft:lightning_bolt");
            Entity entity2 = EntityType.loadEntityWithPassengers(nbt, player.world, (entityx) -> {
                entityx.refreshPositionAndAngles(x, y, z, entityx.prevYaw, entityx.prevPitch);
                return entityx;
            });

            ((ServerWorld) player.world).shouldCreateNewEntityWithPassenger(entity2);

            HadesGameScheduleManager.INSTANCE.delayRunTask.put(new AbstractTick() {
                @Override
                protected void tick() {
                    double x = player.getPos().x - 2 + random.nextInt(4);
                    double z = player.getPos().z - 2 + random.nextInt(4);

                    CompoundTag nbt = new CompoundTag();
                    int y = player.world.getTopY(Heightmap.Type.MOTION_BLOCKING, (int) x, (int) z);

                    nbt.putString("id", "minecraft:lightning_bolt");
                    Entity entity2 = EntityType.loadEntityWithPassengers(nbt, player.world, (entityx) -> {
                        entityx.refreshPositionAndAngles(x, y, z, entityx.prevYaw, entityx.prevPitch);
                        return entityx;
                    });

                    ((ServerWorld) player.world).shouldCreateNewEntityWithPassenger(entity2);
                }
            }, 40);

            HadesGameScheduleManager.INSTANCE.delayRunTask.put(new AbstractTick() {
                @Override
                protected void tick() {
                    double x = player.getPos().x - 2 + random.nextInt(4);
                    double z = player.getPos().z - 2 + random.nextInt(4);

                    CompoundTag nbt = new CompoundTag();
                    int y = player.world.getTopY(Heightmap.Type.MOTION_BLOCKING, (int) x, (int) z);

                    nbt.putString("id", "minecraft:lightning_bolt");
                    Entity entity2 = EntityType.loadEntityWithPassengers(nbt, player.world, (entityx) -> {
                        entityx.refreshPositionAndAngles(x, y, z, entityx.prevYaw, entityx.prevPitch);
                        return entityx;
                    });

                    ((ServerWorld) player.world).shouldCreateNewEntityWithPassenger(entity2);
                }
            }, 80);
        });
    }
}
