package dev.stone.practice.queue;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 17/06/2025
 * Project: Practice
 */
public class QueueListener implements Listener {

    @EventHandler
    public void PlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
    }
}
