package io.github.lucun.netheriteupgrade.api.multiblock.pattern;

import net.minecraft.util.math.BlockPos;

public class MatchResult {
    BlockPos origin;
    SubPatternHardCoded pattern;

    public MatchResult(BlockPos pos, SubPatternHardCoded subPattern) {
        this.origin = pos;
        this.pattern = subPattern;
    }

    public BlockPos getOrigin() {
        return origin;
    }

    public SubPatternHardCoded getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        return String.format("MatchResult:{%s}", this.origin);
    }
}