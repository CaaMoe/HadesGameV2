package moe.caa.fabric.hadesgame.server.mixin;

import moe.caa.fabric.hadesgame.server.CompassItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CompassItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class MixinItem_CompassItem {

    @Inject(method = "use", at = @At("HEAD"))
    private void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (!world.isClient) {
            ItemStack itemStack = user.getStackInHand(hand);
            if (itemStack.getItem() instanceof CompassItem) {
                CompassItemHandler.INSTANCE.put(world, (ServerPlayerEntity) user, itemStack);
            }
        }
    }
}
