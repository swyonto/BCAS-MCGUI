package com.bcas.hud.render;

import net.minecraft.client.gui.DrawContext;

public class IconRenderer {

    public static void drawMonitor(DrawContext context, int x, int y, int color) {
        // Monitor frame: 12x10
        context.fill(x, y, x + 11, y + 8, color);
        context.fill(x + 1, y + 1, x + 10, y + 7, 0xFF141820);
        // Waveform in center
        context.fill(x + 2, y + 4, x + 4, y + 5, color);
        context.fill(x + 4, y + 2, x + 5, y + 6, color);
        context.fill(x + 5, y + 3, x + 6, y + 7, color);
        context.fill(x + 6, y + 4, x + 9, y + 5, color);
        // Stand
        context.fill(x + 4, y + 8, x + 7, y + 9, color);
        context.fill(x + 3, y + 9, x + 8, y + 10, color);
    }

    public static void drawPin(DrawContext context, int x, int y, int color) {
        // Pin head: 8x10
        context.fill(x + 1, y, x + 7, y + 5, color);
        context.fill(x, y + 1, x + 8, y + 4, color);
        // Center hollow
        context.fill(x + 3, y + 2, x + 5, y + 4, 0xFF141820);
        // Point
        context.fill(x + 2, y + 5, x + 6, y + 7, color);
        context.fill(x + 3, y + 7, x + 5, y + 9, color);
        context.fill(x + 3, y + 9, x + 4, y + 10, color);
    }

    public static void drawUsers(DrawContext context, int x, int y, int color) {
        // Left/primary user
        context.fill(x + 2, y, x + 6, y + 4, color); // head
        context.fill(x, y + 5, x + 8, y + 9, color); // body
        // Right/secondary user (slightly offset)
        context.fill(x + 7, y + 1, x + 10, y + 4, color);
        context.fill(x + 8, y + 5, x + 12, y + 9, color);
    }

    public static void drawWifi(DrawContext context, int x, int y, int color) {
        // 3 concentric arcs + dot
        // Outer arc
        context.fill(x + 1, y, x + 10, y + 2, color);
        context.fill(x, y + 1, x + 1, y + 3, color);
        context.fill(x + 10, y + 1, x + 11, y + 3, color);

        // Middle arc
        context.fill(x + 3, y + 3, x + 8, y + 5, color);
        context.fill(x + 2, y + 4, x + 3, y + 6, color);
        context.fill(x + 8, y + 4, x + 9, y + 6, color);

        // Inner dot
        context.fill(x + 4, y + 7, x + 7, y + 10, color);
    }

    public static void drawGear(DrawContext context, int x, int y, int color) {
        // Center body
        context.fill(x + 2, y + 2, x + 8, y + 8, color);
        // Teeth
        context.fill(x + 4, y, x + 6, y + 2, color);
        context.fill(x + 4, y + 8, x + 6, y + 10, color);
        context.fill(x, y + 4, x + 2, y + 6, color);
        context.fill(x + 8, y + 4, x + 10, y + 6, color);
        // Center hole
        context.fill(x + 4, y + 4, x + 6, y + 6, 0xFF141820);
    }

    public static void drawMap(DrawContext context, int x, int y, int color) {
        // Folded 3-panel map
        context.fill(x, y + 1, x + 3, y + 9, color);
        context.fill(x + 3, y, x + 7, y + 8, color);
        context.fill(x + 7, y + 1, x + 10, y + 9, color);
        // Seams
        context.fill(x + 3, y + 1, x + 4, y + 8, 0xFF141820);
        context.fill(x + 7, y + 1, x + 8, y + 8, 0xFF141820);
    }

    public static void drawMods(DrawContext context, int x, int y, int color) {
        // Modern 4-quadrant modules icon
        context.fill(x, y, x + 4, y + 4, color);
        context.fill(x + 6, y, x + 10, y + 4, color);
        context.fill(x, y + 6, x + 4, y + 10, color);
        context.fill(x + 6, y + 6, x + 10, y + 10, color);
    }

    public static void drawHeart(DrawContext context, int x, int y, int color) {
        // 9x8 pixel heart
        context.fill(x + 1, y, x + 4, y + 3, color);
        context.fill(x + 5, y, x + 8, y + 3, color);
        context.fill(x, y + 1, x + 9, y + 4, color);
        context.fill(x + 1, y + 4, x + 8, y + 6, color);
        context.fill(x + 2, y + 6, x + 7, y + 7, color);
        context.fill(x + 3, y + 7, x + 6, y + 8, color);
        context.fill(x + 4, y + 8, x + 5, y + 9, color);
    }

    public static void drawDroplet(DrawContext context, int x, int y, int color) {
        // 7x9 water drop
        context.fill(x + 3, y, x + 4, y + 2, color);
        context.fill(x + 2, y + 2, x + 5, y + 4, color);
        context.fill(x + 1, y + 4, x + 6, y + 7, color);
        context.fill(x, y + 5, x + 7, y + 8, color);
        context.fill(x + 1, y + 8, x + 6, y + 9, color);
        context.fill(x + 2, y + 9, x + 5, y + 10, color);
    }

    public static void drawStar(DrawContext context, int x, int y, int color) {
        // 9x9 star
        context.fill(x + 4, y, x + 5, y + 9, color);
        context.fill(x, y + 3, x + 9, y + 5, color);
        context.fill(x + 2, y + 1, x + 7, y + 8, color);
        context.fill(x + 1, y + 6, x + 8, y + 8, color);
    }

    public static void drawChevron(DrawContext context, int x, int y, boolean pointingRight, int color) {
        if (pointingRight) {
            context.fill(x, y, x + 2, y + 7, color);
            context.fill(x + 2, y + 1, x + 4, y + 6, color);
            context.fill(x + 4, y + 2, x + 6, y + 5, color);
        } else {
            context.fill(x + 4, y, x + 6, y + 7, color);
            context.fill(x + 2, y + 1, x + 4, y + 6, color);
            context.fill(x, y + 2, x + 2, y + 5, color);
        }
    }
}
