package com.bcas.hud.render;

import com.bcas.hud.config.HudConfig;
import com.bcas.hud.data.ManaManager;
import com.bcas.hud.gui.BcasSettingsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

public class HudOverlayRenderer {

    // Colors
    private static final int BG_COLOR = 0xAA0F172A; // Modern dark slate
    private static final int BORDER_COLOR = 0x33475569;
    private static final int HOVER_COLOR = 0x5538BDF8;
    private static final int TEXT_WHITE = 0xFFF8FAFC;
    private static final int TEXT_MUTED = 0xFF94A3B8;

    // Panel bounds for mouse clicks
    public static final int LEFT_PANEL_X = 8;
    public static final int LEFT_PANEL_Y = 55;
    public static final int LEFT_PANEL_W = 22;
    public static final int LEFT_PANEL_COLLAPSED_W = 16;
    public static final int LEFT_PANEL_H = 96;
    public static final int LEFT_PANEL_COLLAPSED_H = 26;

    public static void render(DrawContext context, float tickDelta) {
        HudConfig config = HudConfig.get();
        if (!config.enabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.hudHidden || client.player == null) return;

        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        TextRenderer tr = client.textRenderer;

        if (config.showTopStats) {
            renderTopRightStats(context, tr, client, screenWidth);
        }

        if (config.showLeftPanel) {
            renderLeftPanel(context, client);
        }

        if (config.showBottomStatus) {
            renderBottomStatus(context, tr, client, screenWidth, screenHeight);
        }
    }

    private static void renderTopRightStats(DrawContext context, TextRenderer tr, MinecraftClient client, int screenWidth) {
        HudConfig config = HudConfig.get();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        int pillW = 96;
        int pillH = 17;
        int startX = screenWidth - pillW - 8;
        int y = 8;

        // 1. FPS Pill
        int fps = client.getCurrentFps();
        drawPill(context, startX, y, pillW, pillH);
        IconRenderer.drawMonitor(context, startX + 5, y + 4, 0xFF38BDF8);
        context.drawText(tr, String.valueOf(fps), startX + 22, y + 5, TEXT_WHITE, false);
        y += pillH + 4;

        // 2. Coordinates Pill
        int px = (int) Math.floor(player.getX());
        int py = (int) Math.floor(player.getY());
        int pz = (int) Math.floor(player.getZ());
        String coords = px + ", " + py + ", " + pz;
        int coordPillW = Math.max(pillW, tr.getWidth(coords) + 26);
        int coordX = screenWidth - coordPillW - 8;
        drawPill(context, coordX, y, coordPillW, pillH);
        IconRenderer.drawPin(context, coordX + 6, y + 3, 0xFFFACC15);
        context.drawText(tr, coords, coordX + 22, y + 5, TEXT_WHITE, false);
        y += pillH + 4;

        // 3. Player Count Pill
        int onlineCount = 1;
        int maxPlayers = 24;
        if (client.getNetworkHandler() != null) {
            onlineCount = client.getNetworkHandler().getPlayerList().size();
        }
        String countStr = String.format("%02d/%02d", onlineCount, maxPlayers);
        drawPill(context, startX, y, pillW, pillH);
        IconRenderer.drawUsers(context, startX + 5, y + 4, 0xFF38BDF8);
        context.drawText(tr, countStr, startX + 22, y + 5, TEXT_WHITE, false);
        y += pillH + 4;

        // 4. Ping Pill
        int ping = 0;
        if (client.getNetworkHandler() != null && player != null) {
            PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(player.getUuid());
            if (entry != null) {
                ping = Math.max(0, entry.getLatency());
            }
        }
        String pingStr = ping + " ms";
        drawPill(context, startX, y, pillW, pillH);
        IconRenderer.drawWifi(context, startX + 5, y + 4, 0xFF4ADE80);
        context.drawText(tr, pingStr, startX + 22, y + 5, TEXT_WHITE, false);
        y += pillH + 6;

        // 5. BCAS SMP Badge
        if (config.showServerBadge) {
            String badgeText = config.serverName;
            int badgeW = tr.getWidth(badgeText) + 16;
            int badgeX = screenWidth - badgeW - 8;
            int badgeH = 16;
            context.fill(badgeX, y, badgeX + badgeW, y + badgeH, 0x882A0812);
            drawOutline(context, badgeX, y, badgeW, badgeH, 0xFFE11D48);
            context.drawText(tr, badgeText, badgeX + 8, y + 4, 0xFFFB7185, false);
        }
    }

    private static void renderLeftPanel(DrawContext context, MinecraftClient client) {
        HudConfig config = HudConfig.get();
        boolean collapsed = config.leftPanelCollapsed;

        int x = LEFT_PANEL_X;
        int y = LEFT_PANEL_Y;
        int w = collapsed ? LEFT_PANEL_COLLAPSED_W : LEFT_PANEL_W;
        int h = collapsed ? LEFT_PANEL_COLLAPSED_H : LEFT_PANEL_H;

        // Background panel
        context.fill(x, y, x + w, y + h, BG_COLOR);
        drawOutline(context, x, y, w, h, BORDER_COLOR);

        // Top Chevron button area (highlighted box)
        context.fill(x + 1, y + 1, x + w - 1, y + 19, 0x551E293B);
        IconRenderer.drawChevron(context, x + (w / 2) - 3, y + 6, collapsed, 0xFFE2E8F0);

        if (!collapsed) {
            // Action 1: Settings (Gear)
            IconRenderer.drawGear(context, x + 6, y + 25, 0xFF94A3B8);
            // Action 2: Players (Users)
            IconRenderer.drawUsers(context, x + 5, y + 43, 0xFF94A3B8);
            // Action 3: Map
            IconRenderer.drawMap(context, x + 6, y + 61, 0xFF94A3B8);
            // Action 4: Mods
            IconRenderer.drawMods(context, x + 6, y + 79, 0xFF94A3B8);
        }
    }

    public static void renderInteractiveHighlights(DrawContext context, int mouseX, int mouseY) {
        HudConfig config = HudConfig.get();
        boolean collapsed = config.leftPanelCollapsed;

        int x = LEFT_PANEL_X;
        int y = LEFT_PANEL_Y;
        int w = collapsed ? LEFT_PANEL_COLLAPSED_W : LEFT_PANEL_W;

        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer tr = client.textRenderer;

        // Check hover on Chevron button
        if (mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + 20) {
            context.fill(x + 1, y + 1, x + w - 1, y + 20, HOVER_COLOR);
            String tip = collapsed ? "Expand (C)" : "Collapse (C)";
            context.drawTextWithShadow(tr, tip, x + w + 6, y + 6, 0xFFFFFFFF);
            return;
        }

        if (!collapsed && mouseX >= x && mouseX <= x + w) {
            if (mouseY >= y + 21 && mouseY < y + 39) {
                context.fill(x + 1, y + 21, x + w - 1, y + 39, HOVER_COLOR);
                context.drawTextWithShadow(tr, "Settings (O)", x + w + 6, y + 25, 0xFFFFFFFF);
            } else if (mouseY >= y + 39 && mouseY < y + 57) {
                context.fill(x + 1, y + 39, x + w - 1, y + 57, HOVER_COLOR);
                context.drawTextWithShadow(tr, "Online Players", x + w + 6, y + 43, 0xFFFFFFFF);
            } else if (mouseY >= y + 57 && mouseY < y + 75) {
                context.fill(x + 1, y + 57, x + w - 1, y + 75, HOVER_COLOR);
                context.drawTextWithShadow(tr, "Map / Coords", x + w + 6, y + 61, 0xFFFFFFFF);
            } else if (mouseY >= y + 75 && mouseY <= y + 95) {
                context.fill(x + 1, y + 75, x + w - 1, y + 95, HOVER_COLOR);
                context.drawTextWithShadow(tr, "Mod Info", x + w + 6, y + 79, 0xFFFFFFFF);
            }
        }
    }

    private static void renderBottomStatus(DrawContext context, TextRenderer tr, MinecraftClient client, int screenWidth, int screenHeight) {
        HudConfig config = HudConfig.get();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        float health = player.getHealth();
        float maxHealth = player.getMaxHealth();

        ManaManager manaManager = ManaManager.getInstance();
        float mana = manaManager.getCurrentMana();
        float maxMana = manaManager.getMaxMana();

        // Position directly above hotbar
        int barW = 68;
        int barH = 5;
        int totalWidth = (barW * 2) + 110;
        int startX = (screenWidth - totalWidth) / 2;
        int y = screenHeight - 44;

        // Health Status
        IconRenderer.drawHeart(context, startX, y, 0xFFEF4444);
        String healthStr = (int) Math.ceil(health) + "/" + (int) maxHealth;
        context.drawText(tr, healthStr, startX + 13, y + 1, TEXT_WHITE, false);
        drawSegmentedBar(context, startX, y + 10, barW, barH, health / maxHealth, 0xFFEF4444, 0xFF991B1B);

        // Mana Status
        int manaX = startX + barW + 16;
        IconRenderer.drawDroplet(context, manaX, y, 0xFF06B6D4);
        String manaStr = (int) Math.ceil(mana) + "/" + (int) maxMana;
        context.drawText(tr, manaStr, manaX + 11, y + 1, TEXT_WHITE, false);
        drawSegmentedBar(context, manaX, y + 10, barW, barH, mana / maxMana, 0xFF06B6D4, 0xFF0E7490);

        // Rank Badge
        int rankX = manaX + barW + 14;
        context.fill(rankX, y + 1, rankX + 1, y + 15, 0x6694A3B8);

        // Star Icon + Rank Text
        IconRenderer.drawStar(context, rankX + 7, y + 3, 0xFFFACC15);
        String rank = config.rankText;
        context.drawText(tr, rank, rankX + 20, y + 4, TEXT_WHITE, false);
    }

    private static void drawSegmentedBar(DrawContext context, int x, int y, int width, int height, float fillFraction, int fillColor, int trackColor) {
        fillFraction = Math.max(0f, Math.min(1f, fillFraction));
        int segments = 10;
        int segmentWidth = (width - (segments - 1)) / segments;
        int activeSegments = Math.round(fillFraction * segments);

        for (int i = 0; i < segments; i++) {
            int sx = x + (i * (segmentWidth + 1));
            int color = (i < activeSegments) ? fillColor : 0x661E293B;
            context.fill(sx, y, sx + segmentWidth, y + height, color);
        }
    }

    private static void drawPill(DrawContext context, int x, int y, int w, int h) {
        context.fill(x, y, x + w, y + h, BG_COLOR);
        drawOutline(context, x, y, w, h, BORDER_COLOR);
    }

    private static void drawOutline(DrawContext context, int x, int y, int w, int h, int color) {
        context.fill(x, y, x + w, y + 1, color);
        context.fill(x, y + h - 1, x + w, y + h, color);
        context.fill(x, y, x + 1, y + h, color);
        context.fill(x + w - 1, y, x + w, y + h, color);
    }

    public static boolean toggleToolbar() {
        HudConfig config = HudConfig.get();
        config.leftPanelCollapsed = !config.leftPanelCollapsed;
        HudConfig.save();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(
                Text.literal("§b[Tool Bar]§r " + (config.leftPanelCollapsed ? "§7Collapsed" : "§aExpanded")),
                true
            );
        }
        return config.leftPanelCollapsed;
    }

    public static boolean handleMouseClick(double mouseX, double mouseY, int button) {
        HudConfig config = HudConfig.get();
        if (!config.enabled || !config.showLeftPanel) return false;

        boolean collapsed = config.leftPanelCollapsed;
        int x = LEFT_PANEL_X;
        int y = LEFT_PANEL_Y;
        int w = collapsed ? LEFT_PANEL_COLLAPSED_W : LEFT_PANEL_W;
        int h = collapsed ? LEFT_PANEL_COLLAPSED_H : LEFT_PANEL_H;

        // Generous clickable bounds
        if (mouseX >= x - 2 && mouseX <= x + w + 4 && mouseY >= y - 2 && mouseY <= y + h + 4) {
            MinecraftClient client = MinecraftClient.getInstance();

            // Click chevron (< / >)
            if (mouseY <= y + 21) {
                toggleToolbar();
                return true;
            }

            if (!collapsed) {
                if (mouseY >= y + 21 && mouseY < y + 39) {
                    client.setScreen(new BcasSettingsScreen(client.currentScreen));
                    return true;
                } else if (mouseY >= y + 39 && mouseY < y + 57) {
                    if (client.player != null) {
                        int count = client.getNetworkHandler() != null ? client.getNetworkHandler().getPlayerList().size() : 1;
                        client.player.sendMessage(Text.literal("§b[BCAS SMP]§r Online players: §e" + count + "§r/§724"), false);
                    }
                    return true;
                } else if (mouseY >= y + 57 && mouseY < y + 75) {
                    if (client.player != null) {
                        int px = (int) client.player.getX();
                        int py = (int) client.player.getY();
                        int pz = (int) client.player.getZ();
                        client.player.sendMessage(Text.literal("§e[BCAS SMP]§r Your coordinates: §f" + px + ", " + py + ", " + pz), false);
                    }
                    return true;
                } else if (mouseY >= y + 75 && mouseY <= y + 96) {
                    if (client.player != null) {
                        client.player.sendMessage(Text.literal("§d[BCAS SMP]§r HUD Mod v1.0.0 is active."), false);
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
