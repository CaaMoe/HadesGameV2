package moe.caa.fabric.hadesgame.server.mixin;

import moe.caa.fabric.hadesgame.server.Contains;
import moe.caa.fabric.hadesgame.server.IItemEntityHandler;
import moe.caa.fabric.hadesgame.server.IServerWorldHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

import static moe.caa.fabric.hadesgame.server.HadesGame.ITEM_WEIGHT_RANDOM_ARRAY_LIST;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld extends World implements IServerWorldHandler {
    private boolean hg_tripleDrop = false;
    private boolean hg_randomDrop = false;

    protected MixinServerWorld(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> dimension, Supplier<Profiler> supplier, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, dimension, supplier, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

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
            if(!Contains.doNntModifyDropType.remove(entity.getUuid())){
                ((ItemEntity) entity).setStack(ITEM_WEIGHT_RANDOM_ARRAY_LIST.randomGet().getDefaultStack());
            }
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
