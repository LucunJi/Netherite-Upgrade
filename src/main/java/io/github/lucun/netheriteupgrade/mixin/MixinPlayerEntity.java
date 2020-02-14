package io.github.lucun.netheriteupgrade.mixin;

import io.github.lucun.netheriteupgrade.server.controller.DamageOverflowController;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity {

    protected MixinPlayerEntity(EntityType<? extends LivingEntity> type, World world) {
        super(type, world);
    }

    @Inject(
            method = "attack", at = @At(
            value = "HEAD"
    ))
    private void preAttack(Entity target, CallbackInfo callbackInfo) {
        if (this.getMainHandStack().getItem() == Items.NETHERITE_SWORD) {
            DamageOverflowController.INSTANCE.begin();
        }
    }

    @Redirect(
            method = "attack", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
    ))
    private boolean onDamage(LivingEntity livingEntity, DamageSource damageSource, float dmg) {
        if (DamageOverflowController.INSTANCE.isAlive()) {
            return DamageOverflowController.INSTANCE.transfer(livingEntity, damageSource, dmg);
        } else {
            return livingEntity.damage(damageSource, dmg);
        }
    }

    @Inject(
            method = "attack", at = @At(
            value = "RETURN"
    ))
    private void postAttack(Entity target, CallbackInfo callbackInfo) {
        DamageOverflowController.INSTANCE.end();
    }
}
