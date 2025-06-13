package dev.stone.practice.match.task;

import dev.stone.practice.events.MatchRoundEndEvent;
import dev.stone.practice.match.Match;
import dev.stone.practice.match.MatchState;
import dev.stone.practice.match.MatchTaskTicker;
import dev.stone.practice.match.team.Team;
import dev.stone.practice.match.team.TeamPlayer;
import org.bukkit.entity.Player;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 11/06/2025
 * Project: Practice
 */
public class MatchNewRoundTask extends MatchTaskTicker {

    private final Match match;
    private final TeamPlayer scoredPlayer;
    private final boolean newRound;

    public MatchNewRoundTask(Match match, TeamPlayer scoredPlayer, boolean newRound) {
        super(0, 20, false, match);
        this.match = match;
        this.scoredPlayer = scoredPlayer;
        this.newRound = newRound;
    }

    @Override
    public void onRun() {
        if (match.getState() == MatchState.ENDING) {
            cancel();
            return;
        }

        if (getTicks() == 0) {
           // match.broadcastMessage(Language.MATCH_NEW_ROUND_START_MESSAGE.toString());
            match.broadcastTitle("");
            match.setState(MatchState.FIGHTING);
          //  match.broadcastSound(EdenSound.MATCH_START);

         //   MatchRoundStartEvent event = new MatchRoundStartEvent(match);
           // event.call();

            cancel();
            return;
        }

      //  match.broadcastMessage(Language.MATCH_NEW_ROUND_START_COUNTDOWN.toString(getTicks()));
      //  match.broadcastTitle(Language.MATCH_NEW_ROUND_START_TITLE.toString(getTicks()));
     //   match.broadcastSound(EdenSound.NEW_ROUND_COUNTDOWN);
    }

    @Override
    public void preRun() {
        match.clearEntities(true); //Patch for #226 - Clear all entities when new round is happening, so things like arrow and pearl from last round will not be activated

        //To prevent any duplicate MatchNewRoundTask happens
        //This will occur when the match is sumo, and player walked into the water without taking any hits from opponent
        if (match.getTasks().stream().filter(taskTicker -> taskTicker != this).anyMatch(taskTicker -> taskTicker instanceof MatchNewRoundTask)) {
            cancel();
            return;
        }

        //Meaning the game is started and someone scored
        if (scoredPlayer != null && match.getState() != MatchState.STARTING && match.getKit().getGameRules().isPoint(match)) {
            Team team = match.getTeam(scoredPlayer);
            Player player = scoredPlayer.getPlayer();

            //Display scored message
          /*  match.broadcastMessage(Language.MATCH_NEW_ROUND_START_SCORE.toStringList(player,
                    team.getTeamColor().getColor(),
                    scoredPlayer.getUsername(),
                    Eden.DECIMAL.format((player.getHealth() + ((CraftPlayer) player).getHandle().getAbsorptionHearts())),
                    team.getPoints(),
                    match.getOpponentTeam(team).getTeamColor().getColor(),
                    match.getOpponentTeam(team).getPoints()
            ).stream().map(CenteredMessageSender::getCenteredMessage).collect(Collectors.toList())); */

            //Display scored title

                String scoredTeamColor = team.getTeamColor().getColor();
                String opponentTeamColor = match.getOpponentTeam(team).getTeamColor().getColor();

            /*    match.broadcastTitle(
                        Language.MATCH_NEW_ROUND_START_SCORED_TITLE.toString(scoredTeamColor, scoredPlayer.getUsername()),
                        Language.MATCH_NEW_ROUND_START_SCORED_SUBTITLE.toString(scoredTeamColor, team.getPoints(), opponentTeamColor, match.getOpponentTeam(team).getPoints()),
                        20,60,20
                );
            } */
        }

        if (newRound) {
            match.setState(MatchState.STARTING);

            //Teleport players into their team spawn
            match.getTeams().forEach(t -> t.teleport(t.getSpawnLocation()));
            match.getTeamPlayers().forEach(teamPlayer -> {
                if (teamPlayer.isRespawning()) {
                    //To prevent MatchRespawnTask gets cancelled because of a new round, we need to fully respawn the player instead of just giving the kit loadout. A fix for https://github.com/RealGoodestEnglish/Eden/issues/4
                    match.respawn(teamPlayer);
                } else {
                    teamPlayer.respawn(match);
                }
            });

            if (match.getKit().getGameRules().isResetArenaWhenGetPoint()) {
               // match.getArenaDetail().restoreChunk();

                //Cancel any runnable which affects the gameplay
             //   match.getTasks().stream().filter(taskTicker -> taskTicker instanceof MatchClearBlockTask).forEach(BukkitRunnable::cancel);
            }

            MatchRoundEndEvent event = new MatchRoundEndEvent(match);
            event.call();
        }
    }

    @Override
    public TickType getTickType() {
        return TickType.COUNT_DOWN;
    }

    @Override
    public int getStartTick() {
        if (!newRound) {
            return match.getKit().getGameRules().getMatchCountdownDuration();
        }
        return match.getKit().getGameRules().getNewRoundTime();
    }
}

