package dev.stone.practice.arena.regen.impl.legacy.chunk.restoration.impl;

import dev.stone.practice.arena.Arena;
import dev.stone.practice.arena.regen.impl.legacy.chunk.data.NekoChunk;
import dev.stone.practice.arena.regen.impl.legacy.chunk.data.NekoChunkData;
import dev.stone.practice.arena.regen.impl.legacy.chunk.reset.INekoChunkReset;
import dev.stone.practice.util.Cuboid;
import lombok.RequiredArgsConstructor;


import dev.stone.practice.arena.regen.impl.legacy.chunk.restoration.IChunkRestoration;
import dev.stone.practice.arena.regen.impl.legacy.chunk.ChunkRestorationManager;
import net.minecraft.server.v1_8_R3.ChunkSection;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;

/**
 * This code has been created by
 * gatogamer#6666 A.K.A. gatogamer.
 * If you want to use my code, please
 * ask first, and give me the credits.
 * Arigato! n.n
 */
@RequiredArgsConstructor
public class LegacyRegeneration implements IChunkRestoration {
    private final INekoChunkReset iNekoChunkReset;
    private final ChunkRestorationManager chunkRestorationManager;

    public void copy(Arena standaloneArena) {
        Cuboid cuboid = standaloneArena.getBounds();

        long startTime = System.currentTimeMillis();

        NekoChunkData nekoChunkData = new NekoChunkData();
        cuboid.getChunks().forEach(chunk -> {
            chunk.load();
            net.minecraft.server.v1_8_R3.Chunk nmsChunk = ((CraftChunk) chunk).getHandle();
            ChunkSection[] nmsSections = iNekoChunkReset.cloneSections(nmsChunk.getSections());
            nekoChunkData.chunks.put(new NekoChunk(chunk.getX(), chunk.getZ()), iNekoChunkReset.cloneSections(nmsSections));
        });
        chunkRestorationManager.getChunks().put(standaloneArena, nekoChunkData);

        System.out.println("Chunks copied! (" + (System.currentTimeMillis() - startTime) + "ms)");
    }

    public void reset(Arena standaloneArena) {
        long startTime = System.currentTimeMillis();

        Cuboid cuboid = standaloneArena.getBounds();
        resetCuboid(cuboid, chunkRestorationManager.getChunks().get(standaloneArena));

        System.out.println("Chunks have been reset! (took " + (System.currentTimeMillis() - startTime) + "ms)");
    }

    public void copy(Cuboid cuboid) {
        long startTime = System.currentTimeMillis();

        NekoChunkData nekoChunkData = new NekoChunkData();
        cuboid.getChunks().forEach(chunk -> {
            chunk.load();
            net.minecraft.server.v1_8_R3.Chunk nmsChunk = ((CraftChunk) chunk).getHandle();
            ChunkSection[] nmsSections = iNekoChunkReset.cloneSections(nmsChunk.getSections());
            nekoChunkData.chunks.put(new NekoChunk(chunk.getX(), chunk.getZ()), iNekoChunkReset.cloneSections(nmsSections));
        });
        chunkRestorationManager.getEventMapChunks().put(cuboid, nekoChunkData);
    }

    public void reset(Cuboid cuboid) {
        long startTime = System.currentTimeMillis();
        resetCuboid(cuboid, chunkRestorationManager.getEventMapChunks().get(cuboid));
    }

    private void resetCuboid(Cuboid cuboid, NekoChunkData nekoChunkData) {
        cuboid.getChunks().forEach(chunk -> {
            try {
                chunk.load();
                iNekoChunkReset.setSections(((CraftChunk) chunk).getHandle(), iNekoChunkReset.cloneSections(nekoChunkData.getNyaChunk(chunk.getX(), chunk.getZ())));
                chunk.getWorld().refreshChunk(chunk.getX(), chunk.getZ()); // let the mf server know that you've updated the chunk.
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}