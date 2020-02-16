package io.github.lucun.netheriteupgrade.mixin;

import io.github.lucun.netheriteupgrade.mixininterface.ILineSelector;
import io.github.lucun.netheriteupgrade.mixininterface.ILineSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class MixinChatHud implements ILineSource {

    @Shadow @Final private List<ChatHudLine> visibleMessages;

    @Shadow public abstract boolean isChatFocused();

    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract boolean method_23677();

    @Shadow public abstract double getChatScale();

    @Shadow public abstract int getVisibleLineCount();

    @Shadow public abstract int getWidth();

    @Shadow private int scrolledLines;

    @Shadow public abstract Text getText(double x, double y);

    @Redirect(method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", at = @At(
            value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0))
    private int onQueryListSize1(List<Text> list) {
        return list.size() - (1024 - 100);
    }

    @Redirect(method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", at = @At(
            value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 2))
    private int onQueryListSize2(List<Text> list) {
        return list.size() - (1024 - 100);
    }

    @Redirect(method = "render", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/ChatHudLine;getText()Lnet/minecraft/text/Text;"
    ))
    private Text onGetFormattedString(ChatHudLine line) {
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen instanceof ChatScreen && ((ILineSelector) screen).getSelectionSet().contains(line)) {
            return line.getText().deepCopy().append("§6    <-§r");
        }
        return line.getText();
    }

    public ChatHudLine getLine(double x, double y) {
        if (this.isChatFocused() && !this.client.options.hudHidden && this.method_23677()) {
            double d = this.getChatScale();
            double e = x - 2.0D;
            double f = (double)this.client.getWindow().getScaledHeight() - y - 40.0D;
            e = MathHelper.floor(e / d);
            f = MathHelper.floor(f / d);
            if (e >= 0.0D && f >= 0.0D) {
                int i = Math.min(this.getVisibleLineCount(), this.visibleMessages.size());
                if (e <= (double)MathHelper.floor((double)this.getWidth() / this.getChatScale())) {
                    if (f < (double)(9 * i + i)) {
                        int j = (int)(f / 9.0D + (double)this.scrolledLines);
                        if (j >= 0 && j < this.visibleMessages.size()) {
                            return this.visibleMessages.get(j);
                        }

                        return null;
                    }
                }

            }
        }
        return null;
    }
}
