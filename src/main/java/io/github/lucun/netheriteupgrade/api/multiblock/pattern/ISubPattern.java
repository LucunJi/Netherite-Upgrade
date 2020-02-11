package io.github.lucun.netheriteupgrade.api.multiblock.pattern;

import net.minecraft.block.Block;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;

public interface ISubPattern {
    ISubPattern rotate();
    ISubPattern mirror();

    Map<BlockPos, Block> getPatternMap();
    List<Pair<BlockPos, Block>> getPatternList();
}
