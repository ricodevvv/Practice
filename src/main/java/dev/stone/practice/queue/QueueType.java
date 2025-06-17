package dev.stone.practice.queue;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 11/06/2025
 * Project: Practice
 */
@Getter
@AllArgsConstructor
public enum QueueType {

    UNRANKED("Unranked"),
    RANKED("Ranked"),
    ;

    private final String readable;

}
