package io.github.lucun.netheriteupgrade.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static net.minecraft.block.Blocks.NETHERITE_BLOCK;

public class Blocks {

    public static final Block NETHERITE_ANVIL;
    public static final Block WORKSHOP;

    static {
        NETHERITE_ANVIL = register("netherite_anvil", new NetheriteAnvilBlock(FabricBlockSettings.copy(NETHERITE_BLOCK).build()));
        WORKSHOP = register("workshop", new WorkshopFunctionalBlock(FabricBlockSettings.of(Material.STONE).build()));
    }

    public static Block register(String name, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier("netheriteupgrade", name), block);
    }
}
