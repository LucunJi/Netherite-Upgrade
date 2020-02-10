package io.github.lucun.netheriteupgrade;

import io.github.lucun.netheriteupgrade.blocks.Blocks;
import io.github.lucun.netheriteupgrade.entity.FireproofItemContainerEntity;
import io.github.lucun.netheriteupgrade.items.Items;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;

public class Main implements ModInitializer {

	private static MinecraftClient mc = MinecraftClient.getInstance();

	public static EntityType<FireproofItemContainerEntity> FIREPROOF_CONTAINER;

	@Override
	public void onInitialize() {
		Blocks.BlockManager.register();
		Items.ItemManager.register();

		FIREPROOF_CONTAINER = Registry.register(
				Registry.ENTITY_TYPE,
				new Identifier("netheriteupgrade", "fireproof_container"), EntityType.Builder.create(
				FireproofItemContainerEntity::new,
				EntityCategory.MISC)
				.makeFireImmune()
				.setDimensions(0.25f, 0.25f).build("fireproof_container"));
		EntityRendererRegistry.INSTANCE.register(FIREPROOF_CONTAINER, (type, factory) -> new ItemEntityRenderer(type, factory.getItemRenderer()));

		LogManager.getLogger().info("Spend hours for your netherite tools!");
	}
}
