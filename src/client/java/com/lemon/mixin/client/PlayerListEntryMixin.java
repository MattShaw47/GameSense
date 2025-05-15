package com.lemon.mixin.client;

import com.lemon.GameSenseConfig;
import com.lemon.RenderTracker;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(PlayerListEntry.class)
public class PlayerListEntryMixin {
    @Inject(method = "getDisplayName", at = @At("RETURN"))
    private void appendHighlight(CallbackInfoReturnable<Text> cir) {
        PlayerListEntry entry = (PlayerListEntry)(Object)this;
        UUID uuid = entry.getProfile().getId();

        if (!GameSenseConfig.INSTANCE.tablistHighlight || !RenderTracker.isVisible(uuid)) return;

        Text original = cir.getReturnValue();
        Text decorated = Text.empty()
                .append(original)
                .append(Text.literal(" ⬤").styled(style ->
                        style.withColor(Formatting.AQUA).withBold(true)
                ));
    }
}
