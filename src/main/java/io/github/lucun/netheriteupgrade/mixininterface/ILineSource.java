package io.github.lucun.netheriteupgrade.mixininterface;

import net.minecraft.client.gui.hud.ChatHudLine;

import java.util.List;

public interface ILineSource {
    ChatHudLine getLine(double x, double y);
}
