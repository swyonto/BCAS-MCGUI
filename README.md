# BCAS SMP Custom HUD

A modern, minimal, and responsive client-side HUD mod for Minecraft Java Edition, tailored for the BCAS SMP. Built with Fabric for Minecraft **1.21.11**.

---

## 🎮 Controls & Keybinds

| Key | Action | Description |
| :---: | :--- | :--- |
| **`C`** | **Toggle / Collapse Tool Bar** | Instantly collapses (`<`) or expands (`>`) the left sidebar during gameplay. |
| **`Z`** | **Interactive Cursor Mode** | Frees the mouse cursor over your live screen to click the toolbar icons without pausing the game. |
| **`O`** | **Open Settings GUI** | Opens the in-game HUD configuration screen to toggle modules on/off. |
| **`H`** | **Toggle Entire HUD** | Instantly toggles the visibility of the whole custom HUD. |

> **Note:** All keybinds can be customized at any time in **Options** → **Controls** → **Key Binds** under the **`BCAS SMP HUD`** category.

---

## ✨ Features

### 1. Top-Right Live Stats
- **Monitor / Waveform Icon**: Live FPS.
- **Location Pin Icon**: Real-time integer player coordinates (`X, Y, Z`).
- **Users Icon**: Live online players count (`08/24`).
- **Wi-Fi Icon**: Live server connection latency / ping in milliseconds.
- **Server Badge**: Bordered identity badge with `BCAS SMP`.

### 2. Left Collapsible Tool Panel
- Clickable `<` / `>` button or press **`C`** to fold/unfold against the screen edge.
- **Settings (Gear)**: Opens in-game settings screen.
- **Players (Users)**: Shows online player count/list.
- **Map (Map)**: Displays coordinates in chat.
- **Mods (Modules)**: Displays active mod info.
- Smooth hover highlights and tooltips in cursor mode (**`Z`**).

### 3. Bottom Status Area
- **Health**: Red heart icon with segmented horizontal red health bar.
- **Mana / Energy**: Cyan droplet icon with segmented cyan mana bar.
  - Regenerates passively over time.
  - Restores additional energy when consuming food.
- **Rank Badge**: Thin divider `|` with a yellow star `⭐` and bold `EXPLORER` rank text.
- Replaces vanilla hearts and hunger bar to keep the display clean while preserving the hotbar, crosshair, and XP bar.

---

## 🚀 Building & Installation

### Requirements
- **Minecraft**: Java Edition 1.21.11
- **Mod Loader**: Fabric Loader (>= 0.17.0, tested on 0.19.5)
- **Dependencies**: Fabric API 1.21.11, Java 21

### Install Mod Jar
1. Download or compile `bcas-hud-1.3.jar`.
2. Place it into your `.minecraft/mods` folder (or your TLauncher profile mods folder, e.g., `.minecraft/versions/<profile>/mods`).
3. Ensure **Fabric API** is also in your mods folder.
4. Launch Minecraft with your Fabric profile!

### Build from Source
```bash
# Build mod jar (output in build/libs/)
./gradlew build

# Build and copy directly into your local Minecraft mods folder
./gradlew deployMod
```
