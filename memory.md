# Project Memory

## Project
BCAS SMP custom HUD for Minecraft Java Edition.

## Confirmed decisions
- User plays Java Edition with Fabric on Minecraft 1.21.11 using TLauncher profile `suraj`.
- Bundled OpenJDK 21 is located at `C:\Users\Admin\AppData\Roaming\.minecraft\runtime\java-runtime-delta\windows\java-runtime-delta`.
- This is a custom client-side HUD mod, not just a resource pack.
- Visual layout faithfully matches the user's provided in-game screenshot mockup:
  - Top-right stats pill stack: FPS (monitor graph icon), Coordinates (map pin), Online/Max Players (users icon), Ping (Wi-Fi icon).
  - Top-right SMP badge: bordered pill with text `BCAS SMP`.
  - Left-side collapsible tool panel: Modern React/Lucide-style icons:
    - Collapse toggle (`<` / `>`)
    - Settings (gear) -> Opens in-game HUD config screen
    - Players (users) -> Player list / info
    - Map (map pin/compass) -> Map toggle or coordinate display
    - Mods (puzzle/grid) -> Mod info or ModMenu integration
  - Bottom status bar directly above hotbar/XP bar:
    - Red heart icon + Health bar (segmented red bar with current/max values)
    - Cyan droplet icon + Mana bar (segmented cyan bar with current/max values)
    - Vertical divider `|` + Rank badge (yellow star + `EXPLORER`)
- Mana mechanic: Client energy that passively regenerates over time and restores when eating food; can sync with server packets/scoreboards if available.
- Vanilla hearts and hunger bar are cleanly hidden/replaced while keeping hotbar and crosshair intact.

## Open decisions
- Server protocol for external mana sync (for now client simulates energy regen and eating food restore, with packet hooks ready).
- Keybind to toggle HUD visibility (default `F4` or `H`) and keybind to open settings (default `RSHIFT` or `O`).
