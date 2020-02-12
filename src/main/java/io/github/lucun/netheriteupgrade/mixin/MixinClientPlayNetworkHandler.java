package io.github.lucun.netheriteupgrade.mixin;

import io.github.lucun.netheriteupgrade.entity.EntityTypes;
import io.github.lucun.netheriteupgrade.entity.FireproofItemEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.sound.RidingMinecartSoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler implements ClientPlayPacketListener {

    @Shadow private MinecraftClient client;

    @Shadow private ClientWorld world;

    @Inject(method = "onEntitySpawn", at = @At("HEAD"))
    private void onEntitySpawnPacket(EntitySpawnS2CPacket packet, CallbackInfo callbackInfo) {
        NetworkThreadUtils.forceMainThread(packet, this, this.client);
        double x = packet.getX();
        double y = packet.getY();
        double z = packet.getZ();
        EntityType<?> entityType = packet.getEntityTypeId();
        Entity entity = null;
        if (entityType == EntityTypes.FIREPROOF_CONTAINER) {
            entity = FireproofItemEntity.make(world, x, y, z);
        }

        if (entity != null) {
            int i = packet.getId();
            entity.updateTrackedPosition(x, y, z);
            entity.pitch = (float)(packet.getPitch() * 360) / 256.0F;
            entity.yaw = (float)(packet.getYaw() * 360) / 256.0F;
            entity.setEntityId(i);
            entity.setUuid(packet.getUuid());
            this.world.addEntity(i, entity);
            if (entity instanceof AbstractMinecartEntity) {
                this.client.getSoundManager().play(new RidingMinecartSoundInstance((AbstractMinecartEntity)entity));
            }
        }
    }
}
