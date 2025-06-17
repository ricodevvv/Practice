package dev.stone.practice.util;

import dev.stone.practice.match.MatchState;
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.ProfileState;
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
        PlayerProfile profile = PlayerProfile.get(player);
        return profile.getState() == ProfileState.FIGHTING
                && profile.getMatch() != null
                && profile.getMatch().getTeamPlayer(player).isAlive()
                && !profile.getMatch().getTeamPlayer(player).isRespawning()
                && profile.getMatch().getState() == MatchState.FIGHTING;
    }
}
