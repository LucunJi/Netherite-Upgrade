package io.github.lucun.netheriteupgrade.mixin;

import io.github.lucun.netheriteupgrade.entity.EntityTypes;
import io.github.lucun.netheriteupgrade.entity.HoneySlimeEntity;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.SpawnType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.ChunkRandom;
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
        if (!this.world.isClient()) {
            if (BeehiveBlockEntity.getHoneyLevel(this.getCachedState()) == 5 && this.world.random.nextInt(20 * 60 * 20) == 0) {
                for (BlockPos blockPos : BlockPos.iterate(this.getPos().subtract(new BlockPos(3, 5, 3)), this.getPos().add(3, 0, 3))) {
                    if (ChunkRandom.create(this.getPos().getX() >> 4, this.getPos().getZ() >> 4, world.getSeed(), 987234911L).nextInt(10) == 0) {

                        HoneySlimeEntity slime = new HoneySlimeEntity(EntityTypes.HONEY_SLIME, world);
                        slime.refreshPositionAndAngles(blockPos, this.world.getRandom().nextFloat() * 360, 0.0F);
                        if (slime.canSpawn(this.world) && slime.canSpawn(this.world, SpawnType.NATURAL) &&
                                this.world.getBlockState(blockPos.down()).hasSolidTopSurface(this.world, blockPos, slime) &&
                                this.world.doesNotCollide(slime)) {
                            slime.initialize(this.world, this.world.getLocalDifficulty(pos), SpawnType.NATURAL, null, null);
                            this.world.spawnEntity(slime);
                            break;
                        }
                    }
                }
            }
        }
    }
}
