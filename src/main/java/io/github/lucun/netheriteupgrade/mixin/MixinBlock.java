package io.github.lucun.netheriteupgrade.mixin;

import io.github.lucun.netheriteupgrade.entity.FireproofItemContainerEntity;
import io.github.lucun.netheriteupgrade.server.flow.ThermalConductionController;
import io.github.lucun.netheriteupgrade.api.multiblock.pattern.PatternMatcher;
import io.github.lucun.netheriteupgrade.structure.Patterns;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.FireworkEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class MixinBlock {
    @Inject(method = "afterBreak", at = @At(value = "HEAD"))
    private void preBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack, CallbackInfo callbackInfo) {
        if (world instanceof ServerWorld) {
            if (player instanceof ServerPlayerEntity && player.getMainHandStack().getItem() == Items.NETHERITE_PICKAXE) {
                ThermalConductionController.INSTANCE.begin();
            }
        }
    }

    @Redirect(method = "dropStack", at = @At(value = "NEW", target = "Lnet/minecraft/entity/ItemEntity;"))
    private static ItemEntity onBlockBreak(World world, double x, double y, double z, ItemStack itemStack) {
        if (ThermalConductionController.INSTANCE.isAlive()) {
            return FireproofItemContainerEntity.make(world, x, y, z, itemStack);
        } else {
            return new ItemEntity(world, x, y, z, itemStack);
        }
    }

    @Inject(method = "afterBreak", at = @At(value = "RETURN"))
    private void postBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack, CallbackInfo callbackInfo) {
        if (world instanceof ServerWorld) {
            ThermalConductionController.INSTANCE.end();
        }
    }
}
