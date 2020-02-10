package io.github.lucun.netheriteupgrade.entity;

import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class EntityTypes {
    private static Map<String, Item> types = Maps.newHashMap();

    public static EntityType<FireproofItemContainerEntity> FIREPROOF_CONTAINER;

    static {
        FIREPROOF_CONTAINER = register(
                "fireproof_container", EntityType.Builder.create(
                FireproofItemContainerEntity::new,
                EntityCategory.MISC)
                .makeFireImmune().setDimensions(0.25f, 0.25f),
                (type, factory) -> new ItemEntityRenderer(type, factory.getItemRenderer()));
    }

    private static EntityType register(String name, EntityType.Builder<? extends Entity> builder, EntityRendererRegistry.Factory factory) {
        EntityType<? extends Entity> ret = Registry.register(Registry.ENTITY_TYPE, new Identifier("netheriteupgrade", name), builder.build(name));
        EntityRendererRegistry.INSTANCE.register(ret, factory);
        return ret;
    }
}
