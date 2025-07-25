package dev.stone.practice.arena.event;

import dev.stone.practice.arena.Arena;

import dev.stone.practice.match.Match;
import org.bukkit.event.HandlerList;

import lombok.Getter;

/**
 * Called when an {@link Arena} is allocated for use by a
 * {@link Match}
 */
public final class ArenaAllocatedEvent extends ArenaEvent {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    public ArenaAllocatedEvent(Arena arena) {
        super(arena);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}