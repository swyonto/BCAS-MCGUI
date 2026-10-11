package com.bcas.hud;

import com.bcas.hud.config.HudConfig;
import com.bcas.hud.data.ManaManager;
import com.bcas.hud.gui.BcasSettingsScreen;
import com.bcas.hud.gui.BcasToolbarScreen;
import com.bcas.hud.render.HudOverlayRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class BcasHudClient implements ClientModInitializer {
    public static final String MOD_ID = "bcas-hud";

    private static KeyBinding toggleHudKey;
    private static KeyBinding openSettingsKey;
    private static KeyBinding toggleToolbarKey;
    private static KeyBinding interactiveToolbarKey;

    @Override
    public void onInitializeClient() {
        HudConfig.load();

        KeyBinding.Category category = KeyBinding.Category.create(Identifier.of(MOD_ID, "general"));

        // Key 1: Toggle entire HUD (H)
        toggleHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bcas-hud.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                category
        ));

        // Key 2: Open Settings GUI (O)
        openSettingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bcas-hud.settings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_O,
                category
        ));

        // Key 3: Direct button to Toggle/Collapse Left Tool Bar (< / >) (C)
        toggleToolbarKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bcas-hud.toggle_toolbar",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                category
        ));

        // Key 4: Open Tool Bar Cursor Mode to click < or icons (Z)
        interactiveToolbarKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bcas-hud.interactive_toolbar",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                category
        ));

        // Register HUD Element
        HudElementRegistry.addLast(
                Identifier.of(MOD_ID, "main_hud"),
                (context, tickCounter) -> HudOverlayRenderer.render(context, tickCounter.getTickProgress(true))
        );

        // Mouse clicks on screens (when cursor is available)
        net.fabricmc.fabric.api.client.screen.v1.ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents.allowMouseClick(screen).register((scr, click) -> {
                return !HudOverlayRenderer.handleMouseClick(click.x(), click.y(), click.button());
            });
        });

        // Tick loop for energy/mana regen and key listening
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                ManaManager.getInstance().tick(client.player);
            }

            while (toggleHudKey.wasPressed()) {
                HudConfig config = HudConfig.get();
                config.enabled = !config.enabled;
                HudConfig.save();
                if (client.player != null) {
                    client.player.sendMessage(
                            Text.literal("§b[BCAS HUD]§r " + (config.enabled ? "§aEnabled" : "§cDisabled")),
                            true
                    );
                }
            }

            while (openSettingsKey.wasPressed()) {
                client.setScreen(new BcasSettingsScreen(client.currentScreen));
            }

            while (toggleToolbarKey.wasPressed()) {
                HudOverlayRenderer.toggleToolbar();
            }

            while (interactiveToolbarKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new BcasToolbarScreen());
                } else if (client.currentScreen instanceof BcasToolbarScreen) {
                    client.setScreen(null);
                }
            }
        });
    }
}
