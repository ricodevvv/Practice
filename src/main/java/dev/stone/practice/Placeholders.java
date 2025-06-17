package dev.stone.practice;

import dev.stone.practice.match.Match;
import dev.stone.practice.match.MatchState;
import dev.stone.practice.match.team.Team;
import dev.stone.practice.match.team.TeamPlayer;
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.ProfileState;
import dev.stone.practice.util.CC;
import dev.stone.practice.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 16/06/2025
 * Project: Practice
 */


@RequiredArgsConstructor
public class Placeholders {

    public static final String SKIP_LINE = "<skip-line>";
    public static final String NEW_LINE = "<new-line>";

    public static String translate(Player player, String str) {
    if(player != null && str != null) {
        PlayerProfile profile = PlayerProfile.get(player);
        Match match = profile.getMatch();

        if (profile.getState() == ProfileState.FIGHTING && match != null) {
            str = str
                    .replace("{match-kit}", match.getKit().getDisplayName())
                    .replace("<match-duration>", TimeUtil.millisToTimer(match.getElapsedDuration()))
            ;


            for (int i = 0; i < match.getTeams().size(); i++) {
                Team team = match.getTeams().get(i);
                str = str.replace("{match-team" + (i+1) + "-logo}", team.getTeamColor().getTeamLogo())
                        .replace("{match-team" + (i+1) + "-bed-status}", team.isBedDestroyed() ? CC.RED + "✘" : CC.GREEN + "✔")
                      //  .replace("{match-team" + (i+1) + "-points}", Util.renderPointsAsBar(team, match.getKit().getGameRules().getMaximumPoints()))
                ;
            }

            switch (match.getMatchType()) {
                case SOLO:
                    TeamPlayer self = match.getTeamPlayer(player);
                    TeamPlayer opponent = match.getOpponent(self);
                    int solo_x = self.getHits();
                    int solo_y = opponent.getHits();
                    int soloDifference = solo_x - solo_y;

                    boolean selfComboing = self.getCombo() > 1;
                    boolean opponentComboing = opponent.getCombo() > 1;

                    str = str
                            .replace("{match-solo-opponent}", opponent.getUsername())
                            .replace("{match-solo-winner}", match.getState() == MatchState.ENDING ? match.getWinningPlayers().get(0).getUsername() : "")
                            .replace("{match-solo-loser}", match.getState() == MatchState.ENDING ? match.getTeams().stream().filter(team -> team != match.getWinningTeam()).map(team -> team.getLeader().getUsername()).findFirst().orElse(""): "")
                          //  .replace("{match-solo-boxing-difference-text}", selfComboing || opponentComboing ? Language.SCOREBOARD_BOXING_COUNTER_TEXT_SOLO.toString(selfComboing ? CC.GREEN : CC.RED, selfComboing ? self.getCombo() : opponent.getCombo()) : Language.SCOREBOARD_BOXING_COUNTER_NO_COMBO.toString())
                            .replace("{match-solo-boxing-difference}", Math.abs(soloDifference) + "")
                            .replace("{match-solo-boxing-difference-number}",  soloDifference + "")
                            .replace("{match-solo-boxing-difference-symbol}", soloDifference == 0 ? "" : soloDifference > 0 ? "+" : "-")
                            .replace("{match-solo-boxing-difference-color}", solo_x > solo_y ? CC.GREEN : solo_x == solo_y ? CC.YELLOW : CC.RED)
                            .replace("{match-solo-boxing-self-hit}", solo_x + "")
                            .replace("{match-solo-boxing-opponent-hit}", solo_y + "")
                            .replace("{match-solo-boxing-combo}", self.getCombo() + "")
                            .replace("{match-solo-self-ping}", self.getPing() + "")
                            .replace("{match-solo-opponent-ping}", opponent.getPing() + "")
                    ;
                    break;
                case SPLIT:
                    Team team = match.getTeam(player);
                    Team opponentTeam = match.getOpponentTeam(team);
                    int teams_x = team.getHits();
                    int teams_y = opponentTeam.getHits();
                    int teamsDifference = teams_x - teams_y;

                    boolean xComboing = team.getCombo() > 1;
                    boolean yComboing = opponentTeam.getCombo() > 1;

                    str = str
                            .replace("{match-team-self-alive}", team.getAliveCount() + "")
                            .replace("{match-team-self-size}", team.getTeamPlayers().size() + "")
                            .replace("{match-team-opponent-alive}", opponentTeam.getAliveCount() + "")
                            .replace("{match-team-opponent-size}", opponentTeam.getTeamPlayers().size() + "")
                            .replace("{match-team-winner}", match.getState() != MatchState.ENDING ? "" : match.getWinningTeam() == null ? "" : match.getWinningTeam().getLeader().getUsername())
                            .replace("{match-team-loser}", match.getState() != MatchState.ENDING ? "" : match.getWinningTeam() == null ? "" : match.getTeams().stream().filter(t -> match.getWinningTeam() != t).map(t -> t.getLeader().getUsername()).findFirst().orElse(""))
                           // .replace("{match-team-boxing-difference-text}", xComboing || yComboing ? Language.SCOREBOARD_BOXING_COUNTER_TEXT_TEAM.toString(xComboing ? CC.GREEN : CC.RED, xComboing ? team.getCombo() : opponentTeam.getCombo()) : Language.SCOREBOARD_BOXING_COUNTER_NO_COMBO.toString())
                            .replace("{match-team-boxing-difference}", Math.abs(teamsDifference) + "")
                            .replace("{match-team-boxing-difference-number}",  teamsDifference + "")
                            .replace("{match-team-boxing-difference-symbol}", teamsDifference == 0 ? "" : teamsDifference > 0 ? "+" : "-")
                            .replace("{match-team-boxing-difference-color}", teams_x > teams_y ? CC.GREEN : teams_x == teams_y ? CC.YELLOW : CC.RED)
                            .replace("{match-team-boxing-self-hit}", teams_x + "")
                            .replace("{match-team-boxing-opponent-hit}", teams_y + "")
                            .replace("{match-team-boxing-combo}", team.getCombo() + "")
                    ;
                    break;
                case FFA:
                    List<Team> ffaTeams = match.getTeams();
                    long aliveCount = ffaTeams.stream().filter(t -> !t.isEliminated()).count();

                    str = str
                            .replace("{match-ffa-alive}", aliveCount + "")
                            .replace("{match-ffa-player-size}", ffaTeams.size() + "")
                            .replace("{match-ffa-winner}", match.getState() != MatchState.ENDING ? "" : match.getWinningTeam() == null ? "" : match.getWinningTeam().getLeader().getUsername())
                            .replace("{match-ffa-loser}", match.getState() != MatchState.ENDING ? "" : match.getWinningTeam() == null ? "" : match.getTeams().stream().filter(t -> match.getWinningTeam() != t).map(t -> t.getLeader().getUsername()).collect(Collectors.joining(",")))
                    ;
                    break;
                default:
                    break;
            }
        }
    }
     return str;
    }
}