package io.github.lucun.netheriteupgrade.mixin;

import io.github.lucun.netheriteupgrade.server.processcontroller.DamageOverflowController;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    @Shadow public abstract float getHealth();

    public MixinLivingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "setHealth", at = @At(
            value = "HEAD"
            ))
    private void onSetHealth(float health, CallbackInfo callbackInfo) {
        if (health < 0 && DamageOverflowController.INSTANCE.isAlive()) {
            DamageOverflowController.INSTANCE.collect(-health);
        }
    }

    @Inject(method = "tick", at = @At(
            value = "HEAD"
    ))
    private void onTick(CallbackInfo callbackInfo) {
        this.setCustomName(new LiteralText("HP: " + this.getHealth()));
        this.setCustomNameVisible(true);
    }
}
