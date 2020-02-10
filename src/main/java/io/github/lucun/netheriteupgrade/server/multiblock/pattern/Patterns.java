package io.github.lucun.netheriteupgrade.server.multiblock.pattern;

import com.google.common.collect.Lists;
import net.minecraft.block.Blocks;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

public class Patterns {
    public final static Pattern TEST_RSEMITTER;

    static {
        TEST_RSEMITTER = new Pattern("test_rsemitter", Lists.newArrayList(
                new Pair<>(new BlockPos(0, 2, 0), Blocks.IRON_BARS),
                new Pair<>(new BlockPos(0, 1, 0), Blocks.IRON_BLOCK),
                new Pair<>(new BlockPos(0, 0, 0), Blocks.REDSTONE_BLOCK)
        ));
    }
}
