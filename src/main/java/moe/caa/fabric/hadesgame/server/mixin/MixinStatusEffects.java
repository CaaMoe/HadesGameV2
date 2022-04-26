package moe.caa.fabric.hadesgame.server.mixin;

import moe.caa.fabric.hadesgame.server.HadesGame;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StatusEffects.class)
public abstract class MixinStatusEffects {

    @Inject(method = "register", at = @At("TAIL"))
    private static void onRegister(int rawId, String id, StatusEffect entry, CallbackInfoReturnable<StatusEffect> cir) {
        // 7  instant_damage 瞬间伤害
        // 20 wither 凋零
        if (rawId == 7 || rawId == 20) {
            System.out.println("移除效果 - " + id);
            return;
        }


        HadesGame.STATUS_EFFECT_WEIGHT_RANDOM_ARRAY_LIST.add(cir.getReturnValue(), 10);
    }
}
