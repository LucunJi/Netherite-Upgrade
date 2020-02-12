package io.github.lucun.netheriteupgrade.client.render.entity;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.SlimeEntityRenderer;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.Identifier;

public class HoneySlimeRenderer extends SlimeEntityRenderer {
    private static final Identifier SKIN = new Identifier("netheriteupgrade:textures/entity/slime/honey_slime.png");

    public HoneySlimeRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    @Override
    public Identifier getTexture(SlimeEntity slimeEntity) {
        return SKIN;
    }
}
