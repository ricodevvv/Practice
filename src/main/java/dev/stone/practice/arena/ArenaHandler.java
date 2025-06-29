package dev.stone.practice.arena;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import lombok.SneakyThrows;
import dev.stone.practice.arena.listener.ArenaItemResetListener;
import dev.stone.practice.kit.Kit;
import dev.stone.practice.util.Common;
import org.bukkit.Bukkit;
import org.bukkit.World;
import com.google.gson.reflect.TypeToken;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;

import lombok.Getter;
import dev.stone.practice.Phantom;
import dev.stone.practice.arena.event.ArenaAllocatedEvent;
import dev.stone.practice.arena.event.ArenaReleasedEvent;

/**
 * Facilitates easy access to {@link ArenaSchematic}s and to {@link Arena}s
 * based on their schematic+copy pair
 */
public final class ArenaHandler {

    public static final File WORLD_EDIT_SCHEMATICS_FOLDER = new File(JavaPlugin.getPlugin(WorldEditPlugin.class).getDataFolder(), "schematics");
    private static final String ARENA_INSTANCES_FILE_NAME = "arenaInstances.json";
    private static final String SCHEMATICS_FILE_NAME = "schematics.json";

    // schematic -> (instance id -> Arena instance)
   @Getter private final Map<String, Map<Integer, Arena>> arenaInstances = new HashMap<>();
    // schematic name -> ArenaSchematic instance
    private final Map<String, ArenaSchematic> schematics = new TreeMap<>();
    @Getter
    private final ArenaGrid grid = new ArenaGrid();

    public ArenaHandler() {
        Bukkit.getPluginManager().registerEvents(new ArenaItemResetListener(), Phantom.getInstance());

        File folder = Phantom.getInstance().getDataFolder();

        File arenaInstancesFile = new File(folder, ARENA_INSTANCES_FILE_NAME);
        File schematicsFile = new File(folder, SCHEMATICS_FILE_NAME);

        try {
            // parsed as a List<Arena> and then inserted into Map<String, Map<Integer. Arena>>
            if (arenaInstancesFile.exists()) {
                try (Reader arenaInstancesReader = Files.newReader(arenaInstancesFile, Charsets.UTF_8)) {
                    Type arenaListType = new TypeToken<List<Arena>>() {
                    }.getType();
                    List<Arena> arenaList = Phantom.getGson().fromJson(arenaInstancesReader, arenaListType);

                    for (Arena arena : arenaList) {
                        // create inner Map for schematic if not present
                        arenaInstances.computeIfAbsent(arena.getSchematic(), i -> new HashMap<>());

                        // register this copy with the inner Map
                        arenaInstances.get(arena.getSchematic()).put(arena.getCopy(), arena);
                    }
                }
            }

            // parsed as a List<ArenaSchematic> and then inserted into Map<String, ArenaSchematic>
            if (schematicsFile.exists()) {
                try (Reader schematicsFileReader = Files.newReader(schematicsFile, Charsets.UTF_8)) {
                    Type schematicListType = new TypeToken<List<ArenaSchematic>>() {
                    }.getType();
                    List<ArenaSchematic> schematicList = Phantom.getGson().fromJson(schematicsFileReader, schematicListType);

                    for (ArenaSchematic schematic : schematicList) {
                        this.schematics.put(schematic.getName(), schematic);
                    }
                }
            }
        } catch (IOException ex) {
            // just rethrow, can't recover from arenas failing to load
            throw new RuntimeException(ex);
        }
    }

    @SneakyThrows
    public void saveSchematics() {
        Files.write(
                Phantom.getGson().toJson(schematics.values()),
                new File(Phantom.getInstance().getDataFolder(), SCHEMATICS_FILE_NAME),
                Charsets.UTF_8
        );

    }

    public void saveArenas() throws IOException {
        List<Arena> allArenas = new ArrayList<>();

        arenaInstances.forEach((schematic, copies) -> {
            allArenas.addAll(copies.values());
        });

        Files.write(
                Phantom.getGson().toJson(allArenas),
                new File(Phantom.getInstance().getDataFolder(), ARENA_INSTANCES_FILE_NAME),
                Charsets.UTF_8
        );
    }

    public World getArenaWorld() {
        return Bukkit.getWorlds().get(0);
    }

    public void registerSchematic(ArenaSchematic schematic) {
        // assign a grid index upon creation. currently this will not reuse
        // lower grid indexes from deleted arenas.
        int lastGridIndex = 0;

        for (ArenaSchematic otherSchematic : schematics.values()) {
            lastGridIndex = Math.max(lastGridIndex, otherSchematic.getGridIndex());
        }

        schematic.setGridIndex(lastGridIndex + 1);
        schematics.put(schematic.getName(), schematic);
    }

    public void unregisterSchematic(ArenaSchematic schematic) {
        schematics.remove(schematic.getName());
    }

    void registerArena(Arena arena) {
        Map<Integer, Arena> copies = arenaInstances.get(arena.getSchematic());

        if (copies == null) {
            copies = new HashMap<>();
            arenaInstances.put(arena.getSchematic(), copies);
        }

        copies.put(arena.getCopy(), arena);
    }

    void unregisterArena(Arena arena) {
        Map<Integer, Arena> copies = arenaInstances.get(arena.getSchematic());

        if (copies != null) {
            copies.remove(arena.getCopy());
        }
    }

    /**
     * Finds an arena by its schematic and copy pair
     *
     * @param schematic ArenaSchematic to use when looking up arena
     * @param copy      copy of arena to look up
     * @return Arena object existing for specified schematic and copy pair, if one exists
     */
    public Arena getArena(ArenaSchematic schematic, int copy) {
        Map<Integer, Arena> arenaCopies = arenaInstances.get(schematic.getName());

        if (arenaCopies != null) {
            return arenaCopies.get(copy);
        } else {
            return null;
        }
    }

    /**
     * Finds all arena instances for the given schematic
     *
     * @param schematic schematic to look up arenas for
     * @return immutable set of all arenas for given schematic
     */
    public Set<Arena> getArenas(ArenaSchematic schematic) {
        Map<Integer, Arena> arenaCopies = arenaInstances.get(schematic.getName());

        if (arenaCopies != null) {
            return ImmutableSet.copyOf(arenaCopies.values());
        } else {
            return ImmutableSet.of();
        }
    }

    /**
     * Counts the number of arena instances present for the given schematic
     *
     * @param schematic schematic to count arenas for
     * @return number of copies present of the given schematic
     */
    public int countArenas(ArenaSchematic schematic) {
        Map<Integer, Arena> arenaCopies = arenaInstances.get(schematic.getName());
        return arenaCopies != null ? arenaCopies.size() : 0;
    }

    /**
     * Finds all schematic instances registered
     *
     * @return immutable set of all schematics registered
     */
    public Set<ArenaSchematic> getSchematics() {
        return ImmutableSet.copyOf(schematics.values());
    }

    /**
     * Finds an ArenaSchematic by its id
     *
     * @param schematicName schematic id to search with
     * @return ArenaSchematic present for the given id, if one exists
     */
    public ArenaSchematic getSchematic(String schematicName) {
        return schematics.get(schematicName);
    }

    /**
     * Attempts to allocate an arena for use, using the Predicate provided to determine
     * which arenas are eligible for use. Handles calling {@link ArenaAllocatedEvent}
     * automatically.
     *
     * @param acceptableSchematicPredicate Predicate to use to determine if an {@link ArenaSchematic}
     *                                     is eligible for use.
     * @return The arena which has been allocated for use, or null, if one was not found.
     */
    public Optional<Arena> allocateUnusedArena(Predicate<ArenaSchematic> acceptableSchematicPredicate) {
        List<Arena> acceptableArenas = new ArrayList<>();

        for (ArenaSchematic schematic : schematics.values()) {
            if (!acceptableSchematicPredicate.test(schematic)) {
                continue;
            }

            if (!arenaInstances.containsKey(schematic.getName())) {
                continue;
            }

            for (Arena arena : arenaInstances.get(schematic.getName()).values()) {
                if (!arena.isUsing()) {
                    acceptableArenas.add(arena);
                }
            }
        }

        if (acceptableArenas.isEmpty()) {
            return Optional.empty();
        }

        Arena selected = acceptableArenas.get(ThreadLocalRandom.current().nextInt(acceptableArenas.size()));

        selected.setUsing(true);
        Bukkit.getPluginManager().callEvent(new ArenaAllocatedEvent(selected));

        return Optional.of(selected);
    }

    /**
     * Releases (unallocates) an arena so that it may be used again. Handles calling
     * {@link ArenaReleasedEvent} automatically.
     *
     * @param arena the arena to release
     */
    public void releaseArena(Arena arena) {
        Preconditions.checkArgument(arena.isUsing(), "Cannot release arena not in use.");

        arena.setUsing(false);
        Bukkit.getPluginManager().callEvent(new ArenaReleasedEvent(arena));
    }

    /**
     * get a random arena that accepts the kit
     * @param kit
     * @return Arena
     */
    public Arena getRandomArena(Kit kit) {
        List<Arena> availableArenas = arenaInstances.values().stream()
                .flatMap(subMap -> subMap.values().stream())
                .filter(arena -> {
                    ArenaSchematic schematic = getSchematic(arena.getSchematic());
                    if (schematic == null) return false;
                    boolean kitCompatible = schematic.getKits().contains(kit.getName());
                    if (!kitCompatible) return false;
                    return !arena.isUsing();
                })
                .collect(Collectors.toList());
        if (availableArenas.isEmpty()) {
            Common.debug("No arena found");
            return null;
        }
        return availableArenas.get(ThreadLocalRandom.current().nextInt(availableArenas.size()));
    }


}