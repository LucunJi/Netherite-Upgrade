package io.github.lucun.netheriteupgrade.api.multiblock.pattern;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class PatternHardCoded implements IPattern {

    private final SubPatternHardCoded[] subPatterns;
    private String name;
    
    public PatternHardCoded(String name, List<BlockEntry> basicPattern) {
        List<SubPatternHardCoded> subPatterns = Lists.newArrayList();
        subPatterns.add(new SubPatternHardCoded(basicPattern, name));
        subPatterns.add(subPatterns.get(0).rotate());
        subPatterns.add(subPatterns.get(1).rotate());
        subPatterns.add(subPatterns.get(2).rotate());
        subPatterns.add(subPatterns.get(0).mirror());
        subPatterns.add(subPatterns.get(4).rotate());
        subPatterns.add(subPatterns.get(5).rotate());
        subPatterns.add(subPatterns.get(6).rotate());
        this.subPatterns = subPatterns.toArray(new SubPatternHardCoded[8]);
        this.name = name;
    }

    @Override
    public Iterable<SubPatternHardCoded> getSubPatterns() {
        return Arrays.asList(subPatterns);
    }

    @Override
    public ISubPattern getSubPattern(int rotation, boolean mirrored) {
        return subPatterns[(rotation % 360) / 90 + (mirrored ? 4 : 0)];
    }

    public String getName() {
        return name;
    }
}
