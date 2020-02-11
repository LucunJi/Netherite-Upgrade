package io.github.lucun.netheriteupgrade.api.multiblock.pattern;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

@FunctionalInterface
public interface MatchingAdaptor {
    boolean match(Block patternBlock, BlockState worldBlockState, BlockPos relativePos);
}
