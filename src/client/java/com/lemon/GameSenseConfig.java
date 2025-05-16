package com.lemon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class GameSenseConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/gamesense.json");

    public boolean highlightLowHP = true;
    public boolean showPotionUse = true;

    // Render / de-render settings
    public boolean notifyRender = true;
    public boolean notifyDerender = true;
    public boolean tablistHighlight = true;

    public List<String> renderBlacklist = new ArrayList<>();


    public static GameSenseConfig INSTANCE = load();

    public static GameSenseConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                return GSON.fromJson(reader, GameSenseConfig.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new GameSenseConfig();
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
