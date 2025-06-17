package dev.stone.practice.events;

import dev.stone.practice.queue.QueueProfile;
import dev.stone.practice.util.BaseEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

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
@RequiredArgsConstructor
public class QueueMatchFoundEvent extends BaseEvent implements Cancellable {

    private final Player playerA;
    private final Player playerB;
    private final QueueProfile queueProfileA;
    private final QueueProfile queueProfileB;

    private boolean cancelled = false;
}
