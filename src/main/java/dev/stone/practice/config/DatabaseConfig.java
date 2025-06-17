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
 * Created: 16/06/2025
 * Project: Practice
 */
public class DatabaseConfig extends StaticConfig {
    public DatabaseConfig(String filename, ConfigHandler handler) {
        super(new File(Phantom.getInstance().getDataFolder(), filename + ".yml"), handler);
    }

    public static boolean URI_MODE = true;

    public static class NORMAL {
        public static String HOST =  "127.0.0.1";
        public static int PORT = 27017;
        public static String DATABASE = "Phantom";
        public static class AUTHENTICATION {
            public static boolean ENABLED = false;
            public static String USERNAME = "";
            public static String PASSWORD = "";
        }
    }
    public static class URI{
        public static String DATABASE = "Phantom";
        public static String CONNECTION_STRING = "mongodb://127.0.0.1:27017/Phantom";
    }
}

