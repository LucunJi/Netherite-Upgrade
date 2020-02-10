package io.github.lucun.netheriteupgrade.server.multiblock.pattern;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Patterns {
    private static Map<String, ArrayList<Pattern>> trigger2patternList = Maps.newHashMap();

    public final static Pattern TEST_RSEMITTER;

    static {
        TEST_RSEMITTER = register("test_rsemitter", Blocks.AIR,
                new BlockPos(0,2,0), Blocks.IRON_BARS,
                new BlockPos(0,1,0), Blocks.IRON_BLOCK,
                new BlockPos(0,0,0), Blocks.REDSTONE_BLOCK
                );
    }

    public static Iterator<Pattern> getPatternsByTrigger(Block block) {
        return trigger2patternList.get(((TranslatableText) block.getName()).getKey()).iterator();
    }

    private static Pattern register(String name, Block triggerBlock, Object... poses) {
        Map<BlockPos, Block> map = new HashMap<>(poses.length / 2);
        for (int i = 0; i < poses.length; i+=2) {
            map.put((BlockPos)poses[i], (Block)poses[i+1]);
        }
        Pattern pattern = new Pattern(map, "test_rsemitter", triggerBlock);

        String triggerKey = ((TranslatableText) triggerBlock.getName()).getKey();
        if (!trigger2patternList.containsKey(triggerKey)) {
            trigger2patternList.put(triggerKey, new ArrayList<>());
        }
        trigger2patternList.get(triggerKey).add(pattern);
        return pattern;
    }
}
