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
 * Created: 09/06/2025
 * Project: PotPvpReprised
 */
public class Config extends StaticConfig {
    public Config(String filename, ConfigHandler handler) {
        super(new File(Phantom.getInstance().getDataFolder(), filename + ".yml"), handler);
    }

    @Comment("For developers only, do not touch")
    public static boolean DEBUG = false;

    @Comment("the location where all players will be teleported after finishing a match or entering the server")
    public static String LOBBY_LOCATION = "null";

    @Comment("This message will be sent when a player enters the server.")
    public static List<String> WELCOME_MESSAGE = Arrays.asList(
            "Welcome to server",
            "Use /help for recive help"
    );

    @Comment("The default theme for new users")
    public static String DEFAULT_THEME = "AQUA"
;
    public static int RANKED_WINS_REQUIRED = 10;

    @Comment("For developers only, do not touch")
    public static boolean LOBBY_DISPLAY_PLAYERS = true;

    @Comment("the initial elo with which everyone will start")
    public static int DEFAULT_GLOBAL_ELO = 1000;


    public static class MATCH_SETTINGS {
        @Comment("the time in seconds it will take to teleport a player to the lobby after finishing a match")
        public static int END_DURATION = 100;
    }

    @Comment("list of blocks that are allowed to break in a match")
    public static List<String > MATCH_ALLOW_BREAK_BLOCKS = Arrays.asList(
            "DEAD_BUSH",
            "GRASS",
            "LONG_GRASS",
            "CACTUS"
    );

    @Comment("Configuration for tnt")
    public static class EXPLOSIVE_CONFIG {
       public static class TNT_CONFIG {
           @Comment("Vertical impulse that a player will receive from the tnt")
           public static double VERTICAL = 2.0;
           @Comment("Yield value for the TNT")
           public static double YIELD = 4.0;
           @Comment("Fuse ticks value for the TNT")
           public static int FUSE_TICKS = 50;
       }

    }
}
