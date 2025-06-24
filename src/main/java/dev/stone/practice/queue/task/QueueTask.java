package dev.stone.practice.queue.task;

import dev.stone.practice.Phantom;
import dev.stone.practice.arena.Arena;
import dev.stone.practice.events.QueueMatchFoundEvent;
import dev.stone.practice.kit.Kit;
import dev.stone.practice.match.impl.SoloMatch;
import dev.stone.practice.match.team.Team;
import dev.stone.practice.match.team.TeamPlayer;
import dev.stone.practice.profile.Profile;
import dev.stone.practice.profile.ProfileState;
import dev.stone.practice.queue.Queue;
import dev.stone.practice.queue.QueueProfile;
import dev.stone.practice.queue.QueueType;
import dev.stone.practice.util.TaskTicker;
import dev.stone.practice.util.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 17/06/2025
 * Project: Practice
 */
public class QueueTask extends TaskTicker {

    public QueueTask() {
        super(0, 20, true);
    }

    @Override
    public void onRun() {
        Queue.getPlayers().entrySet().removeIf(entry -> entry.getValue().isFound());
        Queue.getPlayers().values().forEach(QueueProfile::tickRange);

        for (QueueProfile qProfile1 : Queue.getUnmatchedPlayers()) {
            for (QueueProfile qProfile2 : Queue.getUnmatchedPlayers()) {
                if (qProfile1 == qProfile2) {
                    continue;
                }
                //Double check if the QueueProfile already found a match
                if (qProfile1.isFound() || qProfile2.isFound()) {
                    continue;
                }
                if (qProfile1.getKit() != qProfile2.getKit()) {
                    continue;
                }
                if (qProfile1.getQueueType() != qProfile2.getQueueType()) {
                    continue;
                }
                //No need to check if qProfile2 is ranked again, because if qProfile1 is ranked, then qProfile2 will be ranked also
                if (qProfile1.getQueueType() == QueueType.RANKED) {
                    //If one of the QueueProfile is not in the selected ELO range, then stop the loop
                    if (!qProfile1.isInRange(qProfile2.getElo()) || !qProfile2.isInRange(qProfile1.getElo())) {
                        continue;
                    }
                }

                Player player1 = Bukkit.getPlayer(qProfile1.getPlayerUuid());
                Player player2 = Bukkit.getPlayer(qProfile2.getPlayerUuid());
                if (player1 == null || player2 == null) {
                    continue;
                }

                Profile profile1 = Profile.getByUuid(qProfile1.getPlayerUuid());
                Profile profile2 = Profile.getByUuid(qProfile2.getPlayerUuid());
                if (profile1.getState() != ProfileState.QUEUEING) {
                    continue;
                }
              /*  if (player2.spigot().getPing() > Integer.parseInt(profile1.getSettings().get(ProfileSettings.PING_RANGE).getValue()) || player1.spigot().getPing() > Integer.parseInt(profile2.getSettings().get(ProfileSettings.PING_RANGE).getValue())) {
                    continue;
                } */

                //Find arena
                Kit kit = qProfile1.getKit();
                Arena arena = Phantom.getInstance().getArenaHandler().getRandomArena(kit);
                if (arena == null) {
                    //Means no available arena
                    continue;
                }
                //This is to prevent player who's going to be in the same arena as before, to prevent see last match entities because of MatchResetTask in last match isn't triggered yet
                if (Phantom.getInstance().getMatchHandler().getMatches().values().stream().filter(match -> match.getArenaDetail() == arena).anyMatch(match -> match.getTeamPlayer(player1) != null || match.getTeamPlayer(player2) != null)) {
                    continue;
                }

                QueueMatchFoundEvent event = new QueueMatchFoundEvent(player1, player2, qProfile1, qProfile2);
                event.call();
                if (event.isCancelled()) {
                    continue;
                }

                qProfile1.setFound(true);
                qProfile2.setFound(true);

                Team team1 = new Team(new TeamPlayer(player1));
                Team team2 = new Team(new TeamPlayer(player2));
                SoloMatch match = new SoloMatch(arena, kit, team1, team2, qProfile1.getQueueType(), false);
                Tasks.run(match::start);
            }
        }
    }

    @Override
    public TickType getTickType() {
        return null;
    }

    @Override
    public int getStartTick() {
        return 0;
    }
}
