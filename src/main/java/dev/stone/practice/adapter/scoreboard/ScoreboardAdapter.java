package dev.stone.practice.adapter.scoreboard;

import dev.stone.practice.Phantom;
import dev.stone.practice.config.Scoreboard;
import dev.stone.practice.match.Match;
import dev.stone.practice.party.Party;
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.ProfileState;
import dev.stone.practice.queue.Queue;
import dev.stone.practice.queue.QueueProfile;
import io.github.epicgo.sconey.element.SconeyElement;
import io.github.epicgo.sconey.element.SconeyElementAdapter;
import io.github.epicgo.sconey.element.SconeyElementMode;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 22/06/2025
 * Project: Practice
 */
public class ScoreboardAdapter implements SconeyElementAdapter {

    private final Phantom plugn = Phantom.getInstance();

    /**
     * This method returns the scoreboard element used by this instance
     * @param player the player containing the provided scoreboard
     * @return the scoreboard element used by this instance
     */
    @Override
    public SconeyElement getElement(final Player player) {
        SconeyElement element = new SconeyElement();
        String s = Phantom.getInstance().getScoreboardConfig().getString("TITLE");
        element.setTitle(Scoreboard.TITLE);
        element.setMode(SconeyElementMode.CUSTOM);

        PlayerProfile profile = PlayerProfile.get(player);

        if (profile == null) {
            return element;
        }

        Party party = Party.getByPlayer(player);
        QueueProfile qProfile = Queue.getPlayers().get(player.getUniqueId());
        Match match = profile.getMatch();

        if(profile.getState() == ProfileState.LOBBY && party == null) {
            List<String> translated = Scoreboard.LOBBY_SCOREBOARD.stream()
                    .map(key -> Phantom.getInstance().placeholders.translate(player, key))
                    .collect(Collectors.toList());
            element.addAll(translated);
        }  else if (profile.getState() == ProfileState.FIGHTING && match != null) {
            element.addAll(match.getMatchScoreboard(player));
        }
        return element;
    }
}
