package io.github.lucun.netheriteupgrade.mixin;

import io.github.lucun.netheriteupgrade.mixininterface.ILineSelector;
import io.github.lucun.netheriteupgrade.mixininterface.ILineSource;
import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.CommandSuggestor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(ChatScreen.class)
public abstract class MixinChatScreen implements ParentElement, ILineSelector {

    private Set<ChatHudLine> selectedLine = new ReferenceArraySet<>();

    @Shadow protected TextFieldWidget chatField;

    @Shadow private CommandSuggestor commandSuggestor;
    private boolean dragStarted = false;

    @Inject(method = "mouseClicked", at = @At(
            value = "HEAD"
    ))
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!this.commandSuggestor.mouseClicked((int)mouseX, (int)mouseY, button) && button == 0) {
            this.selectedLine.clear();
            ChatHudLine line = ((ILineSource) MinecraftClient.getInstance().inGameHud.getChatHud()).getLine(mouseX, mouseY);
            if (line != null) {
                this.selectedLine.add(line);
                this.dragStarted = true;
            }
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.dragStarted = false;
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (button == 0 && this.dragStarted) {
            ChatHudLine line = ((ILineSource) MinecraftClient.getInstance().inGameHud.getChatHud()).getLine(mouseX, mouseY);
            if (line != null) {
                this.selectedLine.add(line);
            }
        }
        return false;
    }

    @Inject(method = "keyPressed", at = @At(
            value = "HEAD"
    ))
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (this.chatField.getSelectedText().isEmpty()) {
            if (keyCode == 67 && Screen.hasControlDown() && Screen.hasShiftDown() && !Screen.hasAltDown() && !selectedLine.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                selectedLine.forEach(line -> sb.append(line.getText().getString().replaceAll("§.", "")).append("\n"));
                MinecraftClient.getInstance().keyboard.setClipboard(sb.toString());
                selectedLine.clear();
            }
        }
        if (keyCode == 256 /*ESC*/ || keyCode == 257 /*ENTER*/ || keyCode == 335 /*KEYPAD_ENTER*/) {
            this.selectedLine.clear();
        }
    }

    @Override
    public Set<ChatHudLine> getSelectionSet() {
        return this.selectedLine;
    }
}

