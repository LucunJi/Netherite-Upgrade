package io.github.lucun.netheriteupgrade.server.multiblock.pattern;

import net.minecraft.block.Block;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Pattern {

    //TODO use fields for optimization
    private final Set<String> blockKeys;
    private final SubPattern[] patterns = new SubPattern[8];
    private String name;
    
    public Pattern (Map<BlockPos, Block> basicPattern, String name, Block triggerBlock) {
        Dimension dimension = Dimension.fromPatternMap(basicPattern);
        patterns[0] = new SubPattern(dimension, basicPattern);
        patterns[1] = patterns[1].rotate();
        patterns[2] = patterns[2].rotate();
        patterns[3] = patterns[3].rotate();
        patterns[4] = patterns[0].mirror();
        patterns[5] = patterns[4].rotate();
        patterns[6] = patterns[5].rotate();
        patterns[7] = patterns[6].rotate();
        blockKeys = new HashSet<>(basicPattern
                .values()
                .stream()
                .map(block -> ((TranslatableText) block.getName()).getKey())
                .collect(Collectors.toSet()));
    }

    public SubPattern[] getSubPatterns() {
        return patterns;
    }
}
