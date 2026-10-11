# Tasks

## Phase 1 — Project setup
- [x] Generate/open a Fabric mod project targeting Minecraft 1.21.11.
- [x] Confirm compatible Fabric Loader, Fabric API, Loom, mappings, and Java 21 configuration.
- [x] Build environment and auto-deploy to TLauncher profile `suraj`.

## Phase 2 — HUD foundation
- [x] Register a custom HUD layer using the supported 1.21.11 Fabric HUD API (`HudElementRegistry`).
- [x] Add a visibility toggle/keybind (`H`), settings keybind (`O`), and persistent configuration (`bcas-hud.json`).
- [x] Verify rendering adapts to GUI scale and screen dimensions.

## Phase 3 — Visual components
- [x] Implement the minimal top-right stats layout and icon assets (monitor, map pin, users, wifi).
- [x] Display live FPS and coordinates.
- [x] Display live player count and ping.
- [x] Implement the slim collapsible left-side tool panel with modern React/Lucide-style icons (Setting, Players, Map, Mods).
- [x] Implement health and mana bars above the hotbar with segmented bars and clean indicators.
- [x] Implement the text-only rank label with yellow star and `EXPLORER` badge.
- [x] Add the bordered `BCAS SMP` server badge.
- [x] Cleanly hide vanilla hearts & food to match mockup while keeping crosshair and hotbar unobstructed.

## Phase 4 — Server data integration
- [x] Client energy/mana manager with passive regen and restore upon eating food.
- [x] Hook for external server protocol sync when packet/scoreboard is provided.

## Phase 5 — Polish and delivery
- [x] In-game GUI settings screen (`BcasSettingsScreen`) accessible via button or `O` key.
- [x] Built remapped mod JAR (`bcas-hud-1.0.0.jar`) and deployed to TLauncher profile `suraj`.
