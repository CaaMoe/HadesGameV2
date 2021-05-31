package moe.caa.fabric.hadesgame.server.mixin;

import moe.caa.fabric.hadesgame.server.IItemEntityHandler;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemEntity.class)
public abstract class MixinItemEntity implements IItemEntityHandler {
    @Shadow private int pickupDelay;
    private boolean hg_modSpawn = false;

    @Override
    public void hg_setModSpawn(boolean b) {
        this.hg_modSpawn = b;
    }

    @Override
    public boolean hg_isModSpawn() {
        return hg_modSpawn;
    }

    @Override
    public int hg_getPickupDelay() {
        return this.pickupDelay;
    }
}
