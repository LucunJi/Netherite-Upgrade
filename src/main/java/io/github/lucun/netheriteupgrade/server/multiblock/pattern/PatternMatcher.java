package io.github.lucun.netheriteupgrade.server.multiblock.pattern;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class PatternMatcher {

    public static Optional<MatchResult> matchSubPatterns(World world, BlockPos worldPosBegin, Pattern pattern) {
        Iterator<SubPattern> iterator = pattern.getSubPatternIterator();
        while (iterator.hasNext()) {
            SubPattern subPattern = iterator.next();
            for (Map.Entry<BlockPos, Block> entry : subPattern.getPatternMap().entrySet()) {
                BlockPos relativePosBegin = entry.getKey();
                Block block = entry.getValue();

                if (world.getBlockState(worldPosBegin).getBlock() == block) {
                    Optional<BlockPos> result = matchForOrigin(world, worldPosBegin, relativePosBegin, subPattern);
                    if (result.isPresent()) {
                        return Optional.of(new MatchResult(result.get(), subPattern));
                    }
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<BlockPos> matchForOrigin(World world, BlockPos worldPosBegin, BlockPos relativePosBegin, SubPattern subPattern) {
        BlockPos worldPosOrigin = worldPosBegin.subtract(relativePosBegin);
        for (Pair<BlockPos, Block> pair : subPattern.getPatternList()) {
            BlockPos relativePos = pair.getLeft();
            Block targetBlock = pair.getRight();
            if (world.getBlockState(worldPosOrigin.add(relativePos)).getBlock() != targetBlock) {
                return Optional.empty();
            }
        }

        return Optional.of(worldPosBegin);
    }
}
