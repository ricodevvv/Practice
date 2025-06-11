package net.frozenorb.potpvp.adapter.board.listener;

import net.frozenorb.potpvp.adapter.board.Board;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 26/05/2025
 * Project: Amber
 */

@Getter
public class ScoreboardListener implements Listener {

    public static final Map<UUID, Board> boards = new ConcurrentHashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boards.put(player.getUniqueId(), new Board(player));
    }

    @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        boards.remove(player.getUniqueId());
    }
}
