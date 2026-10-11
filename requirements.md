# Requirements — BCAS SMP HUD

## Goal
Create a clean, modern Minecraft-style HUD for a Java Edition SMP, based on the user-provided visual mockup.

## Target platform
- Minecraft Java Edition 1.21.11.
- Fabric Loader and Fabric API versions compatible with Minecraft 1.21.11.
- Client-side Fabric mod for rendering and configuration.

## HUD requirements
- Preserve the normal first-person view, crosshair, and vanilla hotbar; cleanly hide default vanilla hearts and hunger bar to prevent duplicate clutter.
- Bottom status area (above hotbar):
  - Health: Red heart icon with segmented horizontal health bar.
  - Mana: Cyan droplet icon with segmented horizontal mana bar.
  - Rank: Vertical divider `|` followed by a yellow star icon and bold `EXPLORER` text badge.
  - Values: Clean bar visualization matching mockup.
- Upper-right status:
  - Modern stacked translucent pills with crisp modern icons and live values:
    1. Monitor/graph icon with live FPS (e.g. `144`).
    2. Location pin icon with integer player coordinates (e.g. `124, 64, -238`).
    3. Users/group icon with live player count (e.g. `08/24`).
    4. Wi-Fi icon with live ping in ms (e.g. `32 ms`).
  - SMP identity badge: Bordered outline pill with bold text `BCAS SMP`.
- Left edge:
  - Slim modern translucent collapsible sidebar with collapse toggle `<` / `>` at top.
  - Modern React/Lucide-style action buttons:
    1. Settings (gear) -> opens HUD config / settings screen.
    2. Players (users) -> shows player list overview.
    3. Map (map pin/compass) -> toggles map / coordinates display.
    4. Mods (puzzle/grid) -> opens mods overview or ModMenu.
- Scaling and responsiveness:
  - Adapts accurately to different GUI scales (Auto, 1, 2, 3, 4) and screen resolutions.
- Controls:
  - Keybinds to toggle HUD visibility and open settings screen.

## Data and mechanics
- FPS, player coordinates, player health, online player count, and ping read live from Minecraft client.
- Mana: Starts at 20 (max 20), passively regenerates over time and restores when player eats food (mimicking an energy/power mechanic). Supports external server sync when available.
- Rank: Defaults to `EXPLORER` or server team/prefix if present.

## Acceptance criteria
- Loads in a Minecraft 1.21.11 Fabric client without errors.
- HUD elements render at the intended locations and remain legible at common resolutions/scales.
- Hotbar and crosshair remain unobstructed.
- Numeric health/mana labels and player portrait are absent.
- FPS, coordinates, player count, and ping display the correct live values.
- Mana and rank display server-provided values when the integration protocol is available.
- The left tool panel can collapse and expand.
- HUD visibility and appearance can be configured or toggled by the player.
