package io.github.lucun.netheriteupgrade.entity.ai;

import com.google.common.base.Predicates;
import io.github.lucun.netheriteupgrade.entity.HoneySlimeEntity;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.EnumSet;
import java.util.function.Predicate;

public class LookNearHiveGoal extends Goal {

    private final HoneySlimeEntity slimeEntity;
    private final int rangeFromHive;
    private final int searchRange;
    private BlockPos hive;
    private BlockPos target;

    public LookNearHiveGoal(HoneySlimeEntity slimeEntity, int rangeFromHive, int searchRange) {
        this.slimeEntity = slimeEntity;
        this.rangeFromHive = rangeFromHive;
        this.searchRange = searchRange;
        this.setControls(EnumSet.of(Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (!validateHive() && !findNearestHive()) {
            return false;
        } else {
            return this.slimeEntity.getBlockPos().getSquaredDistance(target) > this.rangeFromHive * this.rangeFromHive;
        }
    }

    @Override
    public void start() {
        this.slimeEntity.setFindingHive(true);
    }

    @Override
    public void tick() {
        double dx = target.getX() + 0.5 - this.slimeEntity.getX();
        double dz = target.getZ() + 0.5 - this.slimeEntity.getZ();
        double yaw = MathHelper.wrapDegrees((float)(MathHelper.atan2(dz, dx) * 57.2957763671875D) - 90.0F);
        ((SlimeAI.SlimeMoveControl) this.slimeEntity.getMoveControl()).look((float)yaw, true);
    }

    @Override
    public boolean shouldContinue() {

        if (!validateHive() && !findNearestHive()) {
            return false;
        } else {
            return this.slimeEntity.getBlockPos().getSquaredDistance(target) > this.rangeFromHive * rangeFromHive / 7;
        }
    }

    @Override
    public void stop() {
        this.slimeEntity.setFindingHive(false);
    }

    private boolean validateHive() {
        return hive != null && this.slimeEntity.getEntityWorld().getBlockEntity(hive) instanceof BeehiveBlockEntity;
    }

    private boolean findNearestHive() {
        this.hive = ((ServerWorld) this.slimeEntity.getEntityWorld()).getPointOfInterestStorage().getNearestPosition(
                        Predicate.isEqual(PointOfInterestType.BEE_NEST),
                        Predicates.alwaysTrue(),
                        this.slimeEntity.getBlockPos(),
                        this.searchRange,
                        PointOfInterestStorage.OccupationStatus.ANY)
                .map(BlockPos::toImmutable).orElse(null);
        if (this.hive == null) {
            this.target = null;
            return false;
        }
        BlockPos pos = new BlockPos.Mutable(this.hive);
        do {
            pos = pos.down();
        } while (this.slimeEntity.world.getBlockState(pos).getCollisionShape(this.slimeEntity.world, pos).isEmpty());
        this.target = pos.up().toImmutable();
        return true;
    }
}
