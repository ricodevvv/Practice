package net.frozenorb.potpvp.adapter.board.task;

import net.frozenorb.potpvp.adapter.board.Board;
import net.frozenorb.potpvp.adapter.board.listener.ScoreboardListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 26/05/2025
 * Project: Amber
 */
public class BoardTask implements Runnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Board board = ScoreboardListener.boards.get(player.getUniqueId());
            // not sure if this would happen but just in case.
            if (board != null) {
                board.update();
            }
        }
    }
}
