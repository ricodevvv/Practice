package dev.stone.practice.match.listener.custom;

import dev.stone.practice.config.Lenguaje;
import dev.stone.practice.events.MatchStartEvent;
import dev.stone.practice.kit.Kit;
import dev.stone.practice.match.Match;
import dev.stone.practice.queue.QueueType;
import dev.stone.practice.util.CC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 13/06/2025
 * Project: Practice
 */
public class MatchStartListener implements Listener {

    @EventHandler
    public void OnMatchStart(MatchStartEvent event) {
        Match match = event.getMatch();
        Kit kit = match.getKit();

        match.getMatchPlayers().forEach(p -> {

         //   Profile.get(p).getCooldowns().forEach((s, cooldown) -> cooldown.cancelCountdown());

            String opponents;
            switch (match.getMatchType()) {
                case SOLO:
                    opponents = match.getOpponent(match.getTeamPlayer(p)).getUsername();
                    break;
                default:
                    opponents = CC.RED + "ERROR";
                    break;
            }

            if (match.getQueueType() == QueueType.UNRANKED) {
                Lenguaje.MATCH_MESSAGES.START_MESSAGE.forEach(s -> {
                    String m = s
                            .replace("<gamemode>", kit.getDisplayName())
                            .replace("<kit>", kit.getDisplayName())
                            .replace("<arena>", match.getArenaDetail().getParentArena().getDisplayName())
                            .replace("<opponent>", opponents);

                    p.sendMessage(CC.translate(m));
                });
            }
        });
    }
}

