package dev.stone.practice.arena.event;

import dev.stone.practice.arena.Arena;

import dev.stone.practice.match.Match;
import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * Called when an {@link Arena} is done being used by a
 * {@link Match}
 */
public final class ArenaReleasedEvent extends ArenaEvent {

    @Getter
    private static HandlerList handlerList = new HandlerList();

    public ArenaReleasedEvent(Arena arena) {
        super(arena);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}