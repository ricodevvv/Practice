package dev.stone.practice.adapter.board.fastboard;

import org.bukkit.entity.Player;
import java.util.List;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 26/05/2025
 * Project: Amber
 */
public interface BoardAdapter {

    String getTitle(Player player);

    List<String> getScoreboard(Player player);

}
