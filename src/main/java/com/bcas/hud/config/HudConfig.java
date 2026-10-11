package com.bcas.hud.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class HudConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "bcas-hud.json");
    private static HudConfig instance;

    public boolean enabled = true;
    public boolean showTopStats = true;
    public boolean showServerBadge = false;
    public boolean showLeftPanel = true;
    public boolean showBottomStatus = true;
    public boolean hideVanillaStatusBars = true;
    public boolean leftPanelCollapsed = false;
    public boolean sidebarTopCorner = false;
    public String sidebarSize = "NORMAL"; // "COMPACT", "NORMAL", "LARGE"

    public String serverName = "BCAS SMP";
    public String rankText = "EXPLORER";

    public static HudConfig get() {
        if (instance == null) {
            load();
        }
        return instance;
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                instance = GSON.fromJson(reader, HudConfig.class);
            } catch (Exception e) {
                instance = new HudConfig();
            }
        } else {
            instance = new HudConfig();
            save();
        }
    }

    public static void save() {
        if (instance == null) {
            instance = new HudConfig();
        }
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(instance, writer);
            }
        } catch (IOException e) {
            // Non-critical, ignore
        }
    }
}
