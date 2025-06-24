package dev.stone.practice.arena.regen;

import dev.stone.practice.Phantom;
import dev.stone.practice.arena.regen.impl.CarbonRegenAdapter;
import dev.stone.practice.arena.regen.impl.legacy.LegacyAdapter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

import java.util.Arrays;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 23/06/2025
 * Project: Practice
 */
@Getter
@RequiredArgsConstructor
public enum RegenerationManager {

    CarbonSpigot("CarbonSpigot", "xyz.refinedev.spigot.api.knockback.KnockbackAPI", new CarbonRegenAdapter()),
    Carbon("Carbon", "xyz.refinedev.spigot.features.combat.CombatAPI", new CarbonRegenAdapter()),
    Default("Phantom Legacy Regeneration", "", new LegacyAdapter());

    public final String name;
    private final String classToCheck;
    private final IArenaRegen regeneration;

    /**
     * Detect which spigot is being used and initialize
     * {@link IArenaRegen} according to the spigot's type
     */
    public static IArenaRegen get() {
        RegenerationManager selected = Arrays
                .stream(RegenerationManager.values())
                .filter(spigot -> !spigot.equals(RegenerationManager.Default) && check(spigot.getClassToCheck()))
                .findFirst()
                .orElse(RegenerationManager.Default);

        Bukkit.getConsoleSender().sendMessage("§8[§bPractice§8] §7Arena Regen Adapter: §f" + selected.getName());

        return selected.getRegeneration();
    }

    /**
     * Checks if a class exists or not
     *
     * @param string The class's package path
     * @return {@link Boolean}
     */
    public static boolean check(String string) {
        if (string.isEmpty()) return false;

        try {
            Class.forName(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
