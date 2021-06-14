package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SwapHealthEvent extends ImplicitAbstractEvent {
    public SwapHealthEvent() {
        super("swapHealth", "生命互换", true, 30, 100);
    }

    @Override
    public void callEvent() {
        List<ServerPlayerEntity> entities = GameCore.INSTANCE.getSurvivalPlayers();
        Collections.shuffle(entities);

        LinkedList<Float> healths = entities.stream().map(PlayerEntity::getHealth).collect(Collectors.toCollection(LinkedList::new));
        healths.offer(healths.remove());

        Iterator<Float> healthItr = healths.iterator();
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).setHealth(healthItr.next());
        }
    }
}
