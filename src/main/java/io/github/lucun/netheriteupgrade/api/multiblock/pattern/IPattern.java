package io.github.lucun.netheriteupgrade.api.multiblock.pattern;

public interface IPattern {
    Iterable<? extends ISubPattern> getSubPatterns();
    ISubPattern getSubPattern(int rotation, boolean mirrored);
    String getName();
}
