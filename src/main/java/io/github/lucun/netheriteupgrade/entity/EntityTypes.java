package io.github.lucun.netheriteupgrade.entity;

import io.github.lucun.netheriteupgrade.client.render.entity.HoneySlimeRenderer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntityTypes {
    public static EntityType<FireproofItemEntity> FIREPROOF_CONTAINER;
    public static EntityType<HoneySlimeEntity> HONEY_SLIME;

    static {
        FIREPROOF_CONTAINER = register(
                "item_fireproof", EntityType.Builder.create(
                FireproofItemEntity::new,
                EntityCategory.MISC)
                .makeFireImmune().setDimensions(0.25f, 0.25f),
                (type, factory) -> new ItemEntityRenderer(type, factory.getItemRenderer()));
        HONEY_SLIME = register(
                "honey_slime", EntityType.Builder.create(
                        HoneySlimeEntity::new,
                        EntityCategory.MISC)
                .setDimensions(2.0f, 2.04f),
                (type, factory) -> new HoneySlimeRenderer(type));
    }

    private static EntityType register(String name, EntityType.Builder<? extends Entity> builder, EntityRendererRegistry.Factory factory) {
        EntityType<? extends Entity> ret = Registry.register(Registry.ENTITY_TYPE, new Identifier("netheriteupgrade", name), builder.build(name));
        EntityRendererRegistry.INSTANCE.register(ret, factory);
        return ret;
    }
}
