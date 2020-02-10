package io.github.lucun.netheriteupgrade.server.processcontroller;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;


public class DamageOverflowController {
    public static final DamageOverflowController INSTANCE = new DamageOverflowController();
    private static Logger LOGGER = LogManager.getLogger();

    private Random rnd = new Random();

    private float damage = 0;
    private boolean alive = false;

    public void begin() {
        this.alive = true;
        this.damage = 0;
    }

    public void collect(float dmg) {
        if (!this.alive) {
            LOGGER.info("Failed to collect overflown damage, the transferrer is not alive!");
        }
        this.damage += dmg;
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

    public void end() {
        this.alive = false;
    }

    public boolean isAlive() {
        return alive;
    }
}
