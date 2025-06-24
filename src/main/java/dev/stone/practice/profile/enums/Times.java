package dev.stone.practice.profile.enums;

import lombok.Getter;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 23/06/2025
 * Project: Practice
 */
@Getter
public enum Times {
    DAY(0, "Day"),
    NIGHT(18000, "Night"),
    SUNRISE(23000, "Sunrise"),
    SUNSET(12000, "Sunset");

    private final int time;
    private final String name;

    Times(int time, String name) {
        this.time = time;
        this.name = name;
    }
}
