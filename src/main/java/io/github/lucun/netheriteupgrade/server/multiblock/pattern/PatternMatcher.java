package io.github.lucun.netheriteupgrade.server.multiblock.pattern;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

import static java.util.Optional.of;

public class PatternMatcher {
    public static void match(World world, BlockPos worldPosBegin) {
        Block block = world.getBlockState(worldPosBegin).getBlock();
        Iterator<Pattern> iterator = Patterns.getPatternsByTrigger(block);
        while (iterator.hasNext()) {
            Pattern pattern = iterator.next();
            Optional<MatchResult> result = matchSubPatterns(world, worldPosBegin, pattern);
        }
        iterator = Patterns.getPatternsByTrigger(Blocks.AIR);
        while (iterator.hasNext()) {
            Pattern pattern = iterator.next();
            Optional<MatchResult> result = matchSubPatterns(world, worldPosBegin, pattern);
        }
    }

    private static Optional<MatchResult> matchSubPatterns(World world, BlockPos worldPosBegin, Pattern pattern) {
        for (SubPattern subPattern : pattern.getSubPatterns()) {
            for (Map.Entry<BlockPos, Block> entry : subPattern.getPatternMap().entrySet()) {
                BlockPos relativePosBegin = entry.getKey();
                Block block = entry.getValue();

                if (world.getBlockState(worldPosBegin).getBlock() == block) {
                    Optional<BlockPos> result = matchForOrigin(world, worldPosBegin, relativePosBegin, subPattern);
                    if (result.isPresent()) {
                        return Optional.of(new MatchResult(result.get(), subPattern));
                    }
                }
            }
        }
        return Optional.empty();
    }

    private static Optional<BlockPos> matchForOrigin(World world, BlockPos worldPosBegin, BlockPos relativePosBegin, SubPattern subPattern) {
        //        System.out.println("======try======");
        boolean failed = false;
        Map<BlockPos, Block> patternMap = subPattern.getPatternMap();
        Dimension dimension = subPattern.getDimension();
        Queue<Pair<BlockPos, BlockPos>> queue = new LinkedList<>();
        Set<BlockPos> closeSetRelative = new HashSet<>();
        queue.add(new Pair<>(worldPosBegin, relativePosBegin));
        while (!queue.isEmpty()) {
            Pair<BlockPos, BlockPos> currentPair = queue.poll();
            BlockPos currentWorldPos = currentPair.getLeft();
            BlockPos currentRelativePos = currentPair.getRight();

            if (world.getBlockState(currentWorldPos).getBlock() != patternMap.get(currentRelativePos)) {
                failed = true;
                break;
            }

            closeSetRelative.add(currentRelativePos);

            //add neighbors
            {
                BlockPos up = currentRelativePos.up();
                if (dimension.contains(up) && !closeSetRelative.contains(up)) {
                    queue.add(new Pair<>(currentWorldPos.up(), up));
                    closeSetRelative.add(currentRelativePos);
                }
                BlockPos down = currentRelativePos.down();
                if (dimension.contains(down) && !closeSetRelative.contains(down)) {
                    queue.add(new Pair<>(currentWorldPos.down(), down));
                    closeSetRelative.add(currentRelativePos);
                }
                BlockPos east = currentRelativePos.east();
                if (dimension.contains(east) && !closeSetRelative.contains(east)) {
                    queue.add(new Pair<>(currentWorldPos.east(), east));
                    closeSetRelative.add(currentRelativePos);
                }
                BlockPos west = currentRelativePos.west();
                if (dimension.contains(west) && !closeSetRelative.contains(west)) {
                    queue.add(new Pair<>(currentWorldPos.west(), west));
                    closeSetRelative.add(currentRelativePos);
                }
                BlockPos south = currentRelativePos.south();
                if (dimension.contains(south) && !closeSetRelative.contains(south)) {
                    queue.add(new Pair<>(currentWorldPos.south(), south));
                    closeSetRelative.add(currentRelativePos);
                }
                BlockPos north = currentRelativePos.north();
                if (dimension.contains(north) && !closeSetRelative.contains(north)) {
                    queue.add(new Pair<>(currentWorldPos.north(), north));
                    closeSetRelative.add(currentRelativePos);
                }
            }
//                    System.out.print(currentWorldPos + " " + currentRelativePos + " [");
//                    for (Pair<BlockPos, BlockPos> pair : queue) {
//                        System.out.print("{" + pair.getLeft() + ", " + pair.getRight() + "}, ");
//                    }
//                    System.out.println();
        }

        return failed ? Optional.empty() : of(worldPosBegin.subtract(relativePosBegin));
    }
}
