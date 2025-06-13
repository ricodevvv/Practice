package dev.stone.practice.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import dev.stone.practice.kit.KitLoadout;
import dev.stone.practice.match.Match;
import dev.stone.practice.util.BaseEvent;
import org.bukkit.entity.Player;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 11/06/2025
 * Project: Practice
 */
@Getter
@RequiredArgsConstructor
public class KitLoadoutReceivedEvent extends BaseEvent {

    private final Player player;
    private final Match match;
    private final KitLoadout kitLoadout;

}
