package io.github.lucun.netheriteupgrade.mixin;

import io.github.lucun.netheriteupgrade.entity.spawn.HoneySlimeSpawnHelper;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeehiveBlockEntity.class)
public class MixinBeehiveBlockEntity extends BlockEntity {
    public MixinBeehiveBlockEntity(BlockEntityType<?> type) {
        super(type);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void onTick(CallbackInfo callbackInfo) {
        HoneySlimeSpawnHelper.spawn(this);
    }
}
