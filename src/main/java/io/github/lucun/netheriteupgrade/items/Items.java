package io.github.lucun.netheriteupgrade.items;

import com.google.common.collect.Maps;
import io.github.lucun.netheriteupgrade.blocks.Blocks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class Items {
    private static Map<String, Item> items = Maps.newHashMap();

    public static Item NETHERITE_ANVIL;

    static {
        NETHERITE_ANVIL = ItemManager.addBlockItem(Blocks.NETHERITE_ANVIL);
    }

    public static class ItemManager {
        public static void register() {
            for (Map.Entry<String, Item> entry : items.entrySet()) {
                Registry.register(Registry.ITEM, new Identifier("netheriteupgrade", entry.getKey()), entry.getValue());
            }
        }

        public static Item addBlockItem(Block block) {
            Item item = new BlockItem(Blocks.NETHERITE_ANVIL, new Item.Settings().group(ItemGroup.DECORATIONS));
            String[] keyElements = ((TranslatableText) block.getName()).getKey().split("\\.");
            items.put(keyElements[keyElements.length-1], item);
            return item;
        }

        public static Item add(String name, Item item) {
            items.put(name, item);
            return item;
        }
    }
}
