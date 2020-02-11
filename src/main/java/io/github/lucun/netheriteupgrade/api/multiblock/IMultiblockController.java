package io.github.lucun.netheriteupgrade.api.multiblock;

import io.github.lucun.netheriteupgrade.api.multiblock.pattern.ISubPattern;

public interface IMultiblockController {
    ISubPattern getPatternOccupied();
    ISubPattern getExclusivePattern();
    void destroy();
}
