package dev.stone.practice;

import dev.stone.practice.match.Match;
import dev.stone.practice.match.MatchState;
import dev.stone.practice.match.impl.SoloMatch;
import dev.stone.practice.match.team.Team;
import dev.stone.practice.match.team.TeamPlayer;
import dev.stone.practice.party.Party;
import dev.stone.practice.profile.Profile;
import dev.stone.practice.profile.ProfileState;
import dev.stone.practice.profile.division.ProfileDivision;
import dev.stone.practice.queue.Queue;
import dev.stone.practice.queue.QueueProfile;
import dev.stone.practice.util.CC;
import dev.stone.practice.util.ProgressBar;
import dev.stone.practice.util.TimeUtil;
import lombok.RequiredArgsConstructor;
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

    private final Phantom plugin;
    public static final String SKIP_LINE = "<skip-line>";
    public static final String NEW_LINE = "<new-line>";

    public String translate(Player player, String str) {
        if (player != null) {
            Profile profile = Profile.get(player);

            if (profile == null) {
                return str;
            }

            Party party = Party.getByPlayer(player);
            QueueProfile qProfile = Queue.getPlayers().get(player.getUniqueId());
            Match match = profile.getMatch();

            if(str.contains("<theme>")) {
                str = str.replace("<theme>", CC.translate("&" + profile.getOptions().theme().getColor().getChar()));
            }
            if(str.contains("<player_division>")) {
                str = str.replace("<player_division>",  CC.translate(profile.getDivision().getDisplayName()));
            }
            if(str.contains("<player_level>")) {
               str = str.replace("<player_level>", CC.translate(profile.getDivision().getXpLevel()));
            }
            if(str.contains("<player_bar>")) {
                ProfileDivision profileDivision = profile.getDivision();
                ProfileDivision expDivision = Phantom.getInstance().getDivisionsManager().getNextDivisionByXP(profile.getExperience());
                ProfileDivision oldDivision = Phantom.getInstance().getDivisionsManager().getDivisionForBarByXP(profile.getExperience());
                ProfileDivision eloDivision = Phantom.getInstance().getDivisionsManager().getNextDivisionByELO(profile.getGlobalElo());

                if(Phantom.getInstance().getDivisionsManager().isXPBased()) {
                   str = str.replace("<player_bar>",  ProgressBar.getBar(profile.getExperience() - oldDivision.getExperience(), expDivision.getExperience() - oldDivision.getExperience()));

                } else {
                    str = str.replace("<player_bar>", ProgressBar.getBar(profile.getGlobalElo(), eloDivision.getMaxElo()));
                }
            }
            if (party != null) {
                str = str
                           .replace("{party-leader}", party.getLeader().getUsername())
                          .replace("{party-members}", party.getAllPartyMembers().size() + "")
                        .replace("{party-max}", 0 + "");
            }

            if (profile.getState() == ProfileState.QUEUEING && qProfile != null) {
                str = str
                        .replace("{queue-kit}", qProfile.getKit().getDisplayName())
                        .replace("{queue-time}", TimeUtil.millisToTimer(qProfile.getPassed()))
                        .replace("{queue-ranked-min}", qProfile.getMinRange() + "")
                        .replace("{queue-ranked-max}", qProfile.getMaxRange() + "");
                //  .replace("{ping-range}", profile.getSettings().get(ProfileSettings.PING_RANGE).toString());
            } else if (profile.getState() == ProfileState.FIGHTING && match != null) {
                str = str
                        .replace("{match-kit}", match.getKit().getDisplayName())
                        .replace("{match-duration}", TimeUtil.millisToTimer(match.getElapsedDuration()))
                        .replace("{match-build-limit}", match.getArenaDetail().getMaxbuild() + "")

                ;


                for (int i = 0; i < match.getTeams().size(); i++) {
                    Team team = match.getTeams().get(i);
                    str = str.replace("{match-team" + (i + 1) + "-logo}", team.getTeamColor().getTeamLogo())
                            .replace("{match-team" + (i + 1) + "-bed-status}", team.isBedDestroyed() ? CC.RED + "✘" : CC.GREEN + "✔")
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
                                .replace("{match-solo-loser}", match.getState() == MatchState.ENDING ? match.getTeams().stream().filter(team -> team != match.getWinningTeam()).map(team -> team.getLeader().getUsername()).findFirst().orElse("") : "")
                                //   .replace("{match-solo-boxing-difference-text}", selfComboing || opponentComboing ? Language.SCOREBOARD_BOXING_COUNTER_TEXT_SOLO.toString(selfComboing ? CC.GREEN : CC.RED, selfComboing ? self.getCombo() : opponent.getCombo()) : Language.SCOREBOARD_BOXING_COUNTER_NO_COMBO.toString())
                                .replace("{match-solo-boxing-difference}", Math.abs(soloDifference) + "")
                                .replace("{match-solo-boxing-difference-number}", soloDifference + "")
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
                                //  .replace("{match-team-boxing-difference-text}", xComboing || yComboing ? Language.SCOREBOARD_BOXING_COUNTER_TEXT_TEAM.toString(xComboing ? CC.GREEN : CC.RED, xComboing ? team.getCombo() : opponentTeam.getCombo()) : Language.SCOREBOARD_BOXING_COUNTER_NO_COMBO.toString())
                                .replace("{match-team-boxing-difference}", Math.abs(teamsDifference) + "")
                                .replace("{match-team-boxing-difference-number}", teamsDifference + "")
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
                }
                if (profile.getState() == ProfileState.SPECTATING && match != null) {
                    str = str
                            .replace("{spectate-kit}", match.getKit().getDisplayName())
                            .replace("{spectate-duration}", TimeUtil.millisToTimer(match.getElapsedDuration()))
                            .replace("{spectate-build-limit}", match.getArenaDetail().getMaxbuild() + "")
                    //  .replace("{spectate-build-limit-difference}", Util.renderBuildLimit(player.getLocation().getBlockY(), match.getArenaDetail().getArena().getBuildMax()))
                    ;

                    for (int i = 0; i < match.getTeams().size(); i++) {
                        Team team = match.getTeams().get(i);
                        str = str.replace("{spectate-team" + (i + 1) + "-logo}", team.getTeamColor().getTeamLogo())
                                .replace("{spectate-team" + (i + 1) + "-bed-status}", team.isBedDestroyed() ? CC.RED + "✘" : CC.GREEN + "✔");
                        // .replace("{spectate-team" + (i+1) + "-points}", Util.renderPointsAsBar(team, match.getKit().getGameRules().getMaximumPoints()));
                    }

                    switch (match.getMatchType()) {
                        case SOLO:
                            TeamPlayer playerA = ((SoloMatch) match).getPlayerA();
                            TeamPlayer playerB = ((SoloMatch) match).getPlayerB();

                            str = str
                                    .replace("{spectate-solo-player1}", playerA.getUsername())
                                    .replace("{spectate-solo-player2}", playerB.getUsername())
                                    .replace("{spectate-solo-winner}", match.getState() == MatchState.ENDING ? match.getWinningPlayers().get(0).getUsername() : "")
                                    .replace("{spectate-solo-loser}", match.getState() == MatchState.ENDING ? match.getTeams().stream().filter(team -> team != match.getWinningTeam()).map(team -> team.getLeader().getUsername()).findFirst().orElse("") : "")
                                    .replace("{spectate-solo-boxing-player1-hit}", playerA.getHits() + "")
                                    .replace("{spectate-solo-boxing-player2-hit}", playerB.getHits() + "")
                                    .replace("{spectate-solo-boxing-player1-combo}", playerA.getCombo() + "")
                                    .replace("{spectate-solo-boxing-player2-combo}", playerB.getCombo() + "")
                                    .replace("{spectate-solo-player1-ping}", playerA.getPing() + "")
                                    .replace("{spectate-solo-player2-ping}", playerB.getPing() + "")
                            ;
                            break;
                        default:
                            break;
                    }
                }
            }

            if (str.contains(SKIP_LINE)) {
                return null;
            } else {
                return str
                        .replace("<online-players>", plugin.getCache().getPlayersSize() + "")
                        .replace("<queue-players>", plugin.getCache().getQueuePlayersSize() + "")
                        .replace("<match-players>", plugin.getCache().getMatchPlayersSize() + "")
                        ;
            }
        }
        return null;
    }
}