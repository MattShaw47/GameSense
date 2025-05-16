package com.lemon;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.text.Text;


import java.util.UUID;

public class LowHPNotifier {

    public static void checkHP(MinecraftClient client) {
        GameSenseConfig config = GameSenseConfig.INSTANCE;

        if (client.world == null || config.lowHPNotifier) return;

        for (PlayerEntity player : client.world.getPlayers()) {
            UUID uuid = player.getUuid();
            float currentHP = player.getHealth();

            if (currentHP > config.lowHPThreshold) {
                sendAlert(player);
            }
        }
    }

    private static void sendAlert(PlayerEntity player) {
        GameSenseClient.LOGGER.info("Sending lowHP alert regarding {}", player.getDisplayName().getString());

        MinecraftClient client = MinecraftClient.getInstance();
        client.player.sendMessage(Text.of(player.getDisplayName().getString() + " is low HP!"), false);
    }
}
