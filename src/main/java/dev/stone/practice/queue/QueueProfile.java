package dev.stone.practice.queue;

import dev.stone.practice.kit.Kit;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 17/06/2025
 * Project: Practice
 */
@Getter
@Setter
public class QueueProfile {

    private UUID playerUuid;
    private Kit kit;
    private QueueType queueType;
    private boolean found = false;
    private int elo;
    private int range = 10;
    private long start = System.currentTimeMillis();

    public QueueProfile(UUID playerUuid, Kit kit, int elo, QueueType queueType) {
        this.playerUuid = playerUuid;
        this.kit = kit;
        this.elo = elo;
        this.queueType = queueType;
    }

    public void tickRange() {
        range += 10;
    }

    public boolean isInRange(int elo) {
        return elo >= (this.elo - this.range) && elo <= (this.elo + this.range);
    }

    public int getMinRange() {
        int min = this.elo - this.range;

        return Math.max(min, 0);
    }

    public int getMaxRange() {
        int max = this.elo + this.range;

        return Math.min(max, 2500);
    }

    public long getPassed() {
        return System.currentTimeMillis() - this.start;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof QueueProfile && ((QueueProfile) o).getPlayerUuid().equals(this.playerUuid);
    }

}
