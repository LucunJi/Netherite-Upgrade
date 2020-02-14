package io.github.lucun.netheriteupgrade.api.multiblock.pattern;

import com.google.common.collect.Iterators;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SubPatternHardCoded implements ISubPattern {
    private final BlockEntry[] blocks;
    private final String patternName;
    private final int rotation;
    private final boolean mirrored;

    public SubPatternHardCoded(List<BlockEntry> blockList, String patternName) {
        blocks = (blockList.toArray(new BlockEntry[0]));
        this.patternName = patternName;
        this.rotation = 0;
        this.mirrored = false;
    }

    public SubPatternHardCoded(List<BlockEntry> blockList, String patternName, int rotation, boolean mirrored) {
        blocks = (blockList.toArray(new BlockEntry[0]));
        this.patternName = patternName;
        this.rotation = rotation;
        this.mirrored = mirrored;
    }

    public SubPatternHardCoded mirror() {
        List<BlockEntry> newList = new ArrayList<>(blocks.length);
        for (BlockEntry blockEntry : blocks) {
            BlockPos pos = blockEntry.getBlockPos();
            Block block = blockEntry.getBlock();
            newList.add(new BlockEntry(new BlockPos(-pos.getX(), pos.getY(), pos.getZ()), block));
        }
        return new SubPatternHardCoded(newList, this.patternName, this.rotation, !this.mirrored);
    }

    @Override
    public Iterable<BlockEntry> blockEntries() {
        return Arrays.asList(blocks);
    }

    @Override
    public String getPatternName() {
        return patternName;
    }

    public SubPatternHardCoded rotate() {
        List<BlockEntry> newList = new ArrayList<>(blocks.length);
        for (BlockEntry blockEntry : blocks) {
            BlockPos pos = blockEntry.getBlockPos();
            Block block = blockEntry.getBlock();
            newList.add(new BlockEntry(new BlockPos(-pos.getZ(), pos.getY(), pos.getX()), block));
        }
        return new SubPatternHardCoded(newList, this.patternName, this.rotation + 90, this.mirrored);
    }
}
