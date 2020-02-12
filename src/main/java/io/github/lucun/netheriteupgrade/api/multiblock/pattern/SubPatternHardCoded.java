package io.github.lucun.netheriteupgrade.api.multiblock.pattern;

import com.google.common.collect.Iterators;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class SubPatternHardCoded implements ISubPattern {
    private final BlockEntry[] blocks;

    public SubPatternHardCoded(List<BlockEntry> blockList) {
        blocks = (blockList.toArray(new BlockEntry[0]));
    }

    public SubPatternHardCoded mirror() {
        List<BlockEntry> newList = new ArrayList<>(blocks.length);
        for (BlockEntry blockEntry : blocks) {
            BlockPos pos = blockEntry.getBlockPos();
            Block block = blockEntry.getBlock();
            newList.add(new BlockEntry(new BlockPos(-pos.getX(), pos.getY(), pos.getZ()), block));
        }
        return new SubPatternHardCoded(newList);
    }

    @Override
    public Iterator<BlockEntry> blockIterator() {
        return Iterators.forArray(blocks);
    }

    public SubPatternHardCoded rotate() {
        List<BlockEntry> newList = new ArrayList<>(blocks.length);
        for (BlockEntry blockEntry : blocks) {
            BlockPos pos = blockEntry.getBlockPos();
            Block block = blockEntry.getBlock();
            newList.add(new BlockEntry(new BlockPos(-pos.getZ(), pos.getY(), pos.getX()), block));
        }
        return new SubPatternHardCoded(newList);
    }
}
