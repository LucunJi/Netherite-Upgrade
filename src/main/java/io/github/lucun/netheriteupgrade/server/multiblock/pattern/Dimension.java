package io.github.lucun.netheriteupgrade.server.multiblock.pattern;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class Dimension{
    private final int minX;
    private final int minY;
    private final int minZ;
    private final int maxX;
    private final int maxY;
    private final int maxZ;

    private Dimension (int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public static Dimension fromPatternMap(Map<BlockPos, Block> patternMap) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;
        for (Map.Entry<BlockPos, Block> entry : patternMap.entrySet()) {
            BlockPos pos = entry.getKey();
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            if (minX > x) minX = x;
            if (maxX < x) maxX = x;
            if (minY > y) minY = y;
            if (maxY < y) maxY = y;
            if (minZ > z) minZ = z;
            if (maxZ < z) maxZ = z;
        }
        return new Dimension(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public Dimension mirror(){
        return new Dimension(-maxX, minY, minZ, -minX, maxY, maxZ);
    }

    public Dimension rotate(){
        return new Dimension(-maxZ, minY, minX, -minZ ,maxY ,maxX);
    }

    public boolean contains(BlockPos pos) {
        return !(minX > pos.getX() || pos.getX() > maxX || minY > pos.getY() || pos.getY() > maxY || minZ > pos.getZ() || pos.getZ() > maxZ);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Dimension)) return false;
        Dimension dim = (Dimension)obj;
        return this.minX == dim.minX && this.minY == dim.minX && this.minY == dim.minY && this.maxX == dim.maxX && this.maxY == dim.maxY && this.maxZ == dim.maxZ;
    }
}