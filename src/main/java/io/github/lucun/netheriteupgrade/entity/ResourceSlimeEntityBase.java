package io.github.lucun.netheriteupgrade.entity;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.world.IWorld;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;

/**
 * Note: in vanilla, size of small, medium, large are: 1, 2, 4(in code) or 0, 1, 3(in NBT)
 */
public abstract class ResourceSlimeEntityBase extends SlimeEntity {

    public ResourceSlimeEntityBase(EntityType<? extends SlimeEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        if (!this.world.isClient)
            this.tryGrow();
        super.tick();
    }

    @Override
    public void remove() {
        this.split();
        this.removed = true;
    }

    @Override
    public abstract EntityData initialize(IWorld world, LocalDifficulty difficulty, SpawnType spawnType, EntityData entityData, CompoundTag entityTag);

    @Override
    protected abstract Identifier getLootTableId();

    protected abstract void split();

    protected abstract void tryGrow();
}
