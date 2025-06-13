package dev.stone.practice.match.impl;

import dev.stone.practice.arena.Arena;
import dev.stone.practice.kit.Kit;
import dev.stone.practice.match.Match;
import dev.stone.practice.match.MatchType;
import dev.stone.practice.match.team.Team;
import dev.stone.practice.match.team.TeamPlayer;
import dev.stone.practice.queue.QueueType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    private final TeamPlayer playerA;
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

    }

    @Override
    public void displayMatchEndTitle() {

    }

    @Override
    public void calculateMatchStats() {

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
