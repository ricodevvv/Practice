package net.frozenorb.potpvp.config;

import net.frozenorb.potpvp.PotPvPRP;
import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.StaticConfig;

import java.io.File;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 09/06/2025
 * Project: PotPvpReprised
 */
public class Lenguage extends StaticConfig {
    public Lenguage(String filename, ConfigHandler handler) {
        super(new File(PotPvPRP.getInstance().getDataFolder(), filename + ".yml"), handler);
    }

    public static String WELCOME_MESSAGE = "Welcome to server";

    public static class KIT_MENU {
        public static String MANAGEMENT_MENU_TITLE = "&bManagement menu";
        public static String KIT_MANAGE_BUTTON = "&bClick for manage <kit>";
    }

    public static class EXPLOSIVE_CONFIG {
        public static double TNT_VERTICAL = 2.0;
    }
}
