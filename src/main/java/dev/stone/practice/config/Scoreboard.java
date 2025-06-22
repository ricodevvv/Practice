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
            "&7&m----------------------",
            "&b &fOnline: <theme>{online-players}",
            "&b &fIn Fight: <theme>{match-players}",
            "&b &fIn Queue: <theme>{queue-players}",
            "",
            "&fLevel: <theme><player_level>✮",
            "&fDivision: <player_division>",
            "<player_bar>",
            "<theme>devstone.xyz",
            "&7&m----------------------"
    );

    public static final List<String> IN_PARTY_SCOREBOARD = Arrays.asList(
            "&7&m----------------------",
            "&b&l┃ &fOnline: &b{online-players}",
            "&b&l┃ &fIn Queue: &b{queue-players}",
            "&b&l┃ &fIn Fights: &b{match-players}",
            "",
            "&b&l┃ &fTeam:",
            "&b&l┃ &f Leader: &b{party-leader}",
            "&b&l┃ &f Member(s): &b{party-members}&7/&b{party-max}",
            "{event-information}",
            "&7&m----------------------"
    );

    public static final List<String> IN_QUEUE_UNRANKED_SCOREBOARD = Arrays.asList(
            "&7&m----------------------",
            "&b&l┃ &fOnline: <theme>{online-players}",
            "&b&l┃ &fIn Queue: <theme>{queue-players}",
            "&b&l┃ &fIn Fights: <theme>{match-players}",
            "",
            "&b&l┃ &fQueuing: <theme>{queue-kit}",
            "&b&l┃ &fTime: <theme>{queue-time}",
            "&b&l┃ &fPing Range: &b{ping-range}",
            "&7&m----------------------"
    );

    public static final List<String> IN_QUEUE_RANKED_SCOREBOARD = Arrays.asList(
            "&7&m----------------------",
            "&b&l┃ &fOnline: &b{online-players}",
            "&b&l┃ &fIn Queue: &b{queue-players}",
            "&b&l┃ &fIn Fights: &b{match-players}",
            "",
            "&b&l┃ &fQueuing: &b{queue-kit}",
            "&b&l┃ &fTime: &b{queue-time}",
            "&b&l┃ &fPing Range: &b{ping-range}",
            "&b&l┃ &fRange: &7[&b{queue-ranked-min} &9-> &b{queue-ranked-max}&7]",
            "&7&m----------------------"
    );



    public static class MATCH_SCOREBOARD {
        public static List<String> IN_MATCH_SOLO_SCOREBOARD = Arrays.asList(
                "&7&m----------------------",
                "<theme> ┃ &fFighting: <theme>{match-solo-opponent}",
                "",
                "<theme> ┃ &fDuration: <match-duration>",
                "<theme> ┃ &fYour Ping: <theme>{match-solo-self-ping}ms",
                "<theme> ┃ &fTheir Ping: <theme>{match-solo-opponent-ping}ms",
                "&7&m----------------------"
        );
        public static final List<String> BED_SCOREBOARD = Arrays.asList(
                "&7&m----------------------",
                "&b&l┃ &fFighting: &b{match-solo-opponent}",
                "",
                "&b&l┃ &f{match-team1-logo}&7: &f{match-team1-bed-status}",
                "&b&l┃ &f{match-team2-logo}&7: &f{match-team2-bed-status}",
                "",
                "&b&l┃ &fBuild Height: &b{match-build-limit}{match-build-limit-difference}",
                "",
                "&b&l┃ &fYour Ping: &b{match-solo-self-ping}ms",
                "&b&l┃ &fTheir Ping: &b{match-solo-opponent-ping}ms",
                "&7&m----------------------"
        );

        public static final List<String> POINT_SCOREBOARD = Arrays.asList(
                "&7&m----------------------",
                "&b&l┃ &fFighting: &b{match-solo-opponent}",
                "",
                "&b&l┃ &f{match-team1-logo}&7: &f{match-team1-points}",
                "&b&l┃ &f{match-team2-logo}&7: &f{match-team2-points}",
                "",
                "&b&l┃ &fYour Ping: &b{match-solo-self-ping}ms",
                "&b&l┃ &fTheir Ping: &b{match-solo-opponent-ping}ms",
                "&7&m----------------------"
        );
        public static final List<String> BOXING_SCOREBOARD = Arrays.asList(
                "&7&m----------------------",
                "&b&l┃ &fFighting: &b{match-solo-opponent}",
                "",
                "&b&l┃ &bHits: {match-solo-boxing-difference-color}({match-solo-boxing-difference-symbol}{match-solo-boxing-difference})",
                "&b&l┃ &a You: &f{match-solo-boxing-self-hit}",
                "&b&l┃ &c Them: &f{match-solo-boxing-opponent-hit}",
                "&b&l┃ &f {match-solo-boxing-difference-text}",
                "",
                "&b&l┃ &fYour Ping: &b{match-solo-self-ping}ms",
                "&b&l┃ &fTheir Ping: &b{match-solo-opponent-ping}ms",
                "&7&m----------------------"
        );

        public static final List<String> ENDING_SCOREBOARD = Arrays.asList(
                "&7&m----------------------",
                "&b&l┃ &aMatch Over!",
                "&b&l┃ &fWinner: &b{match-solo-winner}",
                "&7&m----------------------"
        );

    }
}
