package com.bcas.hud.data;

import net.minecraft.client.network.ClientPlayerEntity;

public class ManaManager {
    private static final ManaManager INSTANCE = new ManaManager();

    private float currentMana = 20.0f;
    private float maxMana = 20.0f;
    private int tickCounter = 0;
    private int lastFoodLevel = -1;

    public static ManaManager getInstance() {
        return INSTANCE;
    }

    public void tick(ClientPlayerEntity player) {
        if (player == null) return;

        tickCounter++;

        // Passive regen: restore 0.2 mana every second (20 ticks)
        if (tickCounter % 20 == 0) {
            if (currentMana < maxMana) {
                currentMana = Math.min(maxMana, currentMana + 0.5f);
            }
        }

        // Detect food consumption: when player food increases or player is eating
        int currentFood = player.getHungerManager().getFoodLevel();
        if (lastFoodLevel >= 0 && currentFood > lastFoodLevel) {
            // Restore mana when eating food
            float restored = (currentFood - lastFoodLevel) * 2.0f;
            currentMana = Math.min(maxMana, currentMana + restored);
        }
        lastFoodLevel = currentFood;
    }

    public boolean useMana(float amount) {
        if (currentMana >= amount) {
            currentMana -= amount;
            return true;
        }
        return false;
    }

    public void setMana(float current, float max) {
        this.currentMana = Math.max(0, current);
        this.maxMana = Math.max(1, max);
    }

    public float getCurrentMana() {
        return currentMana;
    }

    public float getMaxMana() {
        return maxMana;
    }
}
