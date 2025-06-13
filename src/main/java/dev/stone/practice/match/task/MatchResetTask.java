package dev.stone.practice.match.task;

import dev.stone.practice.Phantom;
import dev.stone.practice.config.Config;
import dev.stone.practice.events.MatchResetEvent;
import dev.stone.practice.match.Match;
import dev.stone.practice.match.MatchTaskTicker;
import dev.stone.practice.match.impl.SoloMatch;
import dev.stone.practice.profile.Profile;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Objects;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 11/06/2025
 * Project: Practice
 */
public class MatchResetTask extends MatchTaskTicker {
    private final Phantom plugin = Phantom.getInstance();
    private final Match match;

    public MatchResetTask(Match match) {
        super(1, Config.MATCH_SETTINGS.END_DURATION, false, match);
        this.match = match;
    }

    @Override
    public void onRun() {
        if (getTicks() <= 0) {
            cancel();

            MatchResetEvent event = new MatchResetEvent(match);
            event.call();

            match.clearEntities(true);
            match.getMatchPlayers().stream().filter(player -> Objects.nonNull(player) && player.isOnline())
                    .filter(player -> Profile.getByUuid(player.getUniqueId()).getMatch() == match) //This is to prevent player is in another match because of the requeue item
                    .forEach(player -> plugin.getLobbyManager().sendToSpawnAndReset(player));
            match.getSpectators().forEach(match::leaveSpectate);
            match.getTasks().forEach(BukkitRunnable::cancel);
        //   match.getArenaDetail().restoreChunk();
           // match.getArenaDetail().setUsing(false);
            Match.getMatches().remove(match.getUuid());
        }
    }

    @Override
    public void preRun() {
        //Cancel MatchClearBlockTask first, to save performance
       // match.getTasks().stream().filter(taskTicker -> taskTicker instanceof MatchClearBlockTask).forEach(BukkitRunnable::cancel);

        //Give 'Play Again' item like Minemen Club
        if (match instanceof SoloMatch) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    match.getMatchPlayers().stream()
                            .filter(Objects::nonNull) //If match players contains citizens NPC, because of it is already destroyed, it will be null
                           // .filter(player -> !EdenEvent.isInEvent(player)) //Do not give player 'Play Again' item if they are in an event
                            .forEach(player -> {
                               Profile profile = Profile.getByUuid(player.getUniqueId());
                                if (profile.getMatch() == match) {
                                    player.getInventory().clear();
                               //     EdenItems.giveItem(player, EdenItems.MATCH_REQUEUE);
                                }
                            });
                }
            }.runTaskLater(plugin, 20L);
        }
    }

    @Override
    public TickType getTickType() {
        return TickType.COUNT_DOWN;
    }

    @Override
    public int getStartTick() {
        return 1;
    }
}

