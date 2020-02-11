package io.github.lucun.netheriteupgrade.api.multiblock;

import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Occupier {
    private Map<BlockPos, IMultiblockController> occupierMap;
    private Set<BlockPos> exclusiveMap;
    private Map<BlockPos, List<BlockPos>> possessionMap;

    public void possess(IMultiblockController multiblockEntity) {

    }

    public void release(IMultiblockController multiblockEntity) {

    }

    public boolean isExcluded(BlockPos blockPos) {
        return exclusiveMap.contains(blockPos);
    }
}
