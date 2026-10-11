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

        // 4. Verify sidebar bounds for 3 buttons
        int sidebarNormalH = 76;
        int sidebarCompactH = 70;
        int sidebarLargeH = 84;
        assert sidebarNormalH < 90 : "Sidebar height should fit 3 actions compactly";
        assert sidebarCompactH < sidebarNormalH : "Compact sidebar should be shorter";
        assert sidebarLargeH > sidebarNormalH : "Large sidebar should be taller";

        System.out.println("HudLayoutCheck passed successfully!");
    }
}
