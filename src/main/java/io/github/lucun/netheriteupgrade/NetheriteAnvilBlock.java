package io.github.lucun.netheriteupgrade;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.container.NameableContainerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NetheriteAnvilBlock extends AnvilBlock {
    public NetheriteAnvilBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            player.openContainer(state.createContainerFactory(world, pos));
            player.incrementStat(Stats.INTERACT_WITH_ANVIL);
            return ActionResult.SUCCESS;
        }
    }

    @Override
    public NameableContainerFactory createContainerFactory(BlockState state, World world, BlockPos pos) {
        return null;
    }
}
