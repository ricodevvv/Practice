package dev.stone.practice.profile.settings;

import lombok.Getter;
import org.bukkit.Material;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 22/06/2025
 * Project: Practice
 */
@Getter
public enum Settings {
    SHOW_SCOREBOARD("Show Scoreboard", Material.ITEM_FRAME, "Enable or Disable Scoreboard."),
    ALLOW_SPECTATORS("Allow Spectators", Material.REDSTONE_TORCH_ON, "Allow players to spectate you."),
    SPECTATOR_MESSAGES("Toggle Spectators messages", Material.BOOK, "Show or hide spectators messages."),
    SHOW_PLAYERS("Toggle Players Visibility", Material.EYE_OF_ENDER, "Show or Hide players."),
    ALLOW_DUELS("Allow Duels", Material.DIAMOND_SWORD, "Allow Duel Requests."),
    TIME_CHANGE("Change Time", Material.WATCH, "Change Ping Range."),
    THEME("Select Theme", Material.BOOK, "Select a color to see it in messages."),
    PING_RANGE("Change Ping Range", Material.STICK, "Change Ping Range when queueing."),
    MENU_SOUNDS("Menu Sounds", Material.RECORD_3, "Toggle Menu Sounds when clicking or opening.");

    private final String name;
    private final Material material;
    private final String description;

    Settings(String name, Material material, String description) {
        this.name = name;
        this.material = material;
        this.description = description;
    }
}
