package moe.caa.fabric.hadesgame.server.gameevent.coreevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.GameState;
import moe.caa.fabric.hadesgame.server.HadesGame;
import moe.caa.fabric.hadesgame.server.gameevent.AbstractEvent;
import net.minecraft.world.GameMode;

public class GameEndingEvent extends CoreAbstractEvent {
    public GameEndingEvent() {
        super("returnToLobby", "返回大厅", true, 15, 17);
    }

    @Override
    public void tickCountdownSecond(int countdown) {

    }

    @Override
    public void callEvent() {
        GameCore.INSTANCE.allPlayerHandler(p -> {
            GameCore.INSTANCE.clearState(p, GameMode.ADVENTURE);
            GameCore.INSTANCE.teleport(p, HadesGame.getLobbyLocation());
        });
        HadesGame.server.get().getOverworld().getWorldBorder().setCenter(0, 0);
        HadesGame.server.get().getOverworld().getWorldBorder().setSize(200);
        GameCore.INSTANCE.currentState = GameState.WAITING;
    }

    @Override
    public AbstractEvent getNextEvent() {
        return new GameWaitingEvent();
    }
}
