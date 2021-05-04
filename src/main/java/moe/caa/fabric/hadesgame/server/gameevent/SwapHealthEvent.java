package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SwapHealthEvent extends AbstractEvent {
    public SwapHealthEvent() {
        super("swapHealth", "生命互换", true, 60, 120);
    }

    @Override
    public String getFormatEventName(int countdown) {
        if (countdown >= 10)
            return "\u00a7k********";
        return super.EVENT_NAME;
    }

    @Override
    public void callEvent() {
        List<ServerPlayerEntity> entities = GameCore.INSTANCE.getSurvivalPlayer();
        Collections.shuffle(entities);

        LinkedList<Float> healths = entities.stream().map(PlayerEntity::getHealth).collect(Collectors.toCollection(LinkedList::new));
        healths.offer(healths.remove());

        Iterator<Float> healthItr = healths.iterator();
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).setHealth(healthItr.next());
        }
    }
}