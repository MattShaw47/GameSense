package com.lemon;

import net.minecraft.client.gui.screen.Screen;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.stream.Collectors;

public class GameSenseConfigScreen {
    public static Screen create(Screen parent) {
        GameSenseConfig config = GameSenseConfig.INSTANCE;

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.of("GameSense Config"))
                .setSavingRunnable(config::save);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // Render Tracker
        ConfigCategory renderTracker = builder.getOrCreateCategory(Text.of("Render Tracker"));

        renderTracker.addEntry(entryBuilder.startBooleanToggle(Text.of("Notify on render"), config.notifyRender)
                .setDefaultValue(true)
                .setTooltip(Text.of("Sends a chat message when a player enters render distance."))
                .setSaveConsumer(newValue -> config.notifyRender = newValue)
                .build());

        renderTracker.addEntry(entryBuilder.startBooleanToggle(Text.of("Notify on de-render"), config.notifyDerender)
                .setDefaultValue(true)
                .setTooltip(Text.of("Sends a chat message when a player leaves render distance."))
                .setSaveConsumer(newValue -> config.notifyDerender = newValue)
                .build());

        renderTracker.addEntry(entryBuilder.startBooleanToggle(Text.of("Highlight rendered in tablist"), config.tablistHighlight)
                .setDefaultValue(true)
                .setTooltip(Text.of("Displays an icon in tablist next to players who are within render distance."))
                .setSaveConsumer(newValue -> config.tablistHighlight = newValue)
                .build());

        renderTracker.addEntry(entryBuilder.startStrField(Text.of("Blacklist (comma-seperated)"),
                String.join(", ", config.renderBlacklist))
                .setDefaultValue("")
                .setTooltip(Text.of("Ignore notifications for these players."))
                .setSaveConsumer(input -> {
                    config.renderBlacklist = Arrays.stream(input.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toList());
                }).build());


        ConfigCategory general = builder.getOrCreateCategory(Text.of("General"));

        general.addEntry(entryBuilder.startBooleanToggle(Text.of("Highlight Low Hp"), config.highlightLowHP)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.highlightLowHP = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.of("Show Potion Use"), config.showPotionUse)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.showPotionUse = newValue)
                .build());

        return builder.build();
    }

}
