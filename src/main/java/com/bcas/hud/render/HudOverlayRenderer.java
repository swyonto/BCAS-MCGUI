package com.bcas.hud.render;

import com.bcas.hud.config.HudConfig;
import com.bcas.hud.data.ManaManager;
import com.bcas.hud.gui.BcasMapScreen;
import com.bcas.hud.gui.BcasPlayerScreen;
import com.bcas.hud.gui.BcasSettingsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

public class HudOverlayRenderer {

    // Colors: Translucent grayish neutral dark slate with low opacity
    public static final int BG_COLOR = 0x4518181B; // Translucent neutral dark gray (less opacity)
    public static final int BORDER_COLOR = 0x3352525B; // Neutral zinc-gray border
    public static final int HOVER_COLOR = 0x5538BDF8;
    public static final int TEXT_WHITE = 0xFFF8FAFC;
    public static final int TEXT_MUTED = 0xFFA1A1AA;

    // Panel bounds helpers for dynamic scaling and positioning
    public static int getLeftPanelX() {
        return 8;
    }

    public static int getLeftPanelY() {
        HudConfig config = HudConfig.get();
        return config.sidebarTopCorner ? 8 : 55;
    }

    public static int getLeftPanelW(boolean collapsed) {
        HudConfig config = HudConfig.get();
        int base = collapsed ? 16 : 22;
        if ("COMPACT".equals(config.sidebarSize)) return base - 2;
        if ("LARGE".equals(config.sidebarSize)) return base + 4;
        return base;
    }

    public static int getLeftPanelH(boolean collapsed) {
        HudConfig config = HudConfig.get();
        int base = collapsed ? 24 : 76; // 3 actions (Mod Info removed)
        if ("COMPACT".equals(config.sidebarSize)) return base - 6;
        if ("LARGE".equals(config.sidebarSize)) return base + 8;
        return base;
    }

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
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        int fps = client.getCurrentFps();

        int px = (int) Math.floor(player.getX());
        int py = (int) Math.floor(player.getY());
        int pz = (int) Math.floor(player.getZ());
        String coords = px + ", " + py + ", " + pz;

        int onlineCount = 1;
        int maxPlayers = 24;
        if (client.getNetworkHandler() != null) {
            onlineCount = client.getNetworkHandler().getPlayerList().size();
        }
        String countStr = String.format("%02d/%02d", onlineCount, maxPlayers);

        int ping = 0;
        if (client.getNetworkHandler() != null) {
            PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(player.getUuid());
            if (entry != null) {
                ping = Math.max(0, entry.getLatency());
            }
        }
        String pingStr = ping + " ms";

        // One unified compact container (short, tight gap, lower opacity, no SMP tag)
        int cardW = Math.max(90, tr.getWidth(coords) + 26);
        int cardH = 58;
        int cardX = screenWidth - cardW - 8;
        int cardY = 8;

        context.fill(cardX, cardY, cardX + cardW, cardY + cardH, BG_COLOR);
        drawOutline(context, cardX, cardY, cardW, cardH, BORDER_COLOR);

        int iconX = cardX + 6;
        int textX = cardX + 22;

        // 1. FPS
        int r1Y = cardY + 4;
        IconRenderer.drawMonitor(context, iconX, r1Y + 1, 0xFF38BDF8);
        context.drawText(tr, String.valueOf(fps), textX, r1Y + 2, TEXT_WHITE, false);

        // 2. Coordinates
        int r2Y = cardY + 17;
        IconRenderer.drawPin(context, iconX + 1, r2Y, 0xFFFACC15);
        context.drawText(tr, coords, textX, r2Y + 2, TEXT_WHITE, false);

        // 3. Online Players
        int r3Y = cardY + 30;
        IconRenderer.drawUsers(context, iconX, r3Y + 1, 0xFF38BDF8);
        context.drawText(tr, countStr, textX, r3Y + 2, TEXT_WHITE, false);

        // 4. Ping
        int r4Y = cardY + 43;
        IconRenderer.drawWifi(context, iconX, r4Y + 1, 0xFF4ADE80);
        context.drawText(tr, pingStr, textX, r4Y + 2, TEXT_WHITE, false);
    }

    private static void renderLeftPanel(DrawContext context, MinecraftClient client) {
        HudConfig config = HudConfig.get();
        boolean collapsed = config.leftPanelCollapsed;

        int x = getLeftPanelX();
        int y = getLeftPanelY();
        int w = getLeftPanelW(collapsed);
        int h = getLeftPanelH(collapsed);

        // Background panel
        context.fill(x, y, x + w, y + h, BG_COLOR);
        drawOutline(context, x, y, w, h, BORDER_COLOR);

        // Top Chevron button area (highlighted box)
        context.fill(x + 1, y + 1, x + w - 1, y + 19, 0x551E293B);
        IconRenderer.drawChevron(context, x + (w / 2) - 3, y + 6, collapsed, 0xFFE2E8F0);

        if (!collapsed) {
            // Action 1: Settings (Gear)
            IconRenderer.drawGear(context, x + (w / 2) - 5, y + 25, 0xFF94A3B8);
            // Action 2: Players (Users)
            IconRenderer.drawUsers(context, x + (w / 2) - 6, y + 43, 0xFF94A3B8);
            // Action 3: Map
            IconRenderer.drawMap(context, x + (w / 2) - 5, y + 61, 0xFF94A3B8);
        }
    }

    public static void renderInteractiveHighlights(DrawContext context, int mouseX, int mouseY) {
        HudConfig config = HudConfig.get();
        boolean collapsed = config.leftPanelCollapsed;

        int x = getLeftPanelX();
        int y = getLeftPanelY();
        int w = getLeftPanelW(collapsed);

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
                context.drawTextWithShadow(tr, "Players & Ranks", x + w + 6, y + 43, 0xFFFFFFFF);
            } else if (mouseY >= y + 57 && mouseY <= y + 76) {
                context.fill(x + 1, y + 57, x + w - 1, y + 76, HOVER_COLOR);
                context.drawTextWithShadow(tr, "Live World Map", x + w + 6, y + 61, 0xFFFFFFFF);
            }
        }

        // Rank Square Highlight
        if (config.showBottomStatus && client.player != null) {
            int screenWidth = context.getScaledWindowWidth();
            int screenHeight = context.getScaledWindowHeight();
            int barW = 68;
            int rankBoxSize = 16;
            int totalWidth = barW + 14 + barW + 12 + rankBoxSize;
            int startX = (screenWidth - totalWidth) / 2;
            int rankX = startX + barW + 14 + barW + 12;
            int rankY = screenHeight - 49;

            if (mouseX >= rankX && mouseX <= rankX + rankBoxSize && mouseY >= rankY && mouseY <= rankY + rankBoxSize) {
                context.fill(rankX + 1, rankY + 1, rankX + rankBoxSize - 1, rankY + rankBoxSize - 1, HOVER_COLOR);
                context.drawTextWithShadow(tr, "Rank: " + config.rankText, rankX - 10, rankY - 12, 0xFFFFFFFF);
            }
        }
    }

    private static void renderBottomStatus(DrawContext context, TextRenderer tr, MinecraftClient client, int screenWidth, int screenHeight) {
        HudConfig config = HudConfig.get();
        ClientPlayerEntity player = client.player;
        if (player == null) return;

        float health = player.getHealth();
        float maxHealth = player.getMaxHealth();

        int food = player.getHungerManager().getFoodLevel();
        int maxFood = 20;

        // Position directly above hotbar (178px centered over hotbar's 182px width)
        int barW = 68;
        int barH = 5;
        int rankBoxSize = 16;
        int totalWidth = barW + 14 + barW + 12 + rankBoxSize; // 178px
        int startX = (screenWidth - totalWidth) / 2;
        int y = screenHeight - 49;

        // 1. Health Status
        IconRenderer.drawHeart(context, startX, y, 0xFFEF4444);
        String healthStr = (int) Math.ceil(health) + "/" + (int) maxHealth;
        context.drawText(tr, healthStr, startX + 13, y + 1, TEXT_WHITE, false);
        drawSegmentedBar(context, startX, y + 10, barW, barH, health / maxHealth, 0xFFEF4444, 0xFF991B1B);

        // 2. Hunger Status (Water is hunger -> meat drumstick icon and hunger bar)
        int hungerX = startX + barW + 14;
        IconRenderer.drawHunger(context, hungerX, y, 0xFFF59E0B);
        String hungerStr = food + "/" + maxFood;
        context.drawText(tr, hungerStr, hungerX + 13, y + 1, TEXT_WHITE, false);
        drawSegmentedBar(context, hungerX, y + 10, barW, barH, (float) food / (float) maxFood, 0xFFF59E0B, 0xFF92400E);

        // 3. Rank Badge (in a square container using icon)
        int rankX = hungerX + barW + 12;
        int rankY = y;
        context.fill(rankX, rankY, rankX + rankBoxSize, rankY + rankBoxSize, BG_COLOR);
        drawOutline(context, rankX, rankY, rankBoxSize, rankBoxSize, BORDER_COLOR);
        IconRenderer.drawStar(context, rankX + 3, rankY + 3, 0xFFFACC15);
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
        int x = getLeftPanelX();
        int y = getLeftPanelY();
        int w = getLeftPanelW(collapsed);
        int h = getLeftPanelH(collapsed);

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
                    client.setScreen(new BcasPlayerScreen(client.currentScreen));
                    return true;
                } else if (mouseY >= y + 57 && mouseY <= y + 76) {
                    client.setScreen(new BcasMapScreen(client.currentScreen));
                    return true;
                }
            }
        }

        // Click Rank Square
        if (config.showBottomStatus) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null && client.getWindow() != null) {
                int screenWidth = client.getWindow().getScaledWidth();
                int screenHeight = client.getWindow().getScaledHeight();
                int barW = 68;
                int rankBoxSize = 16;
                int totalWidth = barW + 14 + barW + 12 + rankBoxSize;
                int startX = (screenWidth - totalWidth) / 2;
                int rankX = startX + barW + 14 + barW + 12;
                int rankY = screenHeight - 49;

                if (mouseX >= rankX && mouseX <= rankX + rankBoxSize && mouseY >= rankY && mouseY <= rankY + rankBoxSize) {
                    client.player.sendMessage(Text.literal("§e[Rank]§r Current rank: §6" + config.rankText), false);
                    return true;
                }
            }
        }

        return false;
    }
}
