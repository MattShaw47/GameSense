package com.lemon;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArmorSwapNotifier {
    private static final Map<UUID, Integer> lastArmor = new HashMap<>();

    public static void checkArmor(MinecraftClient client) {
        if (!GameSenseConfig.INSTANCE.armorNotifier || client.world == null) return;

        for (PlayerEntity player : client.world.getPlayers()) {
            if (player == client.player && !GameSenseConfig.INSTANCE.armorIncludeSelf) continue;

            int currentArmor = player.getArmor();
            UUID uuid = player.getUuid();

            if (!lastArmor.containsKey(uuid)) {
                lastArmor.put(uuid, currentArmor);
            }

            int last = lastArmor.get(uuid);
            int diff = currentArmor - last;

            lastArmor.put(uuid, currentArmor);


            if (diff != 0) {
                int absDiff = Math.abs(diff);
                int threshold = GameSenseConfig.INSTANCE.armorThreshold;

                if (threshold == 0 || absDiff >= threshold) {
                    MutableText msg = Text.literal(String.format("%s armor %s%d!",
                            player.getName().getString(),
                            diff > 0 ? "+" : "",
                            diff))
                            .styled(style -> style.withColor(GameSenseConfig.INSTANCE.armorFormatting.mcFormat));

                    client.player.sendMessage(GameSenseClient.PREFIX.copy().append(msg), false);
                }
            }
        }
    }

    public static void clearCache() {
        lastArmor.clear();
    }
}
