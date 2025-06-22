package dev.stone.practice;

import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.ProfileState;
import dev.stone.practice.util.TaskTicker;
import lombok.Getter;
import org.bukkit.Bukkit;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 19/06/2025
 * Project: Practice
 */
@Getter
public class Cache {

    private int playersSize;
    private int queuePlayersSize;
    private int matchPlayersSize;

    public Cache() {
        new TaskTicker(0, 5, true) {
            @Override
            public void onRun() {
                playersSize = Bukkit.getOnlinePlayers().size();
                queuePlayersSize = (int) PlayerProfile.getProfiles().values().stream().filter(p -> p.getState() == ProfileState.QUEUEING).count();
                matchPlayersSize = (int) PlayerProfile.getProfiles().values().stream().filter(p -> p.getState() == ProfileState.FIGHTING || p.getState() == ProfileState.SPECTATING).count();
            }

            @Override
            public TickType getTickType() {
                return TickType.NONE;
            }

            @Override
            public int getStartTick() {
                return 0;
            }
        };
    }

}
