package io.github.lucun.netheriteupgrade.api.multiblock.pattern;

import net.minecraft.util.math.BlockPos;

public class MatchResult {
    BlockPos origin;
    ISubPattern pattern;

    public MatchResult(BlockPos pos, ISubPattern subPattern) {
        this.origin = pos;
        this.pattern = subPattern;
    }

    public BlockPos getOrigin() {
        return origin;
    }

    public ISubPattern getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        return String.format("MatchResult:{%s}", this.origin);
    }
}