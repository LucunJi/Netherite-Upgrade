package io.github.lucun.netheriteupgrade.mixin;

import io.github.lucun.netheriteupgrade.Main;
import io.github.lucun.netheriteupgrade.api.multiblock.pattern.MatchResult;
import io.github.lucun.netheriteupgrade.api.multiblock.pattern.PatternMatcher;
import io.github.lucun.netheriteupgrade.structure.Patterns;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld extends World {

    protected MixinServerWorld(LevelProperties levelProperties, DimensionType dimensionType, BiFunction<World, Dimension, ChunkManager> chunkManagerProvider, Supplier<Profiler> supplier, boolean isClient) {
        super(levelProperties, dimensionType, chunkManagerProvider, supplier, isClient);
    }

    @Inject(method = "onBlockChanged", at = @At(
            value = "HEAD"
    ))
    private void onBlockPlacementSuccess(BlockPos pos, BlockState oldBlock, BlockState newBlock, CallbackInfo callbackInfo) {
        Optional<MatchResult> result = PatternMatcher.matchSubPatterns(this, pos, Patterns.TEST);
        Main.LOGGER.info(
                (this.isClient() ? "Client" : "Server") +
                        ": Try to match pattern " +
                        Patterns.TEST.getName() + " -- " +
                result);
        result.ifPresent(r -> r
                .getPattern()
                .blockEntries()
                .forEach(entry ->
                        this.setBlockState(
                                entry.getBlockPos().add(r.getOrigin()),
                                Blocks.IRON_BLOCK.getDefaultState())));
    }
}
