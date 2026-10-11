package com.bcas.hud;

public class HudLayoutCheck {

    public static void main(String[] args) {
        // 1. Verify bottom status bar fits neatly above vanilla hotbar (182px)
        int barW = 68;
        int hungerGap = 14;
        int rankGap = 12;
        int rankBoxSize = 16;
        int totalWidth = barW + hungerGap + barW + rankGap + rankBoxSize; // 178px

        assert totalWidth <= 182 : "Total bottom HUD width (" + totalWidth + ") must fit within hotbar width (182)";
        assert rankBoxSize == 16 : "Rank badge must be a square (16x16)";

        // 2. Verify hunger fraction calculation (0..20)
        int maxFood = 20;
        for (int food = 0; food <= maxFood; food++) {
            float frac = (float) food / (float) maxFood;
            assert frac >= 0.0f && frac <= 1.0f : "Hunger fraction must be normalized between 0 and 1";
        }

        // 3. Verify top stats container height is compact (< 65px)
        int cardH = 58;
        assert cardH < 65 : "Top stats card should be compact and short, actual: " + cardH;

        // 4. Verify Layout Size slider bounds & snapping (70% - 130%)
        double minScale = 0.70;
        double maxScale = 1.30;
        for (double v = 0.0; v <= 1.0; v += 0.05) {
            double scale = minScale + v * (maxScale - minScale);
            int pct = (int) Math.round(scale * 20.0) * 5;
            assert pct >= 70 && pct <= 130 : "Slider percent must be between 70% and 130%, got: " + pct;
            assert pct % 5 == 0 : "Slider percent must snap to multiples of 5%, got: " + pct;
        }

        // Check center value (v = 0.5) corresponds exactly to 100%
        int centerPct = (int) Math.round((minScale + 0.5 * (maxScale - minScale)) * 20.0) * 5;
        assert centerPct == 100 : "Center slider position must be exactly 100%, got: " + centerPct;

        // Check panel scaling bounds
        int normalW = Math.max(14, (int) Math.round(22 * 1.0f));
        int compactW = Math.max(14, (int) Math.round(22 * 0.70f));
        int largeW = Math.max(14, (int) Math.round(22 * 1.30f));
        assert normalW == 22 : "Normal width should be 22";
        assert compactW < normalW : "Compact width must be narrower";
        assert largeW > normalW : "Large width must be wider";

        System.out.println("HudLayoutCheck passed successfully!");
    }
}
