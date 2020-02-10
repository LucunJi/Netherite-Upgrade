package io.github.lucun.netheriteupgrade.server.multiblock.pattern;

import net.minecraft.util.math.BlockPos;

public class MatchResult {
    BlockPos origin;
    SubPattern pattern;

    protected MatchResult(BlockPos pos, SubPattern subPattern) {
        this.origin = pos;
        this.pattern = subPattern;
    }
}