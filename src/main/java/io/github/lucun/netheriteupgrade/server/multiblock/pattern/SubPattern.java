package io.github.lucun.netheriteupgrade.server.multiblock.pattern;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class SubPattern {
    private final Dimension dimension;
    private final Map<BlockPos, Block> patternMap;

    public SubPattern (Dimension dimension, Map<BlockPos, Block> patternMap) {
        this.dimension = Dimension.fromPatternMap(patternMap);
        this.patternMap = patternMap;
    }

    public SubPattern mirror() {
        Map<BlockPos, Block> map = new HashMap<>(patternMap.size());
        for (Map.Entry<BlockPos, Block> entry : patternMap.entrySet()) {
            BlockPos pos = entry.getKey();
            Block block = entry.getValue();
            map.put(new BlockPos(-pos.getX(), pos.getY(), pos.getZ()), block);
        }
        return new SubPattern(dimension.mirror(), map);
    }

    public SubPattern rotate() {
        Map<BlockPos, Block> map = new HashMap<>(patternMap.size());
        for (Map.Entry<BlockPos, Block> entry : patternMap.entrySet()) {
            BlockPos pos = entry.getKey();
            Block block = entry.getValue();
            map.put(new BlockPos(-pos.getZ(), pos.getY(), pos.getX()), block);
        }
        return new SubPattern(dimension.rotate(), map);
    }

    public Map<BlockPos, Block> getPatternMap() {
        return patternMap;
    }

    public Dimension getDimension() {
        return dimension;
    }
}
