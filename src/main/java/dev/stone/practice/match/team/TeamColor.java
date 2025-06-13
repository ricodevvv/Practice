package dev.stone.practice.match.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import dev.stone.practice.config.Lenguaje;
import dev.stone.practice.util.CC;
import org.bukkit.DyeColor;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 11/06/2025
 * Project: Practice
 */
@Getter
@AllArgsConstructor
public enum TeamColor {

    RED(CC.RED, 16711680, DyeColor.RED, Lenguaje.TEAM_COLOR_IDENTIFIER.RED_IDENTIFIER, CC.RED + "[RED]", DyeColor.RED.ordinal()),
    BLUE(CC.BLUE, 255, DyeColor.BLUE, Lenguaje.TEAM_COLOR_IDENTIFIER.BLUE_IDENTIFIER, CC.BLUE + "[BLUE]", DyeColor.BLUE.ordinal()),
    GREEN(CC.GREEN, 32768, DyeColor.GREEN, Lenguaje.TEAM_COLOR_IDENTIFIER.GREEN_IDENTIFIER, CC.GREEN + "[GREEN]", DyeColor.GREEN.ordinal()),
    YELLOW(CC.YELLOW, 16776960, DyeColor.YELLOW, Lenguaje.TEAM_COLOR_IDENTIFIER.YELLOW_IDENTIFIER, CC.YELLOW + "[YELLOW]", DyeColor.YELLOW.ordinal()),
    AQUA(CC.AQUA, 65535, DyeColor.CYAN, Lenguaje.TEAM_COLOR_IDENTIFIER.AQUA_IDENTIFIER, CC.AQUA + "[AQUA]", DyeColor.CYAN.ordinal()),
    WHITE(CC.WHITE, 16777215, DyeColor.WHITE, Lenguaje.TEAM_COLOR_IDENTIFIER.WHITE_IDENTIFIER, CC.WHITE + "[WHITE]", DyeColor.WHITE.ordinal()),
    PINK(CC.PINK, 8388736, DyeColor.PINK, Lenguaje.TEAM_COLOR_IDENTIFIER.PINK_IDENTIFIER, CC.PINK + "[PINK]", DyeColor.PINK.ordinal()),
    GRAY(CC.DARK_GRAY, 8421504, DyeColor.GRAY, Lenguaje.TEAM_COLOR_IDENTIFIER.GRAY_IDENTIFIER, CC.DARK_GRAY + "[GRAY]", DyeColor.GRAY.ordinal());

    private final String color;
    private final int rgb;
    private final DyeColor dyeColor;
    private final String teamName;
    private final String teamLogo;
    private final int durability;
}
