package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.schedule.AbstractTick;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class CorrosionEvent extends ImplicitAbstractEvent {
    private final List<AbstractTick> eventTick = new ArrayList<>();

    public CorrosionEvent() {
        super("corrosion", "腐蚀", true, 30, 100);
    }

    @Override
    public void callEvent() {
        AbstractTick tick = new AbstractTick(80) {
            private int count = 0;

            @Override
            protected void tick() {
                count++;
                GameCore.INSTANCE.survivalPlayerHandler(player -> corrosion(player));
                if (count > 10) cancel();
            }
        };

        HadesGameScheduleManager.runTaskTimer(tick);
        eventTick.add(tick);
    }

    public void corrosion(ServerPlayerEntity entity) {
        ServerWorld world = entity.getWorld();
        final Vec3d pos = entity.getPos();

        int speech = 2;

        for (int y = (int) (pos.getY() - speech); y < pos.getY() + speech; y++) {
            for (int x = (int) (pos.getX() - speech); x < pos.getX() + speech; x++) {
                for (int z = (int) (pos.getZ() - speech); z < pos.getZ() + speech; z++) {
                    world.breakBlock(new BlockPos(x, y, z), true);
                }
            }
        }

        int damageNum = 23;
        for (ItemStack stack : entity.getInventory().main) {
            if (stack.isDamageable()) {
                final int damage = stack.getDamage() + damageNum;
                stack.setDamage(damage);
            }
        }

        for (ItemStack stack : entity.getInventory().armor) {
            if (stack.isDamageable()) {
                final int damage = stack.getDamage() + damageNum;
                stack.setDamage(damage);
            }
        }

        for (ItemStack stack : entity.getInventory().offHand) {
            if (stack.isDamageable()) {
                final int damage = stack.getDamage() + damageNum;
                stack.setDamage(damage);
            }
        }


    }

    @Override
    public void gameEnd() {
        eventTick.forEach(AbstractTick::cancel);
        eventTick.clear();
    }
}
