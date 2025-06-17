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
public class Scoreboard extends StaticConfig {
    public Scoreboard(String filename, ConfigHandler handler) {
        super(new File(Phantom.getInstance().getDataFolder(), filename + ".yml"), handler);
    }

    public static String TITLE = "Development";

    public static  List<String> LOBBY_SCOREBOARD = Arrays.asList(
            "&7&m----------------------",
            " %practice_theme%&l┃ &fOnline: %practice_theme%{online-players}",
            " %practice_theme%&l┃ &fIn Fight: %practice_theme%{match-players}",
            " %practice_theme%&l┃ &fIn Queue: %practice_theme%{queue-players}",
            "",
            "&7&m----------------------"
    );

    public static class MATCH_SCOREBOARD {
        public static List<String> IN_MATCH_SOLO_SCOREBOARD = Arrays.asList(
                "&7&m----------------------",
                " %practice_theme%&l┃ &fFighting: %practice_theme%{match-solo-opponent}",
                "",
                "%practice_theme% ┃ &fDuration: <match-duration>",
                " %practice_theme%&l┃ &fYour Ping: %practice_theme%{match-solo-self-ping}ms",
                " %practice_theme%&l┃ &fTheir Ping: %practice_theme%{match-solo-opponent-ping}ms",
                "&7&m----------------------"
        );
    }
}
