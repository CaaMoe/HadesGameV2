package moe.caa.fabric.hadesgame.server.mixin;

import moe.caa.fabric.hadesgame.server.Contains;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class MixinServerPlayerInteractionManager {

    @Shadow
    protected ServerWorld world;

    @Inject(method = "tryBreakBlock", at = @At("TAIL"))
    private void onBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && Contains.superMiners) {
            int speech = 3;

            for (int y = pos.getY() - speech; y < pos.getY() + speech; y++) {
                for (int x = pos.getX() - speech; x < pos.getX() + speech; x++) {
                    for (int z = pos.getZ() - speech; z < pos.getZ() + speech; z++) {
                        world.breakBlock(new BlockPos(x, y, z), true);
                    }
                }
            }
        }
    }
}
