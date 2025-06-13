package dev.stone.practice.match;

import lombok.AllArgsConstructor;
import lombok.Getter;
import dev.stone.practice.config.Lenguaje;

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
public enum MatchType {
    SOLO(Lenguaje.MATCH_MESSAGES.START_MESSAGE.toString()),
    FFA(Lenguaje.MATCH_MESSAGES.START_MESSAGE.toString()),
    SPLIT(Lenguaje.MATCH_MESSAGES.START_MESSAGE.toString());

    private final String readable;
}

