package io.github.lucun.netheriteupgrade.api.multiblock.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Tickable;

public abstract class OriginPlaceHolderEntityAbstract extends BlockEntity implements Tickable {
    private int ticks;
    private int checkRate;
    protected String patternName;
    protected int rotation;
    protected boolean mirrored;

    public OriginPlaceHolderEntityAbstract(BlockEntityType<?> type, String name, int rotation, boolean mirrored) {
        this(type, 5, name, rotation, mirrored);
    }

    public OriginPlaceHolderEntityAbstract(BlockEntityType<?> type, int checkRate, String name, int rotation, boolean mirrored) {
        super(type);
        this.ticks = 0;
        this.checkRate = checkRate;
        this.patternName = name;
        this.rotation = rotation;
        this.mirrored = mirrored;
    }

    @Override
    public void tick() {
        if (this.ticks++ % this.checkRate == 0) {
            if (this.checkIntegrity()) {
                this.disassemble();
            }
        }
    }

    protected abstract void assemble();

    protected abstract void disassemble();

    protected abstract boolean checkIntegrity();
}

