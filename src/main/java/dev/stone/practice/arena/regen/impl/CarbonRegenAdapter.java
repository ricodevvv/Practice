package dev.stone.practice.arena.regen.impl;

import com.google.common.collect.Maps;
import dev.stone.practice.arena.Arena;
import dev.stone.practice.arena.regen.IArenaRegen;
import dev.stone.practice.util.Callback;
import dev.stone.practice.util.Cuboid;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHash;
import xyz.refinedev.spigot.api.chunk.ChunkSnapshot;

import java.util.Map;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 23/06/2025
 * Project: Practice
 */
public class CarbonRegenAdapter implements IArenaRegen {

    private final transient Map<Long, ChunkSnapshot> chunkSnapshots = Maps.newHashMap();

    @Override
    public void takeSnapshot(Arena arena) {
        synchronized (chunkSnapshots) {
            forEachChunk(chunk -> chunkSnapshots.put(LongHash.toLong(chunk.getX(), chunk.getZ()), chunk.takeSnapshot()), arena.getBounds());
        }
    }


    @Override
    public void restore(Arena arena) {
        Cuboid bounds = arena.getBounds();
        World world = bounds.getWorld();
        synchronized (chunkSnapshots) {
            chunkSnapshots.forEach((key, value) -> world.getChunkAt(LongHash.msw(key), LongHash.lsw(key)).restoreSnapshot(value));
            chunkSnapshots.clear();
        }
    }
    private void forEachChunk(Callback<Chunk> callback, Cuboid bounds) {
        int lowerX = bounds.getLowerX() >> 4;
        int lowerZ = bounds.getLowerZ() >> 4;
        int upperX = bounds.getUpperX() >> 4;
        int upperZ = bounds.getUpperZ() >> 4;

        World world = bounds.getWorld();

        for (int x = lowerX; x <= upperX; x++) {
            for (int z = lowerZ; z <= upperZ; z++) {
                callback.callback(world.getChunkAt(x, z));
            }
        }
    }
}
