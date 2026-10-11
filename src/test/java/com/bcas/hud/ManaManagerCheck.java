package com.bcas.hud;

import com.bcas.hud.data.ManaManager;

public class ManaManagerCheck {

    public static void main(String[] args) {
        ManaManager manager = ManaManager.getInstance();

        // Check initialization
        manager.setMana(20.0f, 20.0f);
        assert manager.getCurrentMana() == 20.0f : "Expected mana to be 20.0";

        // Check consumption
        boolean used = manager.useMana(6.5f);
        assert used : "Expected useMana to return true";
        assert manager.getCurrentMana() == 13.5f : "Expected mana to be 13.5";

        // Check overdraft prevention
        boolean overused = manager.useMana(20.0f);
        assert !overused : "Expected useMana to fail on overdraft";
        assert manager.getCurrentMana() == 13.5f : "Expected mana to remain 13.5";

        // Check bounds clamping
        manager.setMana(-5.0f, 0.0f);
        assert manager.getCurrentMana() == 0.0f : "Expected mana to clamp to 0";
        assert manager.getMaxMana() == 1.0f : "Expected maxMana to clamp to minimum 1";

        System.out.println("ManaManagerCheck passed successfully!");
    }
}
