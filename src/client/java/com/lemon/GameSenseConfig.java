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

    public boolean showPotionUse = true;

    // Render / de-render settings
    public boolean renderNotify = true;
    public boolean renderLeaveNotify = true;
    public boolean tablistHighlight = true;
    public List<String> renderBlacklist = new ArrayList<>();
    public ColorFormatting renderFormatting = ColorFormatting.RED;

    // LowHPNotifier settings
    public boolean lowHPNotifier = true;
    public float lowHPThreshold = 6.0f;
    public boolean lowHPTag = true;
    public boolean lowHPHighlight = true;
    public boolean lowHPMessage = true;
    public ColorFormatting lowHpFormatting = ColorFormatting.RED;
    public boolean lowHPIncludeSelf = false;

    // Potion Notifier settings
    public boolean potionNotifier = true;
    public List<String> trackedEffects = new ArrayList<>(List.of("resistance"));
    public ColorFormatting potionFormatting = ColorFormatting.RED;

    // Armor Swap Notifier settings
    public boolean armorNotifier = true;
    public int armorThreshold = 8; // 0 = all changes reported
    public boolean armorIncludeSelf;
    public ColorFormatting armorFormatting = ColorFormatting.RED;


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
