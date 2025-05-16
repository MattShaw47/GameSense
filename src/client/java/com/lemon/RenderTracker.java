package com.lemon;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class RenderTracker {
    private static final Set<UUID> lastSeen = new HashSet<>();

    public static boolean isVisible(UUID uuid) {
        return lastSeen.contains(uuid);
    }

    public static void tick(MinecraftClient client) {
        if (client.world == null || client.player == null) return;

        Set<UUID> currentlySeen = client.world.getPlayers().stream()
                .map(PlayerEntity::getUuid)
                .filter(uuid -> !uuid.equals(client.player.getUuid()))
                .collect(Collectors.toSet());

        for (UUID uuid : currentlySeen) {
            if (!lastSeen.contains(uuid)) {
                onRender(uuid, client);
            }
        }

        for (UUID uuid : lastSeen) {
            if (!currentlySeen.contains(uuid)) {
                onDerender(uuid, client);
            }
        }

        lastSeen.clear();
        lastSeen.addAll(currentlySeen);
    }

    private static void onRender(UUID uuid, MinecraftClient client) {
        PlayerEntity p = client.world.getPlayerByUuid(uuid);
        if (p == null) return;

        String name = p.getName().getString();
        GameSenseConfig cfg = GameSenseConfig.INSTANCE;

        if (!cfg.notifyRender) return;

        if (!cfg.renderBlacklist.isEmpty() && cfg.renderBlacklist.contains(name)) return;

        client.player.sendMessage(Text.of("[GS] " + name + " entered render distance."), false);
    }

    private static void onDerender(UUID uuid, MinecraftClient client) {
        PlayerEntity p = client.world.getPlayerByUuid(uuid);
        if (p == null) return;

        String name = p.getName().getString();
        GameSenseConfig cfg = GameSenseConfig.INSTANCE;

        if (!cfg.notifyDerender) return;

        if (!cfg.renderBlacklist.isEmpty() && cfg.renderBlacklist.contains(name)) return;

        client.player.sendMessage(Text.of("[GS] " + name + " left render distance."), false);

    }

}
