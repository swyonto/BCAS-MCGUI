package com.bcas.hud.gui;

import com.bcas.hud.render.HudOverlayRenderer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;

public class BcasToolbarScreen extends Screen {

    public BcasToolbarScreen() {
        super(Text.literal("BCAS Toolbar Interactive Screen"));
    }

    @Override
    public boolean shouldPause() {
        // Do not pause the game
        return false;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // Leave background transparent so game remains visible
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        HudOverlayRenderer.renderInteractiveHighlights(context, mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(Click click, boolean bl) {
        if (HudOverlayRenderer.handleMouseClick(click.x(), click.y(), click.button())) {
            return true;
        }
        // Clicking outside closes the interactive cursor mode
        this.close();
        return true;
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        // Close with Z (90) or ESC (256)
        if (input.key() == 90 || input.key() == 256) {
            this.close();
            return true;
        }
        return super.keyPressed(input);
    }
}
