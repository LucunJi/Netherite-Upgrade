package io.github.lucun.netheriteupgrade.server.multiblock.pattern;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Pattern {

    //TODO use fields for optimization
    private final ArrayList<SubPattern> subPatterns;
    private String name;
    
    public Pattern (String name, List<Pair<BlockPos, Block>> basicPattern) {
        subPatterns = Lists.newArrayList();
        subPatterns.add(new SubPattern(basicPattern));
        subPatterns.set(1, subPatterns.get(1).rotate());
        subPatterns.set(2, subPatterns.get(2).rotate());
        subPatterns.set(3, subPatterns.get(3).rotate());
        subPatterns.set(4, subPatterns.get(0).mirror());
        subPatterns.set(5, subPatterns.get(4).rotate());
        subPatterns.set(6, subPatterns.get(5).rotate());
        subPatterns.set(7, subPatterns.get(6).rotate());
        this.name = name;
    }

    public Iterator<SubPattern> getSubPatternIterator() {
        return subPatterns.iterator();
    }
}
