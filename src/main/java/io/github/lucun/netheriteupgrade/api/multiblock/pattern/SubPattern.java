package io.github.lucun.netheriteupgrade.api.multiblock.pattern;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubPattern {
    private final List<Pair<BlockPos, Block>> patternList;
    private final Map<BlockPos, Block> patternMap;
    private final Map<String, ArrayList<BlockPos>> key2posListMap;

    public SubPattern (List<Pair<BlockPos, Block>> patternList) {
        this.patternList = patternList;
        key2posListMap = Maps.newHashMap();
        this.patternMap = Maps.newHashMap();
        patternList.forEach(pair -> {
            patternMap.put(pair.getLeft(), pair.getRight());
            String key = ((TranslatableText) pair.getRight().getName()).getKey();
            if (!key2posListMap.containsKey(key)) {
                key2posListMap.put(key, Lists.newArrayList());
            }
            key2posListMap.get(key).add(pair.getLeft());
        });

    }

    public SubPattern mirror() {
        List<Pair<BlockPos, Block>> newList = new ArrayList<>(patternList.size());
        for (Pair<BlockPos, Block> pair : patternList) {
            BlockPos pos = pair.getLeft();
            Block block = pair.getRight();
            newList.add(new Pair<>(new BlockPos(-pos.getX(), pos.getY(), pos.getZ()), block));
        }
        return new SubPattern(newList);
    }

    public SubPattern rotate() {
        List<Pair<BlockPos, Block>> newList = new ArrayList<>(patternList.size());
        for (Pair<BlockPos, Block> pair : patternList) {
            BlockPos pos = pair.getLeft();
            Block block = pair.getRight();
            newList.add(new Pair<>(new BlockPos(-pos.getZ(), pos.getY(), pos.getX()), block));
        }
        return new SubPattern(newList);
    }

    public Map<BlockPos, Block> getPatternMap() {
        return patternMap;
    }

    public List<Pair<BlockPos, Block>> getPatternList() {
        return patternList;
    }

    public Map<String, ArrayList<BlockPos>> getKey2posListMap() {
        return key2posListMap;
    }
}
