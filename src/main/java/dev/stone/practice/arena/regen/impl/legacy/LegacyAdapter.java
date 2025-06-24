package dev.stone.practice.arena.regen.impl.legacy;

import dev.stone.practice.Phantom;
import dev.stone.practice.arena.Arena;
import dev.stone.practice.arena.regen.IArenaRegen;
import dev.stone.practice.arena.regen.impl.legacy.chunk.ChunkRestorationManager;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 23/06/2025
 * Project: Practice
 */
public class LegacyAdapter implements IArenaRegen {

    @Override
    public void takeSnapshot(Arena arena) {
        ChunkRestorationManager.getIChunkRestoration().copy(arena);
    }

    @Override
    public void restore(Arena arena) {
    ChunkRestorationManager.getIChunkRestoration().reset(arena);
    }
}
