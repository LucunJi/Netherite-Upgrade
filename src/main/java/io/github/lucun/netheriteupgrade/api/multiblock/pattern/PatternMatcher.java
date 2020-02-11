package io.github.lucun.netheriteupgrade.api.multiblock.pattern;

import net.minecraft.block.Block;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class PatternMatcher {

    public static final MatchingAdaptor DEFAULT_ADAPTOR = (patternBlock, worldBlockState, relativePos) -> patternBlock == worldBlockState.getBlock();

    public static Optional<MatchResult> matchSubPatterns(World world, BlockPos worldPosBegin, PatternHardCoded pattern) {
        return matchSubPatterns(world, worldPosBegin, pattern, DEFAULT_ADAPTOR);
    }

    public static Optional<MatchResult> matchSubPatterns(World world, BlockPos worldPosBegin, PatternHardCoded pattern, MatchingAdaptor adaptor) {
        Iterator<SubPatternHardCoded> iterator = pattern.getSubPatternIterator();
        while (iterator.hasNext()) {
            SubPatternHardCoded subPattern = iterator.next();
            for (Map.Entry<BlockPos, Block> entry : subPattern.getPatternMap().entrySet()) {
                BlockPos relativePosBegin = entry.getKey();
                Block block = entry.getValue();

                if (adaptor.match(block, world.getBlockState(worldPosBegin), relativePosBegin)) {
                    Optional<BlockPos> result = matchForOrigin(world, worldPosBegin, relativePosBegin, subPattern, adaptor);
                    if (result.isPresent()) {
                        return Optional.of(new MatchResult(result.get(), subPattern));
                    }
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<BlockPos> matchForOrigin(World world, BlockPos worldPosBegin, BlockPos relativePosBegin, SubPatternHardCoded subPattern) {
        return matchForOrigin(world, worldPosBegin, relativePosBegin, subPattern, DEFAULT_ADAPTOR);
    }

    public static Optional<BlockPos> matchForOrigin(World world, BlockPos worldPosBegin, BlockPos relativePosBegin, SubPatternHardCoded subPattern, MatchingAdaptor adaptor) {
        BlockPos worldPosOrigin = worldPosBegin.subtract(relativePosBegin);
        for (Pair<BlockPos, Block> pair : subPattern.getPatternList()) {
            BlockPos relativePos = pair.getLeft();
            Block targetBlock = pair.getRight();
            if (!adaptor.match(targetBlock, world.getBlockState(worldPosOrigin.add(relativePos)), relativePos)) {
                return Optional.empty();
            }
        }

        return Optional.of(worldPosOrigin);
    }
}
