package dev.stone.practice.lobby;


import dev.stone.practice.util.*;
import lombok.Getter;
import lombok.Setter;
import dev.stone.practice.Phantom;
import dev.stone.practice.config.Config;
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.ProfileState;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 10/06/2025
 * Project: Practice
 */
@Getter
public class LobbyManager {

    private final Phantom plugin;

    @Setter private Location spawnLocation = null;

    public LobbyManager(Phantom plugin) {
        this.plugin = plugin;
        try {
            this.spawnLocation = LocationSerialization.deserializeLocation(Config.LOBBY_LOCATION);
        } catch (Exception e) {
            Common.log("Unable to deserialize spawn-location from location file. " + e.getMessage());
        }
    }

    public void teleport(Player player) {
        if (spawnLocation == null) {
            Common.sendMessage(player, CC.RED + "Unable to teleport you to a certain location. Please check if spawn location and editor location is setup correctly.");
            Common.log(CC.RED + "Unable to teleport " + player.getName() + " to a certain location. Please check if spawn location and editor location is setup correctly.");
            return;
        }
        player.teleport(spawnLocation);
    }

    public void reset(Player player) {
        PlayerProfile profile = PlayerProfile.getByUuid(player.getUniqueId());
        if (profile == null) {
            return;
        }
        PlayerUtil.reset(player);
        profile.setMatch(null);
        profile.setState(ProfileState.LOBBY);
        profile.setupItems();
        profile.getCooldowns().forEach((name, cooldown) -> cooldown.cancelCountdown());
    }

    public void sendToSpawnAndReset(Player player) {
        Tasks.run(()-> {
            reset(player);
            teleport(player);
        });
    }

}
