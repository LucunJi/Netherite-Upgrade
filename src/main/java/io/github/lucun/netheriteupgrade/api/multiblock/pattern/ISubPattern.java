package io.github.lucun.netheriteupgrade.api.multiblock.pattern;

import java.util.Iterator;

public interface ISubPattern {
    ISubPattern rotate();
    ISubPattern mirror();

    Iterable<BlockEntry> blockEntries();

    String getPatternName();
}
