package dev.stone.practice.config;

import dev.stone.practice.Phantom;
import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.StaticConfig;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 10/06/2025
 * Project: Practice
 */
public class ScoreboardConfig extends StaticConfig {
    public ScoreboardConfig(String filename, ConfigHandler handler) {
        super(new File(Phantom.getInstance().getDataFolder(), filename + ".yml"), handler);
    }

   /* public static List<String> SCOREBOARD_LOBBY_TITLE = Arrays.asList(
            "   §b§lNA Practice§r    ",
            "   §b§lN§f§lA Practice§r    ",
            "   §b§lNA§f§l Practice§r    ",
            "   §b§lNA P§f§lractice§b&r    ",
            "   §b§lNA Pr§f§lactice§b&r    ",
            "   §b§lNA Pra§f§lctice§b&r    ",
            "   §b§lNA Prac§f§ltice§b&r    ",
            "   §b§lNA Pract§f§lice§b&r    ",
            "   §b§lNA Practi§f§lce§b&r    ",
            "   §b§lNA Practic§f§le§b&r    ",
            "   §b§lNA Practice§r    ",
            "   §f§lNA Practice§r    ",
            "   §b§lNA Practice§r    ",
            "   §f§lNA Practice§r    "
    ); */

    public static String SCOREBOARD_LOBBY_TITLE = "&bDevStone Development";

    public static List<String> SCOREBOARD_LOBBY_LINES_DEBUG = Arrays.asList(
            "",
            "&bStatus: <status>",
            "",
            "&bCoins: &c<coins>",
            "",
            "&bGlobal Elo: &c<global_elo>",
            ""
    );

    public static List<String> SCOREBOARD_LOBBY_LINES = Arrays.asList(
            "                       ",
            "{second_color}Online: {main_color}{online}",
            "{second_color}Fighting: {main_color}{in_fights}",
            "{second_color}Waiting: {main_color}{in_queues}",
            "",
            "&fCoins: &b{coins} &e⛀",
            "&fElo: &b%bolt_stats_globalelo%",
            "&fWins: &b%bolt_stats_wins%",
            "",
            "&fLevel: &b{division_color}{level}✮ &7({next_percentage}%&7)",
            "{division_progress}",
            "",
            "&7&omazemc.es"
            // "%animations_<shine start= middle=&f&o end=&f&o normal=&b&o>mazemc.es</shine>%"
    );


}
