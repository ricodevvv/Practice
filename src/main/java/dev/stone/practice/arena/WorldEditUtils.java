package dev.stone.practice.arena;

import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.schematic.SchematicFormat;
import dev.stone.practice.Phantom;
import dev.stone.practice.util.Cuboid;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@UtilityClass
public final class WorldEditUtils {

    private static EditSession editSession;
    private static com.sk89q.worldedit.world.World worldEditWorld;
    private final HashMap<String, Schematic> cache = new HashMap<>();

    public static void primeWorldEditApi() {
        if (editSession != null) {
            return;
        }

        EditSessionFactory esFactory = WorldEdit.getInstance().getEditSessionFactory();
        ArenaHandler arenaHandler = Phantom.getInstance().getArenaHandler();

        worldEditWorld = new BukkitWorld(arenaHandler.getArenaWorld());
        editSession = esFactory.getEditSession(worldEditWorld, Integer.MAX_VALUE);
    }


    public Schematic paste(ArenaSchematic s, Vector pastedAt){
        ClipboardFormat cf;
        if (!cache.containsKey(s.getName())){
             cf = ClipboardFormat.findByFile(s.getSchematicFile());
            try {
                if (cf != null){
                    cache.put(s.getName(), cf.load(s.getSchematicFile()));
                }
            } catch (IOException ignored) {
            }
        }
        if (cache.containsKey(s.getName())){
            Schematic sh = cache.get(s.getName());
            EditSession editSession = sh.paste(worldEditWorld, pastedAt, false, true, null);
            editSession.flushQueue();
            return sh;
        }
        return null;
    }

    public static void clear(Cuboid bounds) {
        clear(
                new Vector(bounds.getLowerX(), bounds.getLowerY(), bounds.getLowerZ()),
                new Vector(bounds.getUpperX(), bounds.getUpperY(), bounds.getUpperZ())
        );
    }

    public static void clear(Vector lower, Vector upper) {
        primeWorldEditApi();

        BaseBlock air = new BaseBlock(Material.AIR.getId());
        Region region = new CuboidRegion(worldEditWorld, lower, upper);

        editSession.setBlocks(region, air);
    }

    public static Vector readSchematicSize(ArenaSchematic schematic) throws Exception {
        File schematicFile = schematic.getSchematicFile();
        CuboidClipboard clipboard = SchematicFormat.MCEDIT.load(schematicFile);

        return clipboard.getSize();
    }

    public static Location vectorToLocation(Vector vector) {
        ArenaHandler arenaHandler = Phantom.getInstance().getArenaHandler();

        return new Location(
                arenaHandler.getArenaWorld(),
                vector.getBlockX(),
                vector.getBlockY(),
                vector.getBlockZ()
        );
    }

}