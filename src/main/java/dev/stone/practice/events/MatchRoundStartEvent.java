package dev.stone.practice.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import dev.stone.practice.match.Match;
import dev.stone.practice.util.BaseEvent;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 11/06/2025
 * Project: Practice
 */
@Getter
@Setter
@RequiredArgsConstructor
public class MatchRoundStartEvent extends BaseEvent {

    private final Match match;

}
