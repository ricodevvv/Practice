package dev.stone.practice.adapter.board.adapter;

import dev.stone.practice.adapter.board.fastboard.BoardAdapter;
import dev.stone.practice.config.Config;
import dev.stone.practice.config.ScoreboardConfig;
import dev.stone.practice.profile.Profile;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 10/06/2025
 * Project: Practice
 */
public class ScoreboardAdapter implements BoardAdapter {
    @Override
    public String getTitle(Player player) {
        return ScoreboardConfig.SCOREBOARD_LOBBY_TITLE;
    }

    @Override
    public List<String> getScoreboard(Player player) {
       if(Config.DEBUG) {
           return getDebugScore(player);

       } else return ScoreboardConfig.SCOREBOARD_LOBBY_LINES;
    }

    public List<String> getDebugScore(Player player) {
    Profile profile = Profile.getByUuid(player.getUniqueId());
    if(profile == null) return null;
        return ScoreboardConfig.SCOREBOARD_LOBBY_LINES_DEBUG.stream()
                    .map(s -> s
                            .replace("<status>", String.valueOf(profile.getState()))
                            .replace("<coins>", String.valueOf(profile.getCoins()))
                            .replace("<global_elo>", String.valueOf(profile.getGlobalElo()))
                    )
                    .collect(Collectors.toList());
    }
}
