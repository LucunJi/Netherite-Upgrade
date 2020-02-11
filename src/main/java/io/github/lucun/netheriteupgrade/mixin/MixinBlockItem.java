package io.github.lucun.netheriteupgrade.mixin;

import io.github.lucun.netheriteupgrade.Main;
import io.github.lucun.netheriteupgrade.api.multiblock.pattern.PatternMatcher;
import io.github.lucun.netheriteupgrade.structure.Patterns;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class MixinBlockItem {

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(
            value = "TAIL"
    ))
    private void onBlockPlacementSuccess(ItemPlacementContext context, CallbackInfoReturnable callbackInfoReturnable) {
        Main.LOGGER.info(
                (context.getWorld().isClient() ? "Client" : "Server") + ": Try to match pattern " + Patterns.TEST.getName() + " -- " +
                PatternMatcher.matchSubPatterns(context.getWorld(), context.getBlockPos(), Patterns.TEST));

    }
}
