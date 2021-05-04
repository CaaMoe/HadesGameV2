package moe.caa.fabric.hadesgame.server;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class HgPlayerInventory {
    public final List<ItemStack> main;
    public final List<ItemStack> armor;
    public final List<ItemStack> offHand;

    public HgPlayerInventory(List<ItemStack> main, List<ItemStack> armor, List<ItemStack> offHand) {
        this.main = main;
        this.armor = armor;
        this.offHand = offHand;
    }

    public static HgPlayerInventory valueOf(ServerPlayerEntity entity) {
        PlayerInventory inventory = entity.inventory;
        return new HgPlayerInventory(inventory.main, inventory.armor, inventory.offHand);
    }

    @Override
    public HgPlayerInventory clone() {
        List<ItemStack> cloneMain = new ArrayList<>(main.size());
        List<ItemStack> cloneArmor = new ArrayList<>(armor.size());
        List<ItemStack> cloneOffHand = new ArrayList<>(offHand.size());

        cloneMain.addAll(main);
        cloneArmor.addAll(armor);
        cloneOffHand.addAll(offHand);
        return new HgPlayerInventory(cloneMain, cloneArmor, cloneOffHand);
    }

    public void append(ServerPlayerEntity entity) {
        PlayerInventory inventory = entity.inventory;
        inventory.clear();
        for (int i = 0; i < main.size(); i++) {
            inventory.main.set(i, main.get(i));
        }
        for (int i = 0; i < armor.size(); i++) {
            inventory.armor.set(i, armor.get(i));
        }
        for (int i = 0; i < offHand.size(); i++) {
            inventory.offHand.set(i, offHand.get(i));
        }
    }
}
