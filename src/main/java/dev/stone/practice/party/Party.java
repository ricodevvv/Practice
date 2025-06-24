package dev.stone.practice.party;

import dev.stone.practice.profile.Profile;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 17/06/2025
 * Project: Practice
 */
public class Party {

    @Getter private static final Map<UUID, Party> parties = new HashMap<>();


    public static Party getByPlayer(Player player) {
        return Profile.get(player).getParty();
    }
}
