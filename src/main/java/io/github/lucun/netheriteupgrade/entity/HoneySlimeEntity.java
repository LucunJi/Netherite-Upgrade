package io.github.lucun.netheriteupgrade.entity;

import com.google.common.base.Predicates;
import io.github.lucun.netheriteupgrade.entity.ai.SlimeAI;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;

public class HoneySlimeEntity extends ResourceSlimeEntityBase {
    private int honeyStorage = 0;
    private int honeyRegenCooldown = 0;

    public HoneySlimeEntity(EntityType<? extends HoneySlimeEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new SlimeAI.SlimeMoveControl(this);
    }

    /********** Initializations **********/
    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SlimeAI.SwimmingGoal(this));
        this.goalSelector.add(2, new SlimeAI.FaceTowardTargetGoal(this));
        this.goalSelector.add(3, new SlimeAI.RandomLookGoal(this));
        this.goalSelector.add(5, new SlimeAI.MoveGoal(this));
        this.targetSelector.add(3, new FollowTargetGoal<>(this, PlayerEntity.class, 10, true, false,
                player -> player instanceof PlayerEntity && (player.getMainHandStack().getItem() == Items.HONEY_BOTTLE || player.getOffHandStack().getItem() == Items.HONEY_BOTTLE) && this.getSize() <= 2
        ));
        this.targetSelector.add(7, new FollowTargetGoal<>(this, BeeEntity.class, 20, true, false,
                bee -> bee instanceof BeeEntity && ((BeeEntity)bee).getFlowerPos() != null && this.getSize() <= 2 && Math.abs(bee.getY() - this.getY()) < 4
        ));
//        this.targetSelector.add(8, new MoveToTargetPosGoal(this, 1, 16) {
//            @Override
//            protected boolean isTargetPos(WorldView world, BlockPos pos) {
//                return ((ServerWorld) world).getPointOfInterestStorage().getNearestPosition(
//                        Predicate.isEqual(PointOfInterestType.BEE_NEST),
//                        Predicates.alwaysTrue(),
//                        HoneySlimeEntity.this.getBlockPos(),
//                        16,
//                        PointOfInterestStorage.OccupationStatus.ANY).isPresent();
//            }
//        });
    }

    @Override
    protected ParticleEffect getParticles() {
        return ParticleTypes.LANDING_HONEY;
    }

    @Override
    public EntityData initialize(IWorld world, LocalDifficulty difficulty, SpawnType spawnType, EntityData entityData, CompoundTag entityTag) {
        float f = world.getRandom().nextFloat();
        if (f < 0.8f) {
            this.setSize(1, true);
            this.setHoneyStorage(0);
        } else if (f < 0.95f) {
            this.setSize(2, true);
            this.setHoneyStorage(2);
        } else {
            this.setSize(4, true);
            this.setHoneyStorage(13);
        }
        this.experiencePoints = 0;
        return entityData;
    }

    @Override
    protected Identifier getLootTableId() {
        return null;
    }

    /********** Pacifist **********/
    @Override
    public void onPlayerCollision(PlayerEntity player) {
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (super.damage(source, amount)) {
            if (source.getAttacker() != null && source.getAttacker() instanceof LivingEntity) {
                this.world.getEntities(
                        BeeEntity.class,
                        this.getBoundingBox().expand(5),
                        null)
                        .forEach(beeEntity -> beeEntity.setAttacker(((LivingEntity) source.getAttacker())));
            }
            return true;
        } else {
            return false;
        }
    }

    /********** Generate and decrease honey **********/
    private void generateHoney() {
        if (this.getHoneyCooldown() == 0) {
            this.setHoneyStorage(this.getHoneyStorage() + 1);
            this.setHoneyCooldown(5 * 60 * 20 * (this.getSize()));
        } else {
            this.setHoneyCooldown(this.getHoneyCooldown() - 1);
        }
    }

    @Override
    public boolean interactMob(PlayerEntity player, Hand hand) {
        if (!this.world.isClient()) {
            ItemStack itemStack = player.getStackInHand(hand);
            if (itemStack.getItem() == Items.GLASS_BOTTLE && this.getSize() > 1 && this.honeyStorage > 0 && this.isAlive()) {
                player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
                itemStack.decrement(1);
                if (itemStack.isEmpty()) {
                    player.setStackInHand(hand, new ItemStack(Items.HONEY_BOTTLE));
                } else if (!player.inventory.insertStack(new ItemStack(Items.HONEY_BOTTLE))) {
                    player.dropItem(new ItemStack(Items.HONEY_BOTTLE), false);
                }

                this.setHoneyStorage(this.getHoneyStorage() - 1);

                return true;
            } else {
                return super.interactMob(player, hand);
            }
        }
        return super.interactMob(player, hand);
    }

    @Override
    protected void tryGrow() {
        this.generateHoney();
        // grow up
        if (this.getSize() < 2 && this.getHoneyStorage() >= 4) {
            this.setSize(2, false);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, (4-1)*50));
        }
        if (this.getSize() < 4 && this.getHoneyStorage() >= 15) {
            this.setSize(4, false);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, (16-4)*50));
        }

        // shrink down
        if (this.getSize() >= 4 && this.getHoneyStorage() < 8) {
            this.setSize(2, false);
            this.setHealth(this.getMaximumHealth());
        }
        if (this.getSize() >= 2 && this.getHoneyStorage() < 1) {
            this.setSize(1, false);
            this.setHealth(this.getMaximumHealth());
        }

        // not willing to leave hive at night
        if (this.getSize() <= 2 && (this.world.isNight() || this.world.isRaining())) {
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 1, true, true));
        }
    }

    /********** Getters and setters **********/
    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt("Honey", honeyStorage);
        tag.putInt("HoneyCooldown", this.getHoneyCooldown());
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        this.setHoneyStorage(tag.getInt("Honey"));
        this.setHoneyCooldown(tag.getInt("HoneyCooldown"));
        super.readCustomDataFromTag(tag);
    }

    public int getHoneyStorage() {
        return honeyStorage;
    }

    public void setHoneyStorage(int honeyStorage) {
        this.honeyStorage = MathHelper.clamp(honeyStorage, 0, 15);
    }

    public int getHoneyCooldown() {
        return honeyRegenCooldown;
    }

    public void setHoneyCooldown(int honeyRegenCooldown) {
        this.honeyRegenCooldown = Math.max(honeyRegenCooldown, 0);
    }

    /********** Useless **********/
    @Override
    protected void split() {}

    private static class WanderAroundHiveGoal extends TrackTargetGoal {

        public WanderAroundHiveGoal(MobEntity mob) {
            super(mob, false);
        }

        @Override
        public boolean canStart() {
            return false;
        }
    }
}
