package io.github.lucun.netheriteupgrade.blocks;

import io.github.lucun.netheriteupgrade.NetheriteAnvilBlock;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.minecraft.block.Blocks.NETHERITE_BLOCK;

public class Blocks {

    public static final Block NETHERITE_ANVIL;

    static {
        NETHERITE_ANVIL = register("netherite_anvil", new NetheriteAnvilBlock(FabricBlockSettings.copy(NETHERITE_BLOCK).build()));
    }

    public static Block register(String name, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier("netheriteupgrade", name), block);
    }
}
