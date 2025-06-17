package dev.stone.practice.match.listener.player;

import dev.stone.practice.match.Match;
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.ProfileState;
import org.bukkit.entity.Player;
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
public class PlayerQuitEvent implements Listener {


    @EventHandler
    public void OnPlayerLeave(org.bukkit.event.player.PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = PlayerProfile.get(player);

        //Profile will be null if the profile is not loaded in PlayerJoinEvent
        if (profile == null) {
            return;
        }
        if (profile.getState() == ProfileState.FIGHTING && profile.getMatch() != null) {
            Match match = profile.getMatch();
            match.die(player, true);
        }
    }
}
