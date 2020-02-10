package io.github.lucun.netheriteupgrade.blocks;

import com.google.common.collect.Maps;
import io.github.lucun.netheriteupgrade.NetheriteAnvilBlock;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;

import static net.minecraft.block.Blocks.NETHERITE_BLOCK;

public class Blocks {
    private static Map<String, Block> blocks = Maps.newHashMap();

    public static final Block NETHERITE_ANVIL;

    static {
        NETHERITE_ANVIL = BlockManager.add("netherite_anvil", new NetheriteAnvilBlock(FabricBlockSettings.copy(NETHERITE_BLOCK).build()));
    }

    public static class BlockManager {
        public static void register() {
            for (Map.Entry<String, Block> entry : blocks.entrySet()) {
                Registry.register(Registry.BLOCK, new Identifier("netheriteupgrade", entry.getKey()), entry.getValue());
            }
        }

        public static Block add(String name, Block block) {
            blocks.put(name, block);
            return block;
        }
    }
}
