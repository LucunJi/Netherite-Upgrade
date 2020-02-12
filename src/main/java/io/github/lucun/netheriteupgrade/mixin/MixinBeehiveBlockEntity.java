package io.github.lucun.netheriteupgrade.mixin;

import io.github.lucun.netheriteupgrade.entity.EntityTypes;
import io.github.lucun.netheriteupgrade.entity.HoneySlimeEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.ChunkRandom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeehiveBlockEntity.class)
public class MixinBeehiveBlockEntity extends BlockEntity {
    public MixinBeehiveBlockEntity(BlockEntityType<?> type) {
        super(type);
    }

    @Shadow public static int getHoneyLevel(BlockState state) {return 0;}

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void onTick(CallbackInfo callbackInfo) {
        //TODO: spawn near beehive in slime chunk with similar restriction to normal slime
        if (this.getHoneyLevel(this.getCachedState()) == 5) {
            BlockPos.iterate(this.getPos().subtract(new BlockPos(5, 5, 5)), this.getPos().add(5, 5, 5)).forEach(pos -> {
                HoneySlimeEntity entity = new HoneySlimeEntity(EntityTypes.HONEY_SLIME, world);
                if (ChunkRandom.create(this.getPos().getX() >> 4, this.getPos().getZ() >> 4, world.getSeed(), 987234911L).nextInt(10) == 0) {
                    this.world.spawnEntity(entity);
                }
            });
        }
    }
}
