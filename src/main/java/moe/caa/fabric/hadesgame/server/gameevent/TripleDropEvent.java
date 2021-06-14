package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.HadesGame;
import moe.caa.fabric.hadesgame.server.IServerWorldHandler;
import moe.caa.fabric.hadesgame.server.schedule.HadesGameScheduleManager;
import net.minecraft.server.world.ServerWorld;

public class TripleDropEvent extends ImplicitAbstractEvent {

    public TripleDropEvent() {
        super("tripleDrop", "三倍掉落", true, 30, 100);
    }

    @Override
    public void callEvent() {
        for (ServerWorld world : HadesGame.server.get().getWorlds()) {
            ((IServerWorldHandler) world).hg_setTripleDrop(true);
        }
        HadesGameScheduleManager.runTaskLater(() -> {
            for (ServerWorld world : HadesGame.server.get().getWorlds()) {
                ((IServerWorldHandler) world).hg_setTripleDrop(false);
            }
        }, 20 * 10);
    }

    @Override
    public void gameEnd() {
        for (ServerWorld world : HadesGame.server.get().getWorlds()) {
            ((IServerWorldHandler) world).hg_setTripleDrop(false);
        }
    }
}
