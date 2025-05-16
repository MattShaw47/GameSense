package com.lemon;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;
import java.util.stream.Collectors;

public class PotionUseDetector {
    private static final Map<UUID, Set<StatusEffect>> knownEffects = new HashMap<>();

    public static void checkPotions(MinecraftClient client) {
        if (!GameSenseConfig.INSTANCE.potionNotifier || client.world == null) return;

        for (PlayerEntity player : client.world.getPlayers()) {
//            if (player == client.player) continue;

            Set<StatusEffect> currentEffects = player.getStatusEffects().stream()
                    .map(e -> e.getEffectType().value()) // Unwrap RegistryEntry
                    .collect(Collectors.toSet());

            UUID uuid = player.getUuid();
            Set<StatusEffect> previousEffects = knownEffects.getOrDefault(uuid, new HashSet<>());

            for (StatusEffect effect : currentEffects) {
                String effectId = Registries.STATUS_EFFECT.getId(effect).getPath();

                if (!previousEffects.contains(effect) &&
                        GameSenseConfig.INSTANCE.trackedEffects.contains(effectId)) {
                    alert(player, effectId);
                }
            }

            knownEffects.put(uuid, currentEffects);
        }
    }

    private static void alert(PlayerEntity player, String effectId) {
        String name = player.getName().getString();

        MutableText msg = Text.literal(name + " gained " + effectId)
                .styled(style -> style.withColor(GameSenseConfig.INSTANCE.potionFormatting.mcFormat));

        Text finalMsg = GameSenseClient.PREFIX.copy().append(msg);
        MinecraftClient.getInstance().player.sendMessage(finalMsg, false);
    }
}
