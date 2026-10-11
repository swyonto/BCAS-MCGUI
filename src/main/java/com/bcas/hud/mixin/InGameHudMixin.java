package com.bcas.hud.mixin;

import com.bcas.hud.config.HudConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
    private void bcas$hideVanillaHealth(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
        HudConfig config = HudConfig.get();
        if (config.enabled && config.hideVanillaStatusBars) {
            ci.cancel();
        }
    }

    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    private void bcas$hideVanillaFood(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {
        HudConfig config = HudConfig.get();
        if (config.enabled && config.hideVanillaStatusBars) {
            ci.cancel();
        }
    }
}
