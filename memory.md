# Project Memory

## Project
BCAS SMP custom HUD for Minecraft Java Edition.

## Confirmed decisions
- User plays Java Edition with Fabric on Minecraft 1.21.11 using TLauncher profile `suraj`.
- Bundled OpenJDK 21 is located at `C:\Users\Admin\AppData\Roaming\.minecraft\runtime\java-runtime-delta\windows\java-runtime-delta`.
- This is a custom client-side HUD mod, not just a resource pack.
- Mod version: 1.3 (`bcas-hud-1.3.jar`) deployed to TLauncher profile `suraj`.
- Visual layout:
  - Background colors: Translucent neutral dark gray (`0x4518181B` / `0x7518181B`) with low opacity and no blue tint.
  - Top-right stats container: Unified single container with reduced row gaps, compact height (~58px), containing FPS, Coordinates, Online Players, and Ping.
  - Left-side collapsible tool panel: 3 tools (Settings, Players & Ranks, Live Map) with dynamic scaling (`COMPACT`, `NORMAL`, `LARGE`) and position toggle (`Center-Left` vs `Top-Left Corner`).
  - Settings Screen: Clean 2-column button layout with direct link to Minecraft Keybinds/Shortcuts.
  - Map Screen: Zoom levels (0.5x, 1.0x, 2.0x), zoom buttons (`+` / `-`), mouse wheel scroll zoom, keyboard zoom, buttons rendered on top of card.
  - Players Screen: Two-pane browser with UP/DOWN arrow navigation, mouse hover selection, ENTER key to copy name, and live user info detail card.
  - Bottom status bar: Health, Hunger (drumstick icon), and Rank square badge aligned to 178px above hotbar.
- Keybind shortcuts: `H` (Toggle HUD), `O` (Settings), `C` (Collapse Toolbar), `Z` (Toolbar Cursor Mode), `M` (Live Map), `P` (Players & Ranks). Full in-game keybind customization supported in Minecraft controls.
