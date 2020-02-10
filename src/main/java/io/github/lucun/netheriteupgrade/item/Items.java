package io.github.lucun.netheriteupgrade.item;

import io.github.lucun.netheriteupgrade.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Items {

    public static Item NETHERITE_ANVIL;

    static {
        NETHERITE_ANVIL = register("netherite_anvil", new BlockItem(Blocks.NETHERITE_ANVIL, new Item.Settings().group(ItemGroup.MISC)));
    }

    public static Item register(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier("netheriteupgrade", name), item);
    }
}
