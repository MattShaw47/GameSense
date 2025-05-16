package com.lemon;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameSenseClient implements ClientModInitializer {

	private long accumMS = 0;

	private static final String MOD_ID = "gamesense";

	static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	// Milliseconds between each assumed server tick.
	private static final long SERVER_TICK_INTERVAL = 50L;

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			RenderTracker.tick(client);

			long now = Util.getMeasuringTimeMs();

			if (now - accumMS > SERVER_TICK_INTERVAL) {
					accumMS = now;
					LowHPNotifier.checkHP(client);
			}
		});
	}
}