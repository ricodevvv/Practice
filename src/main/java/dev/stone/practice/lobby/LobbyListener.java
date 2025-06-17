package dev.stone.practice.lobby;

import dev.stone.practice.Phantom;
import dev.stone.practice.config.Config;
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.ProfileState;
import dev.stone.practice.util.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 10/06/2025
 * Project: Practice
 */
public class LobbyListener implements Listener {

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = PlayerProfile.getByUuid(player.getUniqueId());
        if (profile != null && (profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.QUEUEING) && player.getLocation().getY() < 0) {
            Phantom.getInstance().getLobbyManager().teleport(player);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        Config.WELCOME_MESSAGE.forEach(s -> player.sendMessage(CC.translate(s)));
        Phantom.getInstance().getLobbyManager().teleport(player);
    }
}
