package io.github.lucun.netheriteupgrade;

import io.github.lucun.netheriteupgrade.block.Blocks;
import io.github.lucun.netheriteupgrade.entity.EntityTypes;
import io.github.lucun.netheriteupgrade.item.Items;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main implements ModInitializer {

	public static final Logger LOGGER = LogManager.getLogger();
    private static MinecraftClient mc = MinecraftClient.getInstance();

	@Override
	public void onInitialize() {
		new Blocks();
		new Items();
		new EntityTypes();
		LogManager.getLogger().info("Spend hours for your netherite tools!");
	}
}
