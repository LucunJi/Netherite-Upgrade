package io.github.lucun.netheriteupgrade.api.multiblock.pattern;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PatternHardCoded implements IPattern {

    private final ArrayList<SubPatternHardCoded> subPatterns;
    private String name;
    
    public PatternHardCoded(String name, List<Pair<BlockPos, Block>> basicPattern) {
        subPatterns = Lists.newArrayList();
        subPatterns.add(new SubPatternHardCoded(basicPattern));
        subPatterns.add(subPatterns.get(0).rotate());
        subPatterns.add(subPatterns.get(1).rotate());
        subPatterns.add(subPatterns.get(2).rotate());
        subPatterns.add(subPatterns.get(0).mirror());
        subPatterns.add(subPatterns.get(4).rotate());
        subPatterns.add(subPatterns.get(5).rotate());
        subPatterns.add(subPatterns.get(6).rotate());
        this.name = name;
    }

    public Iterator<SubPatternHardCoded> getSubPatternIterator() {
        return subPatterns.iterator();
    }

    public String getName() {
        return name;
    }
}
