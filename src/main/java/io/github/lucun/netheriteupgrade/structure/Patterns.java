package io.github.lucun.netheriteupgrade.structure;

import com.google.common.collect.Lists;
import io.github.lucun.netheriteupgrade.api.multiblock.pattern.Pattern;
import net.minecraft.block.Blocks;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

public class Patterns {
    public final static Pattern TEST;

    static {
        TEST = new Pattern("test", Lists.newArrayList(
                new Pair<>(new BlockPos(0, 0, 1), Blocks.RED_CONCRETE),
                new Pair<>(new BlockPos(1, 0, 0), Blocks.BLUE_CONCRETE),
                new Pair<>(new BlockPos(0, 1, 0), Blocks.GREEN_CONCRETE),
                new Pair<>(new BlockPos(0, 0, 0), Blocks.WHITE_CONCRETE)
        ));
    }
}
