package io.github.lucun.netheriteupgrade.api.multiblock.pattern;

import net.minecraft.util.math.BlockPos;

public class MatchResult {
    BlockPos origin;
    SubPattern pattern;

    public MatchResult(BlockPos pos, SubPattern subPattern) {
        this.origin = pos;
        this.pattern = subPattern;
    }

    public BlockPos getOrigin() {
        return origin;
    }

    public SubPattern getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        return String.format("MatchResult:{%s}", this.origin);
    }
}