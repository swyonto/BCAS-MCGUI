package com.bcas.hud.gui;

import com.bcas.hud.config.HudConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class BcasSettingsScreen extends Screen {
    private final Screen parent;

    public BcasSettingsScreen(Screen parent) {
        super(Text.literal("BCAS SMP HUD Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        HudConfig config = HudConfig.get();
        int btnW = 150;
        int btnH = 20;
        int midX = this.width / 2;
        int col1X = midX - btnW - 6;
        int col2X = midX + 6;
        int y = 46;

        // --- ROW 1 ---
        // Left: Toggle HUD Master
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("HUD Master: " + (config.enabled ? "§aEnabled" : "§cDisabled")),
                btn -> {
                    config.enabled = !config.enabled;
                    btn.setMessage(Text.literal("HUD Master: " + (config.enabled ? "§aEnabled" : "§cDisabled")));
                    HudConfig.save();
                }
        ).dimensions(col1X, y, btnW, btnH).build());

        // Right: Toggle Bottom Status
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Bottom Status: " + (config.showBottomStatus ? "§aShown" : "§cHidden")),
                btn -> {
                    config.showBottomStatus = !config.showBottomStatus;
                    btn.setMessage(Text.literal("Bottom Status: " + (config.showBottomStatus ? "§aShown" : "§cHidden")));
                    HudConfig.save();
                }
        ).dimensions(col2X, y, btnW, btnH).build());
        y += 26;

        // --- ROW 2 ---
        // Left: Toggle Top Stats
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Top-Right Stats: " + (config.showTopStats ? "§aShown" : "§cHidden")),
                btn -> {
                    config.showTopStats = !config.showTopStats;
                    btn.setMessage(Text.literal("Top-Right Stats: " + (config.showTopStats ? "§aShown" : "§cHidden")));
                    HudConfig.save();
                }
        ).dimensions(col1X, y, btnW, btnH).build());

        // Right: Toggle Hide Vanilla Bars
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Vanilla Bars: " + (config.hideVanillaStatusBars ? "§cHidden" : "§aShown")),
                btn -> {
                    config.hideVanillaStatusBars = !config.hideVanillaStatusBars;
                    btn.setMessage(Text.literal("Vanilla Bars: " + (config.hideVanillaStatusBars ? "§cHidden" : "§aShown")));
                    HudConfig.save();
                }
        ).dimensions(col2X, y, btnW, btnH).build());
        y += 26;

        // --- ROW 3 ---
        // Left: Toggle Left Tool Panel
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Left Panel: " + (config.showLeftPanel ? "§aShown" : "§cHidden")),
                btn -> {
                    config.showLeftPanel = !config.showLeftPanel;
                    btn.setMessage(Text.literal("Left Panel: " + (config.showLeftPanel ? "§aShown" : "§cHidden")));
                    HudConfig.save();
                }
        ).dimensions(col1X, y, btnW, btnH).build());

        // Right: Sidebar Position (Center-Left vs Top-Left Corner)
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Sidebar Pos: " + (config.sidebarTopCorner ? "§eTop-Left" : "§bCenter")),
                btn -> {
                    config.sidebarTopCorner = !config.sidebarTopCorner;
                    btn.setMessage(Text.literal("Sidebar Pos: " + (config.sidebarTopCorner ? "§eTop-Left" : "§bCenter")));
                    HudConfig.save();
                }
        ).dimensions(col2X, y, btnW, btnH).build());
        y += 26;

        // --- ROW 4 ---
        // Left: Sidebar Size (Normal / Compact / Large)
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Sidebar Size: §e" + config.sidebarSize),
                btn -> {
                    if ("NORMAL".equals(config.sidebarSize)) {
                        config.sidebarSize = "LARGE";
                    } else if ("LARGE".equals(config.sidebarSize)) {
                        config.sidebarSize = "COMPACT";
                    } else {
                        config.sidebarSize = "NORMAL";
                    }
                    btn.setMessage(Text.literal("Sidebar Size: §e" + config.sidebarSize));
                    HudConfig.save();
                }
        ).dimensions(col1X, y, btnW, btnH).build());

        // Right: Customize Shortcuts (Opens Keybinds Screen)
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("⌨ Keybinds / Shortcuts"),
                btn -> {
                    if (this.client != null) {
                        this.client.setScreen(new KeybindsScreen(this, this.client.options));
                    }
                }
        ).dimensions(col2X, y, btnW, btnH).build());
        y += 30;

        // --- ROW 5: CENTERED DONE BUTTON ---
        int doneW = 160;
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Done"),
                btn -> {
                    HudConfig.save();
                    if (this.client != null) {
                        this.client.setScreen(this.parent);
                    }
                }
        ).dimensions((this.width - doneW) / 2, y, doneW, btnH).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render grayish translucent background card
        int cardW = 340;
        int cardH = 180;
        int cardX = (this.width - cardW) / 2;
        int cardY = 22;

        context.fill(cardX, cardY, cardX + cardW, cardY + cardH, 0x7518181B); // Grayish, less opacity
        drawOutline(context, cardX, cardY, cardW, cardH, 0x4452525B); // Neutral zinc border

        // Title on top
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, cardY + 8, 0xFFF1F5F9);

        // Render buttons ON TOP of background
        super.render(context, mouseX, mouseY, delta);
    }

    private void drawOutline(DrawContext context, int x, int y, int w, int h, int color) {
        context.fill(x, y, x + w, y + 1, color);
        context.fill(x, y + h - 1, x + w, y + h, color);
        context.fill(x, y + 1, x + 1, y + h, color);
        context.fill(x + w - 1, y, x + w, y + h, color);
    }

    @Override
    public void close() {
        HudConfig.save();
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}
