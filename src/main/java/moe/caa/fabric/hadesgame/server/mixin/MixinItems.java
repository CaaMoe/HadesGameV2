package moe.caa.fabric.hadesgame.server.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static moe.caa.fabric.hadesgame.server.HadesGame.ITEM_WEIGHT_RANDOM_ARRAY_LIST;

@Debug(export = true)
@Mixin(Items.class)
public abstract class MixinItems {

    @Inject(method = "register(Lnet/minecraft/util/Identifier;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", at = @At("TAIL"))
    private static void onRegister(Identifier id, Item item, CallbackInfoReturnable<Item> cir) {
        ITEM_WEIGHT_RANDOM_ARRAY_LIST.add(cir.getReturnValue(), 10);
    }
}
