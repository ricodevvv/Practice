package dev.stone.practice.util;

import dev.stone.practice.match.MatchState;
import dev.stone.practice.profile.Profile;
import dev.stone.practice.profile.ProfileState;
import dev.stone.practice.queue.QueueType;
import org.bukkit.entity.Player;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 10/06/2025
 * Project: Practice
 */
public class Checker {

    public static boolean canDamage(Player player) {
        Profile profile = Profile.get(player);
        return profile.getState() == ProfileState.FIGHTING
                && profile.getMatch() != null
                && profile.getMatch().getTeamPlayer(player).isAlive()
                && !profile.getMatch().getTeamPlayer(player).isRespawning()
                && profile.getMatch().getState() == MatchState.FIGHTING;
    }

    public static boolean isQueueType(String index) {
        try {
            QueueType.valueOf(index.toUpperCase());
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
