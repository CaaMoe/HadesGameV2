package moe.caa.fabric.hadesgame.server.mixin;

import com.mojang.authlib.GameProfile;
import moe.caa.fabric.hadesgame.server.Contains;
import moe.caa.fabric.hadesgame.server.IServerPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity implements IServerPlayer {

    private int tickNumRiptide = 0;

    public MixinServerPlayerEntity(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Redirect(method = "dropItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private boolean onDropItem(World instance, Entity entity) {
        Contains.doNntModifyDropType.add(entity.getUuid());
        return instance.spawnEntity(entity);
    }

    @Override
    protected void setFlag(int index, boolean value) {
        if (index == 7 && Contains.climb) {
            super.setFlag(index, true);
            return;
        }
        super.setFlag(index, value);
    }

    @Shadow
    public abstract ServerWorld getWorld();

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        tickNumRiptide++;
    }

    @Override
    protected void tickRiptide(Box a, Box b) {
        if (!Contains.riptide) {
            super.tickRiptide(a, b);
        } else {
            if (tickNumRiptide % 5 == 0) {
                final Box of = Box.of(this.getPos(), 4, 4, 4);

                List<Entity> list = this.getWorld().getOtherEntities(this, of);
                if (!list.isEmpty()) {
                    for (Entity value : list) {
                        if (value instanceof LivingEntity) {
                            this.attackLivingEntity((LivingEntity) value);
                        }
                    }
                }


                final Vec3d pos = getPos();
                for (int y = (int) (pos.getY()); y < pos.getY() + 2.5; y++) {
                    for (int x = (int) (pos.getX() - 1.5); x < pos.getX() + 1.5; x++) {
                        for (int z = (int) (pos.getZ() - 1.5); z < pos.getZ() + 1.5; z++) {
                            world.breakBlock(new BlockPos(x, y, z), true);
                        }
                    }
                }
            }

            if (this.riptideTicks <= 0) {
                this.setLivingFlag(4, false);
            }
        }
    }

    @Override
    public void hg_setLivingFlag(int flag, boolean value) {
        this.setLivingFlag(flag, value);
    }

    @Override
    public void hg_setFlag(int flag, boolean value) {
        this.setFlag(flag, value);
    }
}
