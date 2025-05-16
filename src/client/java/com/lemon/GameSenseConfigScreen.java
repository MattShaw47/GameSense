package com.lemon;

import net.minecraft.client.gui.screen.Screen;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameSenseConfigScreen {
    public static Screen create(Screen parent) {
        GameSenseConfig config = GameSenseConfig.INSTANCE;

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.of("GameSense Config"))
                .setSavingRunnable(config::save);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // General
        ConfigCategory general = builder.getOrCreateCategory(Text.of("General"));

        general.addEntry(entryBuilder.startBooleanToggle(Text.of("Show Potion Use"), config.showPotionUse)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.showPotionUse = newValue)
                .build());

        // LowHPNotifier
        ConfigCategory lowHPNotifier = builder.getOrCreateCategory(Text.of("Low HP Notifier"));

        lowHPNotifier.addEntry(entryBuilder.startBooleanToggle(Text.of("Low HP Notifier"), config.lowHPNotifier)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.lowHPNotifier = newValue)
                .build());

        lowHPNotifier.addEntry(entryBuilder.startBooleanToggle(Text.of("Tag low HP players."), config.lowHPTag)
                .setDefaultValue(true)
                .setTooltip(Text.of("Add a symbol to the nametag of low HP players."))
                .setSaveConsumer(newValue -> config.lowHPTag = newValue)
                .build());

        lowHPNotifier.addEntry(entryBuilder.startBooleanToggle(Text.of("Highlight low HP players."), config.lowHPHighlight)
                .setDefaultValue(true)
                .setTooltip(Text.of("Sets low HP players to have the glowing effect."))
                .setSaveConsumer(newValue -> config.lowHPHighlight = newValue)
                .build());

        lowHPNotifier.addEntry(entryBuilder.startFloatField(Text.of("Low HP threshold"), config.lowHPThreshold)
                .setDefaultValue(6.0f)
                .setTooltip(Text.of("HP values in half heart increments."))
                .setSaveConsumer(newValue -> config.lowHPThreshold = newValue)
                .build());

        lowHPNotifier.addEntry(entryBuilder.startBooleanToggle(Text.of("Send low HP message."), config.lowHPMessage)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.lowHPMessage = newValue)
                .build());

        lowHPNotifier.addEntry(entryBuilder.startEnumSelector(Text.of("Low HP message color."), ColorFormatting.class, config.lowHpFormatting)
                .setDefaultValue(ColorFormatting.RED)
                .setDefaultValue(ColorFormatting.RED)
                .setTooltip(Text.of("Also affects the color of the on screen popup."))
                .setSaveConsumer(newValue -> {config.lowHpFormatting = newValue;})
                .setEnumNameProvider(c -> {
                    ColorFormatting color = (ColorFormatting) c;
                    return Text.literal(color.toString()).styled(s -> s.withColor(color.mcFormat));
                })
                .build());

        lowHPNotifier.addEntry(entryBuilder.startBooleanToggle(Text.of("Low HP module effects also include self."), config.lowHPIncludeSelf)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> config.lowHPIncludeSelf = newValue)
                .build());


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

        return builder.build();
    }

    private static List<Formatting> getColorOptions() {
        return Arrays.stream(Formatting.values())
                .filter(Formatting::isColor)
                .collect(Collectors.toList());
    }
}
