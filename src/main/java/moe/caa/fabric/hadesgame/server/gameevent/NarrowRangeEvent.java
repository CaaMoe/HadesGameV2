package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.HadesGame;
import net.minecraft.world.border.WorldBorder;

public class NarrowRangeEvent extends ImplicitAbstractEvent {
    public NarrowRangeEvent() {
        super("narrowRange", "缩圈", true, 60, 120);
    }

    @Override
    public void callEvent() {
        WorldBorder worldBorder = HadesGame.server.get().getOverworld().getWorldBorder();
        worldBorder.interpolateSize(worldBorder.getSize(), worldBorder.getSize() * 9/10, 10000);
    }
}
