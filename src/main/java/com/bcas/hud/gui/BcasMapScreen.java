package com.bcas.hud.gui;

import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;

public class BcasMapScreen extends Screen {
    private final Screen parent;
    
    // Zoom Levels:
    // 0 = 0.5x (wide overview, 96x96 blocks, 1.33px/block)
    // 1 = 1.0x (normal, 64x64 blocks, 2px/block)
    // 2 = 2.0x (close-up, 32x32 blocks, 4px/block)
    private int zoomLevel = 1;
    private static final int CANVAS_SIZE = 128;
    
    private int currentGridSize = 64;
    private int currentCellSize = 2;
    private int[][] cachedMapColors = new int[64][64];
    private boolean mapGenerated = false;

    public BcasMapScreen(Screen parent) {
        super(Text.literal("Minecraft World Map"));
        this.parent = parent;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    protected void init() {
        updateZoomParameters();
        generateMapData();

        int modalW = 300;
        int modalH = 194;
        int modalX = (this.width - modalW) / 2;
        int modalY = (this.height - modalH) / 2;

        int btnX = modalX + 160;

        // Zoom In button (+)
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("+"),
                btn -> zoomIn()
        ).dimensions(modalX + 14 + CANVAS_SIZE - 46, modalY + 34, 20, 18).build());

        // Zoom Out button (-)
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("-"),
                btn -> zoomOut()
        ).dimensions(modalX + 14 + CANVAS_SIZE - 22, modalY + 34, 20, 18).build());

        // Refresh Map button
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Refresh"),
                btn -> generateMapData()
        ).dimensions(btnX, modalY + modalH - 52, 120, 20).build());

        // Close button
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Close (ESC)"),
                btn -> this.close()
        ).dimensions(btnX, modalY + modalH - 28, 120, 20).build());
    }

    private void updateZoomParameters() {
        if (zoomLevel == 2) {
            currentGridSize = 32;
            currentCellSize = 4;
        } else if (zoomLevel == 0) {
            currentGridSize = 128;
            currentCellSize = 1;
        } else {
            currentGridSize = 64;
            currentCellSize = 2;
        }
        cachedMapColors = new int[currentGridSize][currentGridSize];
    }

    private void zoomIn() {
        if (zoomLevel < 2) {
            zoomLevel++;
            updateZoomParameters();
            generateMapData();
        }
    }

    private void zoomOut() {
        if (zoomLevel > 0) {
            zoomLevel--;
            updateZoomParameters();
            generateMapData();
        }
    }

    private void generateMapData() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        ClientWorld world = client.world;
        if (player == null || world == null) return;

        int playerX = player.getBlockX();
        int playerZ = player.getBlockZ();
        int halfGrid = currentGridSize / 2;

        int[][] heights = new int[currentGridSize][currentGridSize];

        // 1. Sample blocks and heights using Minecraft MapColor logic
        for (int dz = 0; dz < currentGridSize; dz++) {
            for (int dx = 0; dx < currentGridSize; dx++) {
                int worldX = playerX - halfGrid + dx;
                int worldZ = playerZ - halfGrid + dz;

                int topY = world.getTopY(Heightmap.Type.MOTION_BLOCKING, worldX, worldZ);
                if (topY <= world.getBottomY()) {
                    topY = player.getBlockY();
                }

                BlockPos.Mutable mut = new BlockPos.Mutable(worldX, topY, worldZ);
                BlockState state = world.getBlockState(mut);

                while (topY > world.getBottomY() && (state.isAir() || state.getMapColor(world, mut) == MapColor.CLEAR)) {
                    topY--;
                    mut.setY(topY);
                    state = world.getBlockState(mut);
                }

                heights[dx][dz] = topY;
                MapColor mapColor = state.getMapColor(world, mut);
                cachedMapColors[dx][dz] = mapColor.color;
            }
        }

        // 2. Apply Minecraft slope shading
        for (int dz = 0; dz < currentGridSize; dz++) {
            for (int dx = 0; dx < currentGridSize; dx++) {
                int baseColor = cachedMapColors[dx][dz];
                if (baseColor == 0) {
                    cachedMapColors[dx][dz] = 0xFF18181B;
                    continue;
                }

                int prevY = (dz > 0) ? heights[dx][dz - 1] : heights[dx][dz];
                int curY = heights[dx][dz];

                float factor = 1.0f;
                if (curY > prevY) factor = 1.15f;
                else if (curY < prevY) factor = 0.85f;

                int r = Math.min(255, (int) (((baseColor >> 16) & 0xFF) * factor));
                int g = Math.min(255, (int) (((baseColor >> 8) & 0xFF) * factor));
                int b = Math.min(255, (int) ((baseColor & 0xFF) * factor));

                cachedMapColors[dx][dz] = 0xFF000000 | (r << 16) | (g << 8) | b;
            }
        }

        mapGenerated = true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        ClientWorld world = client.world;
        if (player == null || world == null) return;

        int modalW = 300;
        int modalH = 194;
        int modalX = (this.width - modalW) / 2;
        int modalY = (this.height - modalH) / 2;

        // 1. Grayish modal background with lower opacity (rendered first!)
        context.fill(modalX, modalY, modalX + modalW, modalY + modalH, 0x7518181B);
        drawOutline(context, modalX, modalY, modalW, modalH, 0x4452525B);

        // Header Title
        context.fill(modalX + 1, modalY + 1, modalX + modalW - 1, modalY + 22, 0x4427272A);
        context.drawText(this.textRenderer, "🗺 Minecraft Live Map", modalX + 10, modalY + 7, 0xFF38BDF8, true);

        // Map Canvas Box (128x128 pixels)
        int mapCanvasX = modalX + 14;
        int mapCanvasY = modalY + 32;

        // Map Border frame (grayish)
        context.fill(mapCanvasX - 2, mapCanvasY - 2, mapCanvasX + CANVAS_SIZE + 2, mapCanvasY + CANVAS_SIZE + 2, 0x8809090B);
        drawOutline(context, mapCanvasX - 2, mapCanvasY - 2, CANVAS_SIZE + 4, CANVAS_SIZE + 4, 0x5552525B);

        // Render Map Pixels
        if (mapGenerated) {
            for (int dz = 0; dz < currentGridSize; dz++) {
                for (int dx = 0; dx < currentGridSize; dx++) {
                    int px = mapCanvasX + (dx * currentCellSize);
                    int py = mapCanvasY + (dz * currentCellSize);
                    int color = cachedMapColors[dx][dz];
                    context.fill(px, py, px + currentCellSize, py + currentCellSize, color);
                }
            }
        }

        // Compass 'N' Marker at top of map
        context.drawText(this.textRenderer, "§c▲ N", mapCanvasX + (CANVAS_SIZE / 2) - 8, mapCanvasY - 10, 0xFFEF4444, false);

        // Draw Player Marker in the Center
        int playerMapX = mapCanvasX + (CANVAS_SIZE / 2);
        int playerMapY = mapCanvasY + (CANVAS_SIZE / 2);
        drawPlayerMarker(context, playerMapX, playerMapY, player.getYaw());

        // --- RIGHT SIDE: MAP INFO PANEL ---
        int infoX = modalX + 160;
        int infoY = modalY + 32;

        int px = (int) player.getX();
        int py = (int) player.getY();
        int pz = (int) player.getZ();

        context.drawText(this.textRenderer, "§7Coordinates:", infoX, infoY, 0xFFA1A1AA, false);
        context.drawText(this.textRenderer, "§f" + px + ", " + py + ", " + pz, infoX, infoY + 10, 0xFFFFFFFF, false);

        infoY += 23;
        RegistryEntry<Biome> biomeEntry = world.getBiome(player.getBlockPos());
        String biomePath = biomeEntry.getKey().map(k -> k.getValue().getPath()).orElse("Unknown");
        String biomeFormatted = formatName(biomePath);
        context.drawText(this.textRenderer, "§7Biome:", infoX, infoY, 0xFFA1A1AA, false);
        context.drawText(this.textRenderer, "§e" + biomeFormatted, infoX, infoY + 10, 0xFFFACC15, false);

        infoY += 23;
        Direction facing = player.getHorizontalFacing();
        context.drawText(this.textRenderer, "§7Facing:", infoX, infoY, 0xFFA1A1AA, false);
        context.drawText(this.textRenderer, "§a" + facing.asString().toUpperCase(), infoX, infoY + 10, 0xFF4ADE80, false);

        infoY += 23;
        String zoomStr = zoomLevel == 2 ? "2.0x (Close)" : (zoomLevel == 0 ? "0.5x (Wide)" : "1.0x (Normal)");
        context.drawText(this.textRenderer, "§7Zoom: §b" + zoomStr, infoX, infoY, 0xFF94A3B8, false);

        // Render buttons ON TOP of background so they are bright and interactive
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount > 0) {
            zoomIn();
            return true;
        } else if (verticalAmount < 0) {
            zoomOut();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    private void drawPlayerMarker(DrawContext context, int centerX, int centerY, float yaw) {
        context.fill(centerX - 2, centerY - 2, centerX + 3, centerY + 3, 0xFFEF4444);
        context.fill(centerX - 1, centerY - 1, centerX + 2, centerY + 2, 0xFFFFFFFF);

        float rad = (yaw + 90.0f) * (float) Math.PI / 180.0f;
        int tipX = centerX + (int) Math.round(Math.cos(rad) * 5.0);
        int tipY = centerY + (int) Math.round(Math.sin(rad) * 5.0);
        context.fill(tipX - 1, tipY - 1, tipX + 2, tipY + 2, 0xFFFACC15);
    }

    private String formatName(String path) {
        if (path == null || path.isEmpty()) return "Unknown";
        String[] words = path.replace('_', ' ').split(" ");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (!w.isEmpty()) {
                sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
            }
        }
        return sb.toString().trim();
    }

    private void drawOutline(DrawContext context, int x, int y, int w, int h, int color) {
        context.fill(x, y, x + w, y + 1, color);
        context.fill(x, y + h - 1, x + w, y + h, color);
        context.fill(x, y + 1, x + 1, y + h, color);
        context.fill(x + w - 1, y, x + w, y + h, color);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        // Zoom in with + or =
        if (input.key() == 334 || input.key() == 61) {
            zoomIn();
            return true;
        }
        // Zoom out with - or _
        if (input.key() == 333 || input.key() == 45) {
            zoomOut();
            return true;
        }
        // Close with ESC or Z or M
        if (input.key() == 256 || input.key() == 90 || input.key() == 77) {
            this.close();
            return true;
        }
        return super.keyPressed(input);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}
