package dev.stone.practice.adapter.board.adapter;

import dev.stone.practice.Placeholders;
import dev.stone.practice.adapter.board.fastboard.BoardAdapter;
import dev.stone.practice.config.Config;
import dev.stone.practice.config.Scoreboard;
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.ProfileState;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
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
        return Scoreboard.TITLE;
    }

    @Override
    public List<String> getScoreboard(Player player) {
        PlayerProfile profile = PlayerProfile.getByUuid(player.getUniqueId());
        switch (profile.getState()) {
            case FIGHTING:
                return getMatchScoreboard(player, profile);
            default:
                return getLobbyScoreboard(player, profile);
        }
    }

   public List<String> getLobbyScoreboard(Player player, PlayerProfile profile) {
      // return Scoreboard.LOBBY_SCOREBOARD.stream().map(s -> Placeholders.translate(player, s)).collect(Collectors.toList());
       return Scoreboard.LOBBY_SCOREBOARD;
   }

   public List<String> getMatchScoreboard(Player player, PlayerProfile profile) {
    //   return Scoreboard.MATCH_SCOREBOARD.IN_MATCH_SOLO_SCOREBOARD.stream().map(s -> Placeholders.translate(player, s)).collect(Collectors.toList());
       return Scoreboard.MATCH_SCOREBOARD.IN_MATCH_SOLO_SCOREBOARD;
   }
}
