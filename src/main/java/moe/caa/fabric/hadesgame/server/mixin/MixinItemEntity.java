package moe.caa.fabric.hadesgame.server.mixin;

import moe.caa.fabric.hadesgame.server.Contains;
import moe.caa.fabric.hadesgame.server.IItemEntityHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class MixinItemEntity extends Entity implements IItemEntityHandler {
    @Shadow
    private int pickupDelay;

    @Shadow private int itemAge;
    @Unique
    private boolean hg_modSpawn = false;

    public MixinItemEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void onTick(CallbackInfo ci){
        if(itemAge >= 20 * 10){
            this.discard();
        }
    }


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
