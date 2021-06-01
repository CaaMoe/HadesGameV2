package moe.caa.fabric.hadesgame.server.mixin;

import moe.caa.fabric.hadesgame.server.IItemEntityHandler;
import moe.caa.fabric.hadesgame.server.IServerWorldHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static moe.caa.fabric.hadesgame.server.HadesGame.ITEM_WEIGHT_RANDOM_ARRAY_LIST;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld implements IServerWorldHandler {
    private boolean hg_tripleDrop = false;
    private boolean hg_randomDrop = false;

    @Shadow
    public abstract boolean spawnEntity(Entity entity);

    @Override
    public void hg_setRandomDrop(boolean b) {
        hg_randomDrop = b;
    }

    @Override
    public void hg_setTripleDrop(boolean b) {
        hg_tripleDrop = b;
    }

    @Inject(method = "spawnEntity", at = @At("HEAD"))
    private void onSpawnEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (hg_randomDrop && entity instanceof ItemEntity) {
            ((ItemEntity) entity).setStack(ITEM_WEIGHT_RANDOM_ARRAY_LIST.randomGet().getDefaultStack());
        }
        Vec3d vec3d = entity.getVelocity();
        if (hg_tripleDrop && entity instanceof ItemEntity && !((IItemEntityHandler) entity).hg_isModSpawn()) {
            for (int i = 0; i < 3; i++) {
                ItemEntity itemEntity = new ItemEntity(entity.world, entity.getX(), entity.getY(), entity.getZ(), ((ItemEntity) entity).getStack());
                ((IItemEntityHandler) itemEntity).hg_setModSpawn(true);
                itemEntity.setPickupDelay(((IItemEntityHandler) entity).hg_getPickupDelay());
                itemEntity.setVelocity(vec3d);
                spawnEntity(itemEntity);
            }
        }
    }
}
