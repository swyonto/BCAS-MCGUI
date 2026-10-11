package com.bcas.hud.gui;

import com.bcas.hud.config.HudConfig;
import com.bcas.hud.render.IconRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BcasPlayerScreen extends Screen {
    private final Screen parent;
    private int selectedIndex = 0;
    private final List<PlayerListEntry> playerList = new ArrayList<>();

    public BcasPlayerScreen(Screen parent) {
        super(Text.literal("Players & Ranks"));
        this.parent = parent;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        loadPlayers();

        int modalW = 320;
        int modalH = 196;
        int modalX = (this.width - modalW) / 2;
        int modalY = (this.height - modalH) / 2;

        int btnW = 100;
        int btnH = 20;

        // Close button (positioned in bottom right)
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Close (ESC)"),
                btn -> this.close()
        ).dimensions(modalX + modalW - btnW - 12, modalY + modalH - btnH - 8, btnW, btnH).build());
    }

    private void loadPlayers() {
        playerList.clear();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getNetworkHandler() != null) {
            Collection<PlayerListEntry> entries = client.getNetworkHandler().getPlayerList();
            playerList.addAll(entries);
        }
        if (selectedIndex >= playerList.size()) {
            selectedIndex = Math.max(0, playerList.size() - 1);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) return;
        HudConfig config = HudConfig.get();

        int modalW = 320;
        int modalH = 196;
        int modalX = (this.width - modalW) / 2;
        int modalY = (this.height - modalH) / 2;

        // 1. Grayish modal background with lower opacity (rendered first!)
        context.fill(modalX, modalY, modalX + modalW, modalY + modalH, 0x7518181B);
        drawOutline(context, modalX, modalY, modalW, modalH, 0x4452525B);

        // Header Title Bar
        context.fill(modalX + 1, modalY + 1, modalX + modalW - 1, modalY + 22, 0x4427272A);
        context.drawText(this.textRenderer, "👥 Online Players & Ranks", modalX + 10, modalY + 7, 0xFF38BDF8, true);

        // --- LEFT PANE: PLAYER LIST WITH UP/DOWN/MOUSE SELECTION ---
        int listX = modalX + 10;
        int listY = modalY + 28;
        int listW = 145;
        int rowH = 18;
        int maxVisible = 8;

        context.drawText(this.textRenderer, "§7Players (" + playerList.size() + ") [↑/↓]", listX, listY, 0xFFA1A1AA, false);
        listY += 12;

        // Draw left list container border
        context.fill(listX - 2, listY - 2, listX + listW + 2, listY + (maxVisible * rowH) + 2, 0x5509090B);
        drawOutline(context, listX - 2, listY - 2, listW + 4, (maxVisible * rowH) + 4, 0x3352525B);

        int startIndex = 0;
        if (selectedIndex >= maxVisible) {
            startIndex = selectedIndex - maxVisible + 1;
        }

        for (int i = 0; i < maxVisible; i++) {
            int pIdx = startIndex + i;
            if (pIdx >= playerList.size()) break;

            PlayerListEntry entry = playerList.get(pIdx);
            int rowY = listY + (i * rowH);

            // Mouse hover selection check
            if (mouseX >= listX && mouseX <= listX + listW && mouseY >= rowY && mouseY < rowY + rowH) {
                selectedIndex = pIdx;
            }

            boolean isSelected = (pIdx == selectedIndex);

            // Row background highlight
            if (isSelected) {
                context.fill(listX, rowY, listX + listW, rowY + rowH - 1, 0x5538BDF8);
                drawOutline(context, listX, rowY, listW, rowH - 1, 0xFF38BDF8);
            } else if (i % 2 == 0) {
                context.fill(listX, rowY, listX + listW, rowY + rowH - 1, 0x2227272A);
            }

            // Small user icon
            IconRenderer.drawUsers(context, listX + 4, rowY + 3, isSelected ? 0xFFFFFFFF : 0xFF60A5FA);

            // Player name (truncate if too long)
            String pName = entry.getProfile().name();
            String displayName = this.textRenderer.trimToWidth(pName, listW - 38);
            boolean isSelf = pName.equalsIgnoreCase(player.getName().getString());
            int nameColor = isSelf ? 0xFFFACC15 : (isSelected ? 0xFFFFFFFF : 0xFFE2E8F0);
            context.drawText(this.textRenderer, displayName, listX + 20, rowY + 4, nameColor, false);

            // Ping dot indicator
            int ping = Math.max(0, entry.getLatency());
            int pingDotColor = ping < 80 ? 0xFF4ADE80 : (ping < 160 ? 0xFFFACC15 : 0xFFEF4444);
            context.fill(listX + listW - 10, rowY + 6, listX + listW - 5, rowY + 11, pingDotColor);
        }

        // --- RIGHT PANE: SELECTED USER INFO PANEL ---
        int infoX = modalX + 165;
        int infoY = modalY + 28;
        int infoW = 145;

        context.drawText(this.textRenderer, "§bUser Details", infoX, infoY, 0xFF38BDF8, false);
        infoY += 12;

        // Info Card Box
        context.fill(infoX - 2, infoY - 2, infoX + infoW + 2, infoY + 104, 0x6609090B);
        drawOutline(context, infoX - 2, infoY - 2, infoW + 4, 106, 0x4452525B);

        if (selectedIndex >= 0 && selectedIndex < playerList.size()) {
            PlayerListEntry selected = playerList.get(selectedIndex);
            String selectedName = selected.getProfile().name();
            boolean isSelf = selectedName.equalsIgnoreCase(player.getName().getString());

            // Rank Square Badge inside detail card
            context.fill(infoX + 6, infoY + 6, infoX + 24, infoY + 24, 0x8818181B);
            drawOutline(context, infoX + 6, infoY + 6, 18, 18, 0x66FACC15);
            IconRenderer.drawStar(context, infoX + 10, infoY + 10, 0xFFFACC15);

            // Name
            context.drawText(this.textRenderer, this.textRenderer.trimToWidth(selectedName, 105), infoX + 30, infoY + 7, 0xFFFFFFFF, true);

            // Rank Pill
            Team team = selected.getScoreboardTeam();
            String role = team != null ? team.getDisplayName().getString() : (isSelf ? config.rankText : "EXPLORER");
            context.drawText(this.textRenderer, "§e[" + role + "]", infoX + 30, infoY + 18, 0xFFFACC15, false);

            int detailY = infoY + 34;
            // Status
            context.drawText(this.textRenderer, "§7Status: §a● Online", infoX + 6, detailY, 0xFFA1A1AA, false);
            detailY += 13;

            // Ping / Latency
            int ping = Math.max(0, selected.getLatency());
            String pingQuality = ping < 80 ? "Optimal" : (ping < 160 ? "Good" : "High");
            context.drawText(this.textRenderer, "§7Ping: §f" + ping + "ms §8(" + pingQuality + ")", infoX + 6, detailY, 0xFFA1A1AA, false);
            detailY += 13;

            // Coordinates / Health if self
            if (isSelf) {
                int px = (int) player.getX();
                int py = (int) player.getY();
                int pz = (int) player.getZ();
                context.drawText(this.textRenderer, "§7XYZ: §f" + px + ", " + py + ", " + pz, infoX + 6, detailY, 0xFFA1A1AA, false);
                detailY += 13;
                context.drawText(this.textRenderer, "§7HP: §c" + (int) player.getHealth() + "§7/§c" + (int) player.getMaxHealth(), infoX + 6, detailY, 0xFFA1A1AA, false);
            } else {
                context.drawText(this.textRenderer, "§7Action: §b[ENTER] Copy", infoX + 6, detailY, 0xFFA1A1AA, false);
                detailY += 13;
                context.drawText(this.textRenderer, "§8Click / Enter to copy name", infoX + 6, detailY, 0xFF71717A, false);
            }
        } else {
            context.drawText(this.textRenderer, "§7No player selected", infoX + 10, infoY + 40, 0xFF71717A, false);
        }

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
    public boolean keyPressed(KeyInput input) {
        // UP arrow or W: move selection up
        if (input.key() == 265 || input.key() == 87) {
            if (selectedIndex > 0) {
                selectedIndex--;
            }
            return true;
        }

        // DOWN arrow or S: move selection down
        if (input.key() == 264 || input.key() == 83) {
            if (selectedIndex < playerList.size() - 1) {
                selectedIndex++;
            }
            return true;
        }

        // ENTER: copy player name to clipboard
        if (input.key() == 257) {
            confirmSelectedPlayer();
            return true;
        }

        // Close with ESC (256) or P (80) or Z (90)
        if (input.key() == 256 || input.key() == 80 || input.key() == 90) {
            this.close();
            return true;
        }

        return super.keyPressed(input);
    }

    private void confirmSelectedPlayer() {
        if (selectedIndex >= 0 && selectedIndex < playerList.size()) {
            String name = playerList.get(selectedIndex).getProfile().name();
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.keyboard != null) {
                client.keyboard.setClipboard(name);
            }
            if (client.player != null) {
                client.player.sendMessage(Text.literal("§b[Players]§r Copied name: §e" + name), true);
            }
        }
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        int modalW = 320;
        int modalH = 196;
        int modalX = (this.width - modalW) / 2;
        int modalY = (this.height - modalH) / 2;
        int listX = modalX + 10;
        int listY = modalY + 40;
        int listW = 145;
        int rowH = 18;
        int maxVisible = 8;

        if (click.x() >= listX && click.x() <= listX + listW && click.y() >= listY && click.y() <= listY + (maxVisible * rowH)) {
            int clickedRow = (int) ((click.y() - listY) / rowH);
            int startIndex = 0;
            if (selectedIndex >= maxVisible) {
                startIndex = selectedIndex - maxVisible + 1;
            }
            int targetIdx = startIndex + clickedRow;
            if (targetIdx >= 0 && targetIdx < playerList.size()) {
                selectedIndex = targetIdx;
                confirmSelectedPlayer();
                return true;
            }
        }

        return super.mouseClicked(click, bl);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}
