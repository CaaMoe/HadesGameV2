package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.Location;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SwapLocationEvent extends ImplicitAbstractEvent {
    public SwapLocationEvent() {
        super("swapLocation", "斗转星移", true, 60, 120);
    }

    @Override
    public void callEvent() {
        List<ServerPlayerEntity> entities = GameCore.INSTANCE.getSurvivalPlayer();
        Collections.shuffle(entities);

        LinkedList<Location> locations = entities.stream().map(Location::valueOf).map(Location::clone).collect(Collectors.toCollection(LinkedList::new));
        locations.offer(locations.remove());

        Iterator<Location> locItr = locations.iterator();
        for (int i = 0; i < entities.size(); i++) {
            Location loc = locItr.next();
            entities.get(i).teleport(loc.world, loc.pos.x, loc.pos.y, loc.pos.z, loc.yaw, loc.pitch);
        }
    }

}
