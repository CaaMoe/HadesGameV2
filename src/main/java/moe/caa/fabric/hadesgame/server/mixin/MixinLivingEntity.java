package moe.caa.fabric.hadesgame.server.mixin;

import moe.caa.fabric.hadesgame.server.callback.LivingEntityChangeHealthCallback;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {

    @Inject(method = "setHealth", at = @At("RETURN"))
    private void onChangeHealth(float health, CallbackInfo ci) {
        LivingEntityChangeHealthCallback.EVENT.invoker().register((LivingEntity) (Object) this, health);
    }
}
