package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.HgPlayerInventory;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SwapInventoryEvent extends ImplicitAbstractEvent {
    public SwapInventoryEvent() {
        super("swapInventory", "背包互换", true, 60, 120);
    }

    @Override
    public void callEvent() {
        List<ServerPlayerEntity> entities = GameCore.INSTANCE.getSurvivalPlayers();
        Collections.shuffle(entities);

        LinkedList<HgPlayerInventory> inventories = entities.stream().map(HgPlayerInventory::valueOf).map(HgPlayerInventory::clone).collect(Collectors.toCollection(LinkedList::new));
        inventories.offer(inventories.remove());

        Iterator<HgPlayerInventory> inventoryItr = inventories.iterator();
        for (int i = 0; i < entities.size(); i++) {
            inventoryItr.next().append(entities.get(i));
        }
    }
}
