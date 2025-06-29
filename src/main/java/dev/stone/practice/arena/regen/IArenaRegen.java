package dev.stone.practice.arena.regen;

import dev.stone.practice.arena.Arena;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 23/06/2025
 * Project: Practice
 */
public interface IArenaRegen {


    /**
     * it will be used to copy a section of the arena to then regenerate it
     */
    void takeSnapshot(Arena arena);

    /**
     * This method will be called at the end of a duel; it will regenerate the arena.
     */
    void restore(Arena arena);

}
