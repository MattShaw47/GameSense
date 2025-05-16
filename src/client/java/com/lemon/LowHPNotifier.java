package com.lemon;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mutable;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LowHPNotifier {

    private static final Set<UUID> lowHPPlayers = new HashSet<UUID>();

    public static void checkHP(MinecraftClient client) {
        GameSenseConfig config = GameSenseConfig.INSTANCE;

        if (client.world == null || !config.lowHPNotifier) return;

        for (PlayerEntity player : client.world.getPlayers()) {
            if (player == client.player && !config.lowHPIncludeSelf) continue;

            UUID uuid = player.getUuid();
            float currentHP = player.getHealth();

            if (currentHP <= config.lowHPThreshold && !lowHPPlayers.contains(uuid)) {
                lowHPPlayers.add(uuid);
                sendAlert(player);

                if (config.lowHPTag) {
                    player.setGlowing(true);
                }

                if (config.lowHPHighlight) {
                    if (!player.isCustomNameVisible()) {
                        player.setCustomNameVisible(true);
                    }

                    Text baseName = player.getName();
                    if (!baseName.getString().endsWith(" ⚠")) {
                        player.setCustomName(Text.literal(baseName.getString() + " ⚠"));
                    }
                }
            }
            else if (currentHP >= config.lowHPThreshold && lowHPPlayers.contains(uuid)) {
                lowHPPlayers.remove(uuid);
                player.setGlowing(false);
                player.setCustomName(player.getName());
                player.setCustomNameVisible(false);
            }
        }
    }

    private static void sendAlert(PlayerEntity player) {
        if (!GameSenseConfig.INSTANCE.lowHPMessage) return;

        GameSenseClient.LOGGER.info("Sending lowHP alert regarding {}", player.getDisplayName().getString());

        MinecraftClient client = MinecraftClient.getInstance();

        MutableText msg = Text.literal(player.getDisplayName().getString() + " is low HP!")
                .styled(style -> style.withColor(GameSenseConfig.INSTANCE.lowHpFormatting.mcFormat));

        client.player.sendMessage(GameSenseClient.PREFIX.copy().append(msg), false);
    }
}
