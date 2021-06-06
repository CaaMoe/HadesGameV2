package moe.caa.fabric.hadesgame.server.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static moe.caa.fabric.hadesgame.server.HadesGame.BLOCK_WEIGHT_RANDOM_ARRAY_LIST;

@Mixin(Blocks.class)
public abstract class MixinBlocks {

    @Inject(method = "register", at = @At("TAIL"))
    private static void onRegister(String id, Block block, CallbackInfoReturnable<Block> cir){
        BLOCK_WEIGHT_RANDOM_ARRAY_LIST.add(cir.getReturnValue(), 10);
    }
}
