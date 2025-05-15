package com.lemon;

import net.minecraft.client.gui.screen.Screen;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class GameSenseConfigScreen {
    public static Screen create(Screen parent) {
        GameSenseConfig config = GameSenseConfig.INSTANCE;

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.of("GameSense Config"))
                .setSavingRunnable(config::save);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
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
