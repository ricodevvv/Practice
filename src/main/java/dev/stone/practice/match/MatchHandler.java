package dev.stone.practice.match;

import dev.stone.practice.Phantom;
import dev.stone.practice.commands.PotPvPCommand;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 13/06/2025
 * Project: Practice
 */
public class MatchHandler {

    @Getter
    public final Map<UUID, Match> matches = new ConcurrentHashMap<>();

    public MatchHandler(String startMessage) {
        Phantom.getInstance().consoleLog(startMessage);
    }

}
