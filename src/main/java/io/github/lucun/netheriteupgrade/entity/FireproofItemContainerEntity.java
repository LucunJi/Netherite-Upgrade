package io.github.lucun.netheriteupgrade.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FireproofItemContainerEntity extends ItemEntity {

    public FireproofItemContainerEntity(EntityType<? extends FireproofItemContainerEntity> entityEntityType, World world) {
        super(entityEntityType, world);
    }

    public static FireproofItemContainerEntity make(World world, double x, double y, double z, ItemStack stack) {
        FireproofItemContainerEntity entity = make(world, x, y, z);
        entity.setStack(stack);
        return entity;
    }

    public static FireproofItemContainerEntity make(World world, double x, double y, double z) {
        FireproofItemContainerEntity entity = new FireproofItemContainerEntity(EntityTypes.FIREPROOF_CONTAINER, world);
        entity.updatePosition(x, y, z);
        entity.yaw = entity.random.nextFloat() * 360.0F;
        entity.setVelocity(entity.random.nextDouble() * 0.2D - 0.1D, 0.2D, entity.random.nextDouble() * 0.2D - 0.1D);
        return entity;
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
