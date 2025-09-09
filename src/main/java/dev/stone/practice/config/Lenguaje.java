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

    public static class PARTY_MESSAGES {
    public static String IN_A_PARTY = "&cYou are already in a party, cannot use this command";
    }

    public static class KIT_MENU {
        public static String MANAGEMENT_MENU_TITLE = "&bManagement menu";
        public static String KIT_MANAGE_BUTTON = "&bClick for manage <kit>";
        public static List<String> KIT_MANAGE_BUTTON_LORE = Arrays.asList(
                "",
                "&7View, edit and test this kit.",
                "&aClick to open"
        );
        public static int KIT_DETAILS_MENU_SIZE = 36;
    }

    public static class HELP {
        public static String HEADER = "&6<name> Help &7<> = Required [] = Optional";
        public static String SEPARATOR = "&7&m------------------------------------------------";

        public static List<String> ARENA = Arrays.asList(
                "&e/arena list &7- List all arenas",
                "&e/arena save &7- Save all arenas",
                "&e/arena manage &7- Manage all arenas",
                "&e/arena duplicates <arena> &7- View an arena's duplicates",
                "&e/arena teleport <arena> [duplicate-id] &7- Teleport to an arena",
                "&e/arena toggle <arena> &7- Toggle an arena from being selected",
                "&e/arena saveChanges &7- Save your map changes to the schematic",
                "&e/arena scale <arena> <amount> &7- Scale an arena's copies",
                "&e/arena create <name> <type> &7- Create an arena",
                "&e/arena delete <arena> [deleteBounds] &7- Delete an arena",
                "&e/arena updatePoints <arena> &7- Re-detect spawn-points",
                "&e/arena setName <arena> <name> &7- Set display-name",
                "&e/arena kits <arena> &7- View kits",
                "&e/arena addKit <arena> <kit> &7- Whitelist a kit",
                "&e/arena removeKit <arena> <kit> &7- Un-whitelist a kit"
        );

        public static List<String> KIT = Arrays.asList(
                "&e/kit list &7- List all kits",
                "&e/kit save &7- Save all kits",
                "&e/kit manage &7- Manage all kits",
                "&e/kit create <name> &7- Create a kit",
                "&e/kit delete <name> &7- Delete a kit",
                "&e/kit toggle <name> &7- Enable/Disable a kit",
                "&e/kit setName <kit> <name> &7- Set display-name",
                "&e/kit setIcon <kit> &7- Set icon from item in hand",
                "&e/kit setPriority <kit> <priority> &7- Set priority",
                "&e/kit setKnockback <kit> <name> &7- Set knockback profile",
                "&e/kit getInv <kit> &7- Get default inventory",
                "&e/kit setInv <kit> &7- Set default inventory"
        );
    }

    public static class MATCH_MESSAGES {
        public static String RESPAWN_IN_MESSAGE = "<theme>Respawn in <seconds>";
        public static String CANNOT_BREAK_OWN_BED = "&cYou cannot break your own bed!";
        public static String BED_BREAK_TITLE = "&cBed Destroyed！";
        public static String BED_BREAK_SUBTITLE = "&fYou will not respawn after die！";
        public static List<String> BED_BREAK_MESSAGE = Arrays.asList(
          "",
          "&f&lBed Destroyed > <bed_team>'s bed &7has been destroyed by <team><player>!",
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
        public static class POST_INVENTORY {
            public static String MENU_TITLE = "Inventory of <player>";
            public static String VIEW_INVENTORY = "&eView <player>'s inventory";
            public static String PLAYER_INFO_NAME = "&bPlayer Information";
            public static List<String> PLAYER_INFO = Arrays.asList(
                    "",
                    "&fHearts: &b<hearts>/<max_heart> &4❤",
                    "&fHunger: &b<hunger>/20",
                    ""
            );
            public static String WINNER = "&aWinner: &e";
            public static String LOSER = "&cLoser: &e";
            public static String HOVER = "&aClick to view &6<player>'s &ainventory";
            public static List<String> MESSAGE = Arrays.asList(
                    "&7&m------------------------------------------------",
                    "&bPost-Match Inventories &7(click to view)",
                    "<inventories>",
                    "&7&m------------------------------------------------"
            );
            public static String RATING_CHANGE = "&eELO changes: &a<winner> +<winner_elo> (<winner_new_elo>)  &c<loser> - <loser_elo> (<loser_new_elo>)";
        }
    }

    public static class QUEUE_MESSAGES {
    public static String MENU_TITLE = "<tyoe> Queue Menu";
    public static List<String> MENU_BUTTON_LORE = Arrays.asList(
            "",
            "&fIn-Queue: %practice_theme% <in-queue>",
            "&fIn-Game: %practice_theme% <in-game>",
            "",
            "&fClick to join %practice_theme%<kit>'s &fQueue!",
            ""
    );
    public static String ERROR_FOUND_QUEUE_PROFILE = "&cError: Found QueueProfile, cannot join the queue";
    public static String ERROR_NOT_ENOUGH_WINS = "&cYou need at least <require> wins to queue ranked. You now have <wins> wins.";
    public static String SUCCESS_QUIT = "&fYou have left %practice_theme% <kit> &f's game queue";
    public static String CANNOT_QUIT_QUEUE = "&cYou are not in a queue";
    public static String SUCCESS_JOIN = "You have entered the queue of <kit>";
    public static String WRONG_STATE = "&cCannot join a queue in the current state";
    }

    public static class MENU_UI {
        public static String NEXT_PAGE_NAME = "&aNext Page »";
        public static String PREVIOUS_PAGE_NAME = "&a« Previous Page";
        public static String DISABLED_NAME = "&7Unavailable";
        public static List<String> NEXT_PAGE_LORE = Arrays.asList(
                "",
                "&7Go to the next page.",
                "&aClick to continue"
        );
        public static List<String> PREVIOUS_PAGE_LORE = Arrays.asList(
                "",
                "&7Go back to the previous page.",
                "&aClick to go back"
        );
        public static List<String> DISABLED_LORE = Arrays.asList(
                "",
                "&7There are no more pages."
        );
    }
}
