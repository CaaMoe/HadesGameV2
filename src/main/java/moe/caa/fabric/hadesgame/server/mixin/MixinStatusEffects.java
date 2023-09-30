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
    private static void onRegister(String id, StatusEffect statusEffect, CallbackInfoReturnable<StatusEffect> cir) {
        // 7  instant_damage 瞬间伤害
        // 20 wither 凋零
        if (id.equals("wither") || id.equals("instant_damage")) {
            return;
        }


        HadesGame.STATUS_EFFECT_WEIGHT_RANDOM_ARRAY_LIST.add(cir.getReturnValue(), 10);
    }
}
