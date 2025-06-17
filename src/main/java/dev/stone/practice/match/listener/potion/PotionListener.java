package dev.stone.practice.match.listener.potion;

import dev.stone.practice.events.MatchStateChangeEvent;
import dev.stone.practice.kit.Kit;
import dev.stone.practice.match.Match;
import dev.stone.practice.match.MatchState;
import dev.stone.practice.match.team.TeamPlayer;
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.ProfileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 16/06/2025
 * Project: Practice
 */
public class PotionListener implements Listener {

    @EventHandler
    public void onPotionSplashEvent(PotionSplashEvent event) {
        if (event.getPotion().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            PlayerProfile profile = PlayerProfile.get(player);
            //PracticePlayer may be null because player left the server but the potion still in there
            if (profile == null) {
                return;
            }
            if (profile.getState() == ProfileState.FIGHTING && profile.getMatch() != null) {
                Match match = profile.getMatch();
                if (match.getState() != MatchState.FIGHTING) {
                    return;
                }
                if (!match.getTeamPlayer(player).isAlive() || match.getTeamPlayer(player).isRespawning()) {
                    return;
                }
                if (event.getIntensity(player) <= 0.5D) {
                    match.getTeamPlayer(player).addPotionsMissed();
                }
            }
        }
    }

    @EventHandler
    public void onMatchStateChange(MatchStateChangeEvent event) {
        Match match = event.getMatch();
        Kit kit = match.getKit();

        if (match.getState() == MatchState.FIGHTING) {
            for (TeamPlayer teamPlayer : match.getTeamPlayers()) {
                Player player = teamPlayer.getPlayer();
                if (player != null && teamPlayer.getKitLoadout() == null) {
                    player.setItemOnCursor(null); //Fix for #308 point 1 - Prevent book duplicate
                    kit.getKitLoadout().apply(match, player);
                }
            }
        }
    }
}
