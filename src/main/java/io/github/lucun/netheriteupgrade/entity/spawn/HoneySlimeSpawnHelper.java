package io.github.lucun.netheriteupgrade.entity.spawn;

import io.github.lucun.netheriteupgrade.entity.EntityTypes;
import io.github.lucun.netheriteupgrade.entity.HoneySlimeEntity;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.ai.goal.WanderAroundPointOfInterestGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkRandom;

public class HoneySlimeSpawnHelper {
    public static void spawn(BlockEntity hiveEntity) {
        World world = hiveEntity.getWorld();

        if (!world.isClient()) {
            BlockPos hivePos = hiveEntity.getPos();
            BlockState hiveState = world.getBlockState(hivePos);

            if (hasSlimeAround(world, hivePos)) return;

            if (BeehiveBlockEntity.getHoneyLevel(hiveState) == 5 && world.getRandom().nextInt(20 * 60 * 20) == 0) {
                for (BlockPos spawnPos : BlockPos.iterate(hivePos.subtract(new BlockPos(3, 5, 3)), hivePos.add(3, 0, 3))) {
                    if (ChunkRandom.create(hivePos.getX() >> 4, hivePos.getZ() >> 4, world.getSeed(), 987234911L).nextInt(10) == 0) {
                        if (// if NO_RESTRICTIONS, return true, else, it should be in world border, and of non-null EntityType
                            // if ON_GROUND, light level < 14 and isSideSolidFullSquare(view, pos, Direction.UP) and isClearForSpawn
                            // if IN_WATER, there should be water and is not a SimpleFullBlock
                                SpawnHelper.canSpawn(SpawnRestriction.Location.ON_GROUND, world, spawnPos, EntityTypes.HONEY_SLIME)) {

                            HoneySlimeEntity slime = new HoneySlimeEntity(EntityTypes.HONEY_SLIME, world);
                            slime.refreshPositionAndAngles(spawnPos, world.getRandom().nextFloat() * 360, 0.0F);

                            if (// if bounding box overlaps with fluid or other entities
                                    slime.canSpawn(world) &&
                                            // true for non-pathfinding mobs, otherwise checks getPathfindingFavor()
                                            slime.canSpawn(world, SpawnType.NATURAL) &&
                                            // straightforward
                                            world.getBlockState(spawnPos.down()).hasSolidTopSurface(world, spawnPos, slime) &&
                                            // if bounding box overlaps with blocks
                                            world.doesNotCollide(slime)) {

                                slime.initialize(world, world.getLocalDifficulty(hivePos), SpawnType.NATURAL, null, null);
                                world.spawnEntity(slime);
                                world.setBlockState(hivePos, hiveState.with(BeehiveBlock.HONEY_LEVEL, 1));
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean hasSlimeAround(World world, BlockPos hivePos) {
        return world.getEntities(
                EntityTypes.HONEY_SLIME,
                new Box(hivePos.subtract(new BlockPos(8, 16, 8)), hivePos.add(8, 16, 8)),
                slime -> slime.getHoneyStorage() < 15)
                .size() > 0;
    }
}
