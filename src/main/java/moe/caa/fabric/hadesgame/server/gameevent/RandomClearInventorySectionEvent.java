package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.HgPlayerInventory;
import net.minecraft.item.ItemStack;

public class RandomClearInventorySectionEvent extends ImplicitAbstractEvent {
    public RandomClearInventorySectionEvent() {
        super("randomClearItem", "背包破损", true, 60, 120);
    }

    @Override
    public void callEvent() {
        GameCore.INSTANCE.survivalPlayerHandler(p -> {
            HgPlayerInventory hgPlayerInventory = HgPlayerInventory.valueOf(p);
            for (int i = 0; i < hgPlayerInventory.armor.size(); i++) {
                if (0.3 > Math.random()) {
                    hgPlayerInventory.armor.set(i, ItemStack.EMPTY);
                }
            }
            for (int i = 0; i < hgPlayerInventory.main.size(); i++) {
                if (0.3 > Math.random()) {
                    hgPlayerInventory.main.set(i, ItemStack.EMPTY);
                }
            }
            for (int i = 0; i < hgPlayerInventory.offHand.size(); i++) {
                if (0.3 > Math.random()) {
                    hgPlayerInventory.offHand.set(i, ItemStack.EMPTY);
                }
            }
        });
    }
}
