package dev.stone.practice.match.entity;

import lombok.Getter;
import dev.stone.practice.match.Match;
import org.bukkit.entity.Entity;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 11/06/2025
 * Project: Practice
 */
@Getter
public class MatchEntity {

    private final long timestamp;
    private final Match match;
    private final Entity entity;

    public MatchEntity(Match match, Entity entity) {
        this.timestamp = System.currentTimeMillis();
        this.match = match;
        this.entity = entity;
    }

}
