package dev.stone.practice.arena.regen.impl.legacy.chunk;


import dev.stone.practice.arena.Arena;
import dev.stone.practice.arena.regen.impl.legacy.chunk.data.NekoChunkData;
import dev.stone.practice.arena.regen.impl.legacy.chunk.reset.INekoChunkReset;
import dev.stone.practice.arena.regen.impl.legacy.chunk.reset.impl.VanillaNekoChunkReset;
import dev.stone.practice.arena.regen.impl.legacy.chunk.restoration.IChunkRestoration;
import dev.stone.practice.arena.regen.impl.legacy.chunk.restoration.impl.LegacyRegeneration;
import dev.stone.practice.util.Cuboid;
import lombok.Getter;
import lombok.Setter;



import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ChunkRestorationManager {

    @Setter @Getter private static INekoChunkReset iNekoChunkReset;
    @Setter @Getter private static IChunkRestoration iChunkRestoration;

    private final Map<Arena, NekoChunkData> chunks = new ConcurrentHashMap<>();
    private final Map<Cuboid, NekoChunkData> eventMapChunks = new ConcurrentHashMap<>();

    public ChunkRestorationManager() {
        if (iNekoChunkReset == null) { // Let the other plugins create an INekoReset before we load ours.
            iNekoChunkReset = new VanillaNekoChunkReset();
        }
        if (iChunkRestoration == null) { // Let the other plugins create an IChunkRestoration before we load ours.
            iChunkRestoration = new LegacyRegeneration(iNekoChunkReset, this);
        }
    }
}