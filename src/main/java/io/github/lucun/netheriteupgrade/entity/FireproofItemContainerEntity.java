package io.github.lucun.netheriteupgrade.entity;

import io.github.lucun.netheriteupgrade.Main;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class FireproofItemContainerEntity extends ItemEntity {

    public FireproofItemContainerEntity(EntityType<? extends FireproofItemContainerEntity> entityEntityType, World world) {
        super(entityEntityType, world);
    }

    public static FireproofItemContainerEntity make(World world, double x, double y, double z, ItemStack stack) {
        FireproofItemContainerEntity entity = new FireproofItemContainerEntity(Main.FIREPROOF_CONTAINER, world);
        entity.updatePosition(x, y, z);
        entity.yaw = entity.random.nextFloat() * 360.0F;
        entity.setVelocity(entity.random.nextDouble() * 0.2D - 0.1D, 0.2D, entity.random.nextDouble() * 0.2D - 0.1D);
        entity.setStack(stack);
        return entity;
    }

    public static FireproofItemContainerEntity make(World world, double x, double y, double z) {
        FireproofItemContainerEntity entity = new FireproofItemContainerEntity(Main.FIREPROOF_CONTAINER, world);
        entity.updatePosition(x, y, z);
        entity.yaw = entity.random.nextFloat() * 360.0F;
        entity.setVelocity(entity.random.nextDouble() * 0.2D - 0.1D, 0.2D, entity.random.nextDouble() * 0.2D - 0.1D);
        return entity;
    }

    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.isFire()) {
            return false;
        } else {
            return super.damage(source, amount);
        }
    }
}
