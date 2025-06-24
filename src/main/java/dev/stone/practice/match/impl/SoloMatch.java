package dev.stone.practice.match.impl;

import dev.stone.practice.Phantom;
import dev.stone.practice.arena.Arena;
import dev.stone.practice.config.Config;
import dev.stone.practice.config.Lenguaje;
import dev.stone.practice.config.Scoreboard;
import dev.stone.practice.kit.Kit;
import dev.stone.practice.match.Match;
import dev.stone.practice.match.MatchState;
import dev.stone.practice.match.MatchType;
import dev.stone.practice.match.team.Team;
import dev.stone.practice.match.team.TeamPlayer;
import dev.stone.practice.profile.Profile;
import dev.stone.practice.profile.data.ProfileKitData;
import dev.stone.practice.queue.QueueType;
import dev.stone.practice.util.CC;
import dev.stone.practice.util.Clickable;
import dev.stone.practice.util.Common;
import dev.stone.practice.util.Util;
import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 11/06/2025
 * Project: Practice
 */
public class SoloMatch extends Match {

    private final Team teamA;
    private final Team teamB;

    @Getter private final TeamPlayer playerA;
    @Getter
   private final TeamPlayer playerB;

    public SoloMatch(Arena arenaDetail, Kit kit, Team teamA, Team teamB, QueueType queueType, boolean duel) {
        super(arenaDetail, kit, Arrays.asList(teamA, teamB));

        this.teamA = teamA;
        this.teamB = teamB;

        this.playerA = teamA.getLeader();
        this.playerB = teamB.getLeader();

        setQueueType(queueType);
        setDuel(duel);
    }

    @Override
    public void setupTeamSpawnLocation() {
        teamA.setSpawnLocation(getArenaDetail().getTeam1Spawn());
        teamB.setSpawnLocation(getArenaDetail().getTeam2Spawn());
    }

    @Override
    public void displayMatchEndMessages() {
        TeamPlayer winner = getWinningPlayers().get(0);
        TeamPlayer loser = getOpponent(winner);
        Clickable clickable = new Clickable(
                Lenguaje.MATCH_MESSAGES.POST_INVENTORY.WINNER + winner.getUsername(),
                Lenguaje.MATCH_MESSAGES.POST_INVENTORY.HOVER.replace("<player>", winner.getUsername()),
                "/viewinv " + winner.getUuid()
        );

        clickable.add(
                CC.GRAY + " - " +
                        Lenguaje.MATCH_MESSAGES.POST_INVENTORY.LOSER + loser.getUsername(),
                Lenguaje.MATCH_MESSAGES.POST_INVENTORY.HOVER.replace("<player>", winner.getUsername()),
                "/viewinv " + loser.getUuid()
        );

        Lenguaje.MATCH_MESSAGES.POST_INVENTORY.MESSAGE.forEach(s -> {
            if (s.contains("<inventories>")) {
                getPlayersAndSpectators().forEach(clickable::sendToPlayer);
            } else {
                getPlayersAndSpectators().forEach(p -> Common.sendMessage(p, s));
            }
        });
    }

    @Override
    public void displayMatchEndTitle() {
        TeamPlayer tWinner = getWinningPlayers().get(0);
        TeamPlayer tLoser = getOpponent(getWinningPlayers().get(0));
        tWinner.broadcastTitle(Lenguaje.MATCH_MESSAGES.WINNER_END_TITLE.replace("<winner>", tWinner.getUsername()), Lenguaje.MATCH_MESSAGES.WINNER_END_SUBTITLE.replace("<winner>", tWinner.getUsername()));
        tLoser.broadcastTitle(Lenguaje.MATCH_MESSAGES.LOSER_END_TITLE, Lenguaje.MATCH_MESSAGES.LOSER_END_SUBTITLE.replace("<winner>", tWinner.getUsername()));
    }

    @Override
    public void calculateMatchStats() {
        TeamPlayer tWinner = getWinningPlayers().get(0);
        TeamPlayer tLoser = getOpponent(getWinningPlayers().get(0));

        //Set Post-Match Inventories swtichTo
        getPostMatchInventories().get(tWinner.getUuid()).setSwitchTo(tLoser.getUsername(), tLoser.getUuid());
        getPostMatchInventories().get(tLoser.getUuid()).setSwitchTo(tWinner.getUsername(), tWinner.getUuid());

        //Because this is a duel match, we don't increase win/lose in player statistics and don't calculate the elo afterwards
        if (isDuel()) {
            return;
        }

        Profile pWinner = Profile.getByUuid(tWinner.getUuid());
        Profile pLoser = Profile.getByUuid(tLoser.getUuid());
        Validate.notNull(pWinner);
        Validate.notNull(pLoser);

        ProfileKitData kWinner = pWinner.getKitData().get(getKit());
        ProfileKitData kLoser = pLoser.getKitData().get(getKit());

        if (getQueueType() == QueueType.RANKED) {
            int oldWinnerElo = kWinner.getElo();
            int oldLoserElo = kLoser.getElo();

            int newWinnerElo = Util.getNewRating(oldWinnerElo, oldLoserElo, 1);
            int newLoserElo = Util.getNewRating(oldLoserElo, oldWinnerElo, 0);

            kWinner.setElo(newWinnerElo);
            kLoser.setElo(newLoserElo);

            int winnerEloChange = newWinnerElo - oldWinnerElo;
            int loserEloChange = oldLoserElo - newLoserElo;

            broadcastMessage(Lenguaje.MATCH_MESSAGES.POST_INVENTORY.RATING_CHANGE
                    .replace("<winner>", pWinner.getName())
                    .replace("<winner_elo>", String.valueOf(winnerEloChange))
                    .replace("<winner_new_elo>", String.valueOf( newWinnerElo))
                    .replace("<loser>", pLoser.getName())
                    .replace("<loser_elo>", String.valueOf(loserEloChange))
                    .replace("<loser_new_elo>", String.valueOf( newLoserElo))

            );
        }

        kWinner.incrementWon(getQueueType() == QueueType.RANKED);
        kLoser.incrementLost(getQueueType() == QueueType.RANKED);

        kWinner.calculateWinstreak(true);
        kLoser.calculateWinstreak(false);

        List<String> winCommands = Config.WIN_COMMANDS;
        List<String> loseCommands = Config.LOSE_COMMANDS;
        if (!winCommands.isEmpty()) {
            for (String cmd : winCommands) {
                String c = cmd.replace("<player>", tWinner.getUsername());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c);
            }
        }
        if (!loseCommands.isEmpty()) {
            for (String cmd1 : loseCommands) {
                String d = cmd1.replace("<loser-player>", tLoser.getUsername());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), d);
            }
        }
    }

    @Override
    public MatchType getMatchType() {
        return MatchType.SOLO;
    }
    @Override
    public List<TeamPlayer> getWinningPlayers() {
        if (playerA.isDisconnected() || !playerA.isAlive()) {
            return Collections.singletonList(playerB);
        } else {
            return Collections.singletonList(playerA);
        }
    }

    @Override
    public Team getWinningTeam() {
        if (teamA.isEliminated()) {
            return teamB;
        } else if (teamB.isEliminated()) {
            return teamA;
        } else {
            return null;
        }
    }

    @Override
    public List<String> getMatchScoreboard(Player player) {
        List<String> elements = new ArrayList<>();

        if (getState() == MatchState.ENDING) {
            List<String> translated = Scoreboard.MATCH_SCOREBOARD.ENDING_SCOREBOARD.stream()
                    .map(key -> Phantom.getInstance().placeholders.translate(player, key))
                    .collect(Collectors.toList());
            elements.addAll(translated);
        } else {
            if (getKit().getGameRules().isBoxing()) {
                List<String> translated = Scoreboard.MATCH_SCOREBOARD.BOXING_SCOREBOARD.stream()
                        .map(key -> Phantom.getInstance().placeholders.translate(player, key))
                        .collect(Collectors.toList());
                elements.addAll(translated);
            } else if (getKit().getGameRules().isBed()) {
                List<String> translated = Scoreboard.MATCH_SCOREBOARD.BED_SCOREBOARD.stream()
                        .map(key -> Phantom.getInstance().placeholders.translate(player, key))
                        .collect(Collectors.toList());
                elements.addAll(translated);
            } else if (getKit().getGameRules().isPoint(this)) {
                List<String> translated = Scoreboard.MATCH_SCOREBOARD.POINT_SCOREBOARD.stream()
                        .map(key -> Phantom.getInstance().placeholders.translate(player, key))
                        .collect(Collectors.toList());
                elements.addAll(translated);
            } else {
                List<String> translated = Scoreboard.MATCH_SCOREBOARD.IN_MATCH_SOLO_SCOREBOARD.stream()
                        .map(key -> Phantom.getInstance().placeholders.translate(player, key))
                        .collect(Collectors.toList());
                elements.addAll(translated);
            }
        }
        return elements;
    }

    @Override
    public List<String> getSpectateScoreboard(Player player) {
        return Collections.emptyList();
    }

    @Override
    public Team getOpponentTeam(Team team) {
        if (teamA.equals(team)) {
            return teamB;
        } else if (teamB.equals(team)) {
            return teamA;
        } else {
            return null;
        }
    }

    @Override
    public TeamPlayer getOpponent(TeamPlayer teamPlayer) {
        if (teamPlayer == null) {
            return null;
        }

        if (playerA == teamPlayer) {
            return playerB;
        } else if (playerB == teamPlayer) {
            return playerA;
        }
        return null;
    }
}
