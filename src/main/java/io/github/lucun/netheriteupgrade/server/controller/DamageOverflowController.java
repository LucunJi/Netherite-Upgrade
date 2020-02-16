package io.github.lucun.netheriteupgrade.server.controller;

import io.github.lucun.netheriteupgrade.api.FlowControllerAbstract;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundEvents;

import java.util.Random;


public class DamageOverflowController extends FlowControllerAbstract<Float> {
    public static final DamageOverflowController INSTANCE = new DamageOverflowController();

    private Random rnd = new Random();

    private float damage = 0;

    public void begin() {
        super.begin();
        this.damage = 0;
    }

    @Override
    protected void onFeed(Float item) {
        this.damage += item;
    }

    public boolean transfer(LivingEntity livingEntity, DamageSource damageSource, float dmg) {
        if (!this.alive) {
            throw new IllegalStateException("Failed to transfer overflown damage, the transferrer is not alive!");
        }

        if (this.damage > 0.0f) {
            dmg += this.damage;
            Entity attacker = damageSource.getAttacker();
            if (attacker != null) {
                attacker.world.playSound(
                        null, attacker.getX(), attacker.getY(), attacker.getZ(),
                        SoundEvents.BLOCK_ANVIL_PLACE, attacker.getSoundCategory(), 1.0F, 1.0F + rnd.nextFloat() * 5
                );
            }
            this.damage = 0;
        }
        return livingEntity.damage(damageSource, dmg);
    }
}
