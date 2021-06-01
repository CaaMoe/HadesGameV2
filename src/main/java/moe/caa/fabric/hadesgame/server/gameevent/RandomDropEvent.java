package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.HadesGame;
import moe.caa.fabric.hadesgame.server.IServerWorldHandler;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import net.minecraft.server.world.ServerWorld;

public class RandomDropEvent extends ImplicitAbstractEvent {

    public RandomDropEvent() {
        super("randomDrop", "随机掉落", true, 60, 120);
    }

    @Override
    public void callEvent() {
        for (ServerWorld world : HadesGame.server.get().getWorlds()) {
            ((IServerWorldHandler) world).hg_setRandomDrop(true);
        }
        HadesGameScheduleManager.runTaskLater(() -> {
            for (ServerWorld world : HadesGame.server.get().getWorlds()) {
                ((IServerWorldHandler) world).hg_setRandomDrop(false);
            }
        }, 20 * 90);
    }

    @Override
    public void gameEnd() {
        for (ServerWorld world : HadesGame.server.get().getWorlds()) {
            ((IServerWorldHandler) world).hg_setRandomDrop(false);
        }
    }
}
