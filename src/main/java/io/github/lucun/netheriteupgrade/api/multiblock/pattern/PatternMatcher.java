package io.github.lucun.netheriteupgrade.api.multiblock.pattern;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Optional;

public class PatternMatcher {

    public static final MatchingAdaptor DEFAULT_ADAPTOR = (patternBlock, worldBlockState, relativePos) -> patternBlock == worldBlockState.getBlock();

    public static Optional<MatchResult> matchSubPatterns(World world, BlockPos worldPosBegin, IPattern pattern) {
        return matchSubPatterns(world, worldPosBegin, pattern, DEFAULT_ADAPTOR);
    }

    public static Optional<MatchResult> matchSubPatterns(World world, BlockPos worldPosBegin, IPattern pattern, MatchingAdaptor adaptor) {
        Iterable<? extends ISubPattern> subPatterns = pattern.getSubPatterns();
        for (ISubPattern subPattern : subPatterns) {
            for (BlockEntry entry : subPattern.blockEntries()) {
                BlockPos relativePosBegin = entry.getBlockPos();
                Block block = entry.getBlock();

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

    public static Optional<BlockPos> matchForOrigin(World world, BlockPos worldPosBegin, BlockPos relativePosBegin, ISubPattern subPattern) {
        return matchForOrigin(world, worldPosBegin, relativePosBegin, subPattern, DEFAULT_ADAPTOR);
    }

    public static Optional<BlockPos> matchForOrigin(World world, BlockPos worldPosBegin, BlockPos relativePosBegin, ISubPattern subPattern, MatchingAdaptor adaptor) {
        BlockPos worldPosOrigin = worldPosBegin.subtract(relativePosBegin);
        for (BlockEntry entry : subPattern.blockEntries()) {
            BlockPos relativePos = entry.getBlockPos();
            Block targetBlock = entry.getBlock();
            if (!adaptor.match(targetBlock, world.getBlockState(worldPosOrigin.add(relativePos)), relativePos)) {
                return Optional.empty();
            }
        }

        return Optional.of(worldPosOrigin);
    }
}
