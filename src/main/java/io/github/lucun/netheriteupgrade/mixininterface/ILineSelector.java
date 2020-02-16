package io.github.lucun.netheriteupgrade.mixininterface;

import net.minecraft.client.gui.hud.ChatHudLine;

import java.util.Set;

public interface ILineSelector {
    Set<ChatHudLine> getSelectionSet();
}
