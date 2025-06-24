package dev.stone.practice.arena.regen.impl.legacy.chunk.restoration;

import dev.stone.practice.arena.Arena;
import dev.stone.practice.util.Cuboid;

public interface IChunkRestoration {
    void copy(Arena standaloneArena);
    void reset(Arena standaloneArena);
    void copy(Cuboid cuboid);
    void reset(Cuboid cuboid);
}