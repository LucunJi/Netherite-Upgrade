package io.github.lucun.netheriteupgrade.api.multiblock.pattern;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public final class BlockEntry {
    private final BlockPos blockPos;
    private final Block block;

    public BlockEntry(BlockPos blockPos, Block block) {
        this.blockPos = blockPos;
        this.block = block;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    @Override
    public String toString() {
        return "BlockEntry{" +
                "blockPos=" + blockPos +
                ", block=" + block +
                '}';
    }

    public Block getBlock() {
        return block;
    }
}
