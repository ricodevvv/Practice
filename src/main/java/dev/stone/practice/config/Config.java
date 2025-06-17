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

    public static List<String> WELCOME_MESSAGE = Arrays.asList(
            "Welcome to server",
            "Use /help for recive help"
    );

    public static boolean LOBBY_DISPLAY_PLAYERS = true;

    public static int DEFAULT_GLOBAL_ELO = 1000;

    public static boolean DEBUG = false;

    public static class MATCH_SETTINGS {
        public static int END_DURATION = 100;
    }

    public static List<String > MATCH_ALLOW_BREAK_BLOCKS = Arrays.asList(
            "DEAD_BUSH",
            "GRASS",
            "LONG_GRASS",
            "CACTUS"
    );

    public static String LOBBY_LOCATION = "null";

    public static class KIT_MENU {
        public static String MANAGEMENT_MENU_TITLE = "&bManagement menu";
        public static String KIT_MANAGE_BUTTON = "&bClick for manage <kit>";
        public static int KIT_DETAILS_MENU_SIZE = 36;
    }

    public static class EXPLOSIVE_CONFIG {
       public static class TNT_CONFIG {
           public static double VERTICAL = 2.0;
           public static double YIELD = 4.0;
           public static int FUSE_TICKS = 50;

       }

    }
}
