package com.bcas.hud.gui;

import com.bcas.hud.config.HudConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
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
        int btnW = 180;
        int btnH = 20;
        int startX = (this.width - btnW) / 2;
        int y = 50;

        // Toggle Enabled
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("HUD Master: " + (config.enabled ? "§aEnabled" : "§cDisabled")),
                btn -> {
                    config.enabled = !config.enabled;
                    btn.setMessage(Text.literal("HUD Master: " + (config.enabled ? "§aEnabled" : "§cDisabled")));
                    HudConfig.save();
                }
        ).dimensions(startX, y, btnW, btnH).build());
        y += 24;

        // Toggle Top Stats
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Top-Right Stats: " + (config.showTopStats ? "§aShown" : "§cHidden")),
                btn -> {
                    config.showTopStats = !config.showTopStats;
                    btn.setMessage(Text.literal("Top-Right Stats: " + (config.showTopStats ? "§aShown" : "§cHidden")));
                    HudConfig.save();
                }
        ).dimensions(startX, y, btnW, btnH).build());
        y += 24;

        // Toggle Left Panel
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Left Tool Panel: " + (config.showLeftPanel ? "§aShown" : "§cHidden")),
                btn -> {
                    config.showLeftPanel = !config.showLeftPanel;
                    btn.setMessage(Text.literal("Left Tool Panel: " + (config.showLeftPanel ? "§aShown" : "§cHidden")));
                    HudConfig.save();
                }
        ).dimensions(startX, y, btnW, btnH).build());
        y += 24;

        // Toggle Bottom Status
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Bottom Status (HP/Mana): " + (config.showBottomStatus ? "§aShown" : "§cHidden")),
                btn -> {
                    config.showBottomStatus = !config.showBottomStatus;
                    btn.setMessage(Text.literal("Bottom Status (HP/Mana): " + (config.showBottomStatus ? "§aShown" : "§cHidden")));
                    HudConfig.save();
                }
        ).dimensions(startX, y, btnW, btnH).build());
        y += 24;

        // Toggle Hide Vanilla Bars
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Hide Vanilla Hearts/Food: " + (config.hideVanillaStatusBars ? "§aYes" : "§cNo")),
                btn -> {
                    config.hideVanillaStatusBars = !config.hideVanillaStatusBars;
                    btn.setMessage(Text.literal("Hide Vanilla Hearts/Food: " + (config.hideVanillaStatusBars ? "§aYes" : "§cNo")));
                    HudConfig.save();
                }
        ).dimensions(startX, y, btnW, btnH).build());
        y += 32;

        // Done button
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Done"),
                btn -> {
                    HudConfig.save();
                    if (this.client != null) {
                        this.client.setScreen(this.parent);
                    }
                }
        ).dimensions(startX, y, btnW, btnH).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 25, 0xFFF43F5E);
    }

    @Override
    public void close() {
        HudConfig.save();
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}
