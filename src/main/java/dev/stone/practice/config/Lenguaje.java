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
 * Created: 11/06/2025
 * Project: Practice
 */
public class Lenguaje extends StaticConfig {
    public Lenguaje(String filename, ConfigHandler handler) {
        super(new File(Phantom.getInstance().getDataFolder(), filename + ".yml"), handler);
    }

    public static class TEAM_COLOR_IDENTIFIER {
        public static String RED_IDENTIFIER = "Red";
        public static String BLUE_IDENTIFIER = "Blue";
        public static String GREEN_IDENTIFIER = "Green";
        public static String YELLOW_IDENTIFIER = "Yellow";
        public static String AQUA_IDENTIFIER = "Aqua";
        public static String WHITE_IDENTIFIER = "White";
        public static String PINK_IDENTIFIER = "Pink";
        public static String GRAY_IDENTIFIER = "Gray";
    }

    public static class MATCH_MESSAGES {
        public static String RESPAWN_IN_MESSAGE = "<theme>Respawn in <seconds>";
        public static String CANNOT_BREAK_OWN_BED = "&cYou cannot break your own bed!";
        public static String BED_BREAK_TITLE = "&cBed Destroyed！";
        public static String BED_BREAK_SUBTITLE = "&fYou will not respawn after die！";
        public static List<String> BED_BREAK_MESSAGE = Arrays.asList(
          "",
          "&f&lBed Destroyed > {0}'s bed &7has been destroyed by <team><player>!",
          ""
        );
        public static String RESPAWN_IN_TITLE = "&cYou Died";
        public static String RESPAWN_IN_SUBTITLE = "&c<seconds>";
        public static List<String> START_MESSAGE = Arrays.asList(
          "",
          "<gamemode>",
          "&9 • &7Kit: &b<kit>",
          "&9 • &7Map: &b<arena>",
          "&9 • &7Opponent(s): &b<opponent>"
        );
        public static String START_COUNTDOWN = "&e<seconds>";
        public static String START_TITLE = "&e<seconds>";
        public static String WINNER_END_TITLE = "&aVICTORY!";
        public static String WINNER_END_SUBTITLE = "&a<winner> &fwin the match!";
        public static String LOSER_END_TITLE = "&cDEFEAT!";
        public static String LOSER_END_SUBTITLE = "&a<winner> &fwin the match!";
        public static String WINNER_END_TITLE_LOSER = "";
        public static String END_FORCE = "&cThe match is cancelled! Reason: <reason> (Your stats related to this match will not be saved)";
        public static String NAMETAG_OTHER = "&e";
        public static String NAMETAG_TEAMMATE = "&a";
        public static String NAMETAG_OPPONENT = "&c";
    }
}
