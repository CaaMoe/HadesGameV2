package moe.caa.fabric.hadesgame.server.mixin;

import moe.caa.fabric.hadesgame.server.Contains;
import moe.caa.fabric.hadesgame.server.IItemEntityHandler;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class MixinItemEntity implements IItemEntityHandler {
    @Shadow
    private int pickupDelay;
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

    @Inject(method = "onPlayerCollision", at = @At("HEAD"), cancellable = true)
    public void onOnPlayerCollision(PlayerEntity player, CallbackInfo ci) {
        if (Contains.doNotPickupItem) {
            ci.cancel();
        }
    }
}
