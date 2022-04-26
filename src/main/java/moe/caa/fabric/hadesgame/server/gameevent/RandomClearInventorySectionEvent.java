package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import moe.caa.fabric.hadesgame.server.HgPlayerInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomClearInventorySectionEvent extends ImplicitAbstractEvent {
    public RandomClearInventorySectionEvent() {
        super("randomClearItem", "背包破损", true, 30, 100);
    }

    @Override
    public void callEvent() {
        GameCore.INSTANCE.survivalPlayerHandler(p -> {
            HgPlayerInventory hgPlayerInventory = HgPlayerInventory.valueOf(p);
            final List<ItemStack> stacks = new ArrayList<>();
            for (int i = 0; i < hgPlayerInventory.armor.size(); i++) {
                if (0.6 > Math.random()) {
                    stacks.add(hgPlayerInventory.armor.get(i));
                }
            }
            for (int i = 0; i < hgPlayerInventory.main.size(); i++) {
                if (0.6 > Math.random()) {
                    stacks.add(hgPlayerInventory.main.get(i));
                }
            }
            for (int i = 0; i < hgPlayerInventory.offHand.size(); i++) {
                if (0.6 > Math.random()) {
                    stacks.add(hgPlayerInventory.offHand.get(i));
                }
            }

            while (stacks.size() <= 41) {
                stacks.add(ItemStack.EMPTY);
            }

            Collections.shuffle(stacks);

            for (int i = 0; i < 4; i++) {
                hgPlayerInventory.armor.set(i, stacks.get(i));
            }
            for (int i = 0; i < 36; i++) {
                hgPlayerInventory.main.set(i, stacks.get(i + 4));
            }
            hgPlayerInventory.offHand.set(0, stacks.get(40));
        });
    }
}
