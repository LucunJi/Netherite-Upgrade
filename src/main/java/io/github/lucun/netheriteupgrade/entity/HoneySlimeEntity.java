package io.github.lucun.netheriteupgrade.entity;

import io.github.lucun.netheriteupgrade.entity.ai.LookNearHiveGoal;
import io.github.lucun.netheriteupgrade.entity.ai.SlimeAI;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;

public class HoneySlimeEntity extends ResourceSlimeEntityBase {
    private int honeyStorage = 0;
    private int honeyRegenCooldown = 0;
    private boolean isFindingHive = false;

    public HoneySlimeEntity(EntityType<? extends HoneySlimeEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new SlimeAI.SlimeMoveControl(this);
    }

    /********** Initializations **********/
    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SlimeAI.SwimmingGoal(this));
        this.goalSelector.add(2, new SlimeAI.FaceTowardTargetGoal(this));
        this.targetSelector.add(4, new LookNearHiveGoal(this, 8, 32));
        this.goalSelector.add(3, new SlimeAI.RandomLookGoal(this));
        this.goalSelector.add(5, new SlimeAI.MoveGoal(this));

        //if really annoying, delete it. honey slime is only for cuteness
        this.targetSelector.add(1, new ChasePlayerWithHoneyGoal(this));
        this.targetSelector.add(2, new PlayWithBeeGoal(this));
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

    /********** Pacifist, except for honey **********/
    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (!this.world.isClient()) {
            player.getItemsHand().forEach(itemStack -> {
                if (player.getRandom().nextInt(100) == 0) {
                    if (itemStack.getItem() == Items.HONEY_BOTTLE && !this.isLarge() && this.isAlive()) {
                        itemStack.decrement(1);
                        this.setHoneyStorage(this.getHoneyStorage() + 1);
                        this.dropStack(new ItemStack(Items.GLASS_BOTTLE));
                        this.playSound(SoundEvents.ITEM_HONEY_BOTTLE_DRINK, 1.0f, 1.0f);
                    }
                }
            });
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (super.damage(source, amount)) {
            if (source.getAttacker() != null && source.getAttacker() instanceof LivingEntity) {
                this.world.getEntities(
                        BeeEntity.class,
                        this.getBoundingBox().expand(5),
                        null)
                        .forEach(beeEntity -> beeEntity.setBeeAttacker(source.getAttacker()));
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
        if (this.isSmall() && this.getHoneyStorage() >= 4) {
            this.setSize(2, false);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, (4-1)*50));
        }
        if (!this.isLarge() && this.getHoneyStorage() >= 15) {
            this.setSize(4, false);
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, (16-4)*50));
        }

        // shrink down
        if (this.isLarge() && this.getHoneyStorage() < 8) {
            this.setSize(2, false);
            this.setHealth(this.getMaximumHealth());
        }
        if (!this.isSmall() && this.getHoneyStorage() < 1) {
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

    public boolean isLarge() {
        return this.getSize() >= 4;
    }

    public boolean isFindingHive() {
        return isFindingHive;
    }

    public void setFindingHive(boolean findingHive) {
        isFindingHive = findingHive;
    }

    /********** Useless **********/
    @Override
    protected void split() {}

    private static class ChasePlayerWithHoneyGoal extends FollowTargetGoal<PlayerEntity> {

        public ChasePlayerWithHoneyGoal(HoneySlimeEntity mob) {
            super(mob, PlayerEntity.class, true);
        }

        @Override
        public boolean canStart() {
            return super.canStart() && this.canRun();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && this.canRun();
        }

        private boolean canRun() {
            return !((HoneySlimeEntity) this.mob).isLarge() &&
                    this.targetEntity != null &&
                    (this.targetEntity.getMainHandStack().getItem() == Items.HONEY_BOTTLE ||
                    this.targetEntity.getOffHandStack().getItem() == Items.HONEY_BOTTLE);
        }
    }

    private static class PlayWithBeeGoal extends FollowTargetGoal<BeeEntity> {
        int playtime = 0;
        int cooldown = 0;

        public PlayWithBeeGoal(HoneySlimeEntity slime) {
            super(slime, BeeEntity.class, 20, true, false,
                    bee -> bee instanceof BeeEntity && slime.isSmall() && Math.abs(bee.getY() - slime.getY()) < 5
            );
        }

        @Override
        public boolean canStart() {
            return super.canStart() && cooldown-- <= 0;
        }

        @Override
        public void start() {
            this.playtime = this.mob.getRandom().nextInt(600) + 600;
            super.start();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && this.playtime-- > 0;
        }

        @Override
        public void stop() {
            this.cooldown = 100;
            super.stop();
        }
    }
}
