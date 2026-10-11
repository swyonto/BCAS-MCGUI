# Development Rules

1. Target Minecraft Java 1.21.11 only unless the user asks to expand version support.
2. Use Fabric APIs and dependency versions compatible with 1.21.11. Check current official documentation and APIs before relying on examples, since HUD rendering APIs change across Minecraft versions.
3. Register and draw HUD elements through the supported Fabric HUD rendering API for the target version; do not copy outdated `HudRenderCallback` tutorials without verifying compatibility.
4. Keep the HUD client-side unless a feature requires server data. Clearly identify any server-side companion mod/plugin requirement.
5. Never present placeholder mana, rank, ping, or player-count values as real data.
6. Do not render a player portrait or numeric health/mana totals. Keep rank as unboxed text and server identity text-only.
7. Scale and position the interface relative to the current GUI viewport; preserve the crosshair, vanilla hotbar, and important gameplay visibility.
8. Keep rendering code organized by HUD component and keep configuration separate from drawing logic.
9. Provide toggles or sensible visibility behavior for HUD elements, and make the tool panel collapse control work.
10. Do not add unrelated features or alter gameplay mechanics without an explicit request.
11. Test changes in the target Minecraft/Fabric version before claiming they work. Keep a note of unresolved compatibility or server integration issues.
