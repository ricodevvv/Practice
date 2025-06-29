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

  /*  public static List<String> TITLE = Arrays.asList(
            "§6§lP§f§lRACTICE",
            "§6§lPR§f§lACTICE",
            "§6§lPRA§f§lCTICE",
            "§6§lPRAC§f§lTICE",
            "§6§lPRACT§f§lICE",
            "§6§lPRACTI§f§lCE",
            "§6§lPRACTIC§f§lE",
            "§6§lPRACTICE"
    ); */
    public static String TITLE = "Development";
    public static int UPDATE_TICKS = 10;

    public static  List<String> LOBBY_SCOREBOARD = Arrays.asList(
            "",
            "&c &fOnline: <theme><online-players>",
            "&c &fIn Fight: <theme><match-players>",
            "&c &fIn Queue: <theme><queue-players>",
            "",
            "&fLevel: <theme><player_level>✮",
            "&fDivision: <player_division>",
            "<player_bar>",
            "<theme>devstone.xyz"
    );

    public static final List<String> IN_PARTY_SCOREBOARD = Arrays.asList(
            "",
            "&c&l┃ &fOnline: <theme><online-players>",
            "&c&l┃ &fIn Queue: <theme><queue-players>",
            "&c&l┃ &fIn Fights: <theme><match-players>",
            "",
            "&c&l┃ &fTeam:",
            "&c&l┃ &f Leader: &b{party-leader}",
            "&c&l┃ &f Member(s): &7{party-members}&7/&f{party-max}",
            "",
            "<theme>devstone.xyz"
    );

    public static final List<String> IN_QUEUE_UNRANKED_SCOREBOARD = Arrays.asList(
            "",
            "&c&l┃ &fOnline: <theme><online-players>",
            "&c&l┃ &fIn Queue: <theme><queue-players>",
            "&c&l┃ &fIn Fights: <theme><match-players>",
            "",
            "&c&l┃ &fQueuing: <theme>{queue-kit}",
            "&c&l┃ &fTime: <theme>{queue-time}",
            "&c&l┃ &fPing Range: &7{ping-range}",
            "",
            "<theme>devstone.xyz"
    );

    public static final List<String> IN_QUEUE_RANKED_SCOREBOARD = Arrays.asList(
            "",
            "&c&l┃ &fOnline: &7<online-players>",
            "&c&l┃ &fIn Queue: &7{queue-players}",
            "&c&l┃ &fIn Fights: &7{match-players}",
            "",
            "&c&l┃ &fQueuing: &7{queue-kit}",
            "&c&l┃ &fTime: &7{queue-time}",
            "&c&l┃ &fPing Range: &7{ping-range}",
            "&c&l┃ &fRange: &7[&b{queue-ranked-min} &9-> &b{queue-ranked-max}&7]",
            "",
            "<theme>devstone.xyz"
    );



    public static class MATCH_SCOREBOARD {
        public static List<String> IN_MATCH_SOLO_SCOREBOARD = Arrays.asList(
                "",
                "<theme> ┃ &fFighting: <theme>{match-solo-opponent}",
                "",
                "<theme> ┃ &fDuration: <match-duration>",
                "<theme> ┃ &fYour Ping: <theme>{match-solo-self-ping}ms",
                "<theme> ┃ &fTheir Ping: <theme>{match-solo-opponent-ping}ms",
                "",
                "<theme>devstone.xyz"
        );
        public static final List<String> BED_SCOREBOARD = Arrays.asList(
                "",
                "&c&l┃ &fFighting: &7{match-solo-opponent}",
                "",
                "&c&l┃ &f{match-team1-logo}&7: &7{match-team1-bed-status}",
                "&c&l┃ &f{match-team2-logo}&7: &7{match-team2-bed-status}",
                "",
                "&c&l┃ &fBuild Height: &7{match-build-limit}{match-build-limit-difference}",
                "",
                "&c&l┃ &fYour Ping: &7{match-solo-self-ping}ms",
                "&c&l┃ &fTheir Ping: &7{match-solo-opponent-ping}ms",
                "",
                "<theme>devstone.xyz"
        );

        public static final List<String> POINT_SCOREBOARD = Arrays.asList(
                "",
                "&c&l┃ &fFighting: &7{match-solo-opponent}",
                "",
                "&c&l┃ &f{match-team1-logo}&7: &7{match-team1-points}",
                "&c&l┃ &f{match-team2-logo}&7: &7{match-team2-points}",
                "",
                "&c&l┃ &fYour Ping: &7{match-solo-self-ping}ms",
                "&c&l┃ &fTheir Ping: &7{match-solo-opponent-ping}ms",
                "",
                "<theme>devstone.xyz"
        );
        public static final List<String> BOXING_SCOREBOARD = Arrays.asList(
                "",
                "&b&l┃ &fFighting: &7{match-solo-opponent}",
                "",
                "&c&l┃ &bHits: {match-solo-boxing-difference-color}({match-solo-boxing-difference-symbol}{match-solo-boxing-difference})",
                "&c&l┃ &a You: &7{match-solo-boxing-self-hit}",
                "&c&l┃ &c Them: &7{match-solo-boxing-opponent-hit}",
                "&c&l┃ &f {match-solo-boxing-difference-text}",
                "",
                "&c&l┃ &fYour Ping: &7{match-solo-self-ping}ms",
                "&c&l┃ &fTheir Ping: &7{match-solo-opponent-ping}ms",
                "",
                "<theme>devstone.xyz"
        );

        public static final List<String> ENDING_SCOREBOARD = Arrays.asList(
                "",
                "&b&l┃ &aMatch Over!",
                "&b&l┃ &fWinner: &b{match-solo-winner}",
                "",
                "<theme>devstone.xyz"
        );

    }
}
