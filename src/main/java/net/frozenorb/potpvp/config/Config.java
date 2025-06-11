package net.frozenorb.potpvp.config;

import net.frozenorb.potpvp.PotPvPRP;
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
        super(new File(PotPvPRP.getInstance().getDataFolder(), filename + ".yml"), handler);
    }

    public static List<String> WELCOME_MESSAGE = Arrays.asList(
            "Welcome to server",
            "Use /help for recive help"
    );

    public static int DEFAULT_GLOBAL_ELO = 1000;

    public static boolean DEBUG = false;

    public static String LOBBY_LOCATION = "null";

    public static class KIT_MENU {
        public static String MANAGEMENT_MENU_TITLE = "&bManagement menu";
        public static String KIT_MANAGE_BUTTON = "&bClick for manage <kit>";
        public static int KIT_DETAILS_MENU_SIZE = 36;
    }

    public static class EXPLOSIVE_CONFIG {
        public static double TNT_VERTICAL = 2.0;
    }
}
