package dev.stone.practice.arena;

import com.google.common.base.Preconditions;


import com.sk89q.worldedit.Vector;

import java.io.File;
import java.util.*;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an arena schematic. See {@link net.frozenorb.potpvp.arena}
 * for a comparision of {@link Arena}s and {@link ArenaSchematic}s.
 */
@Getter
@Setter
public final class ArenaSchematic {

    /**
     * Name of this schematic (ex "Candyland")
     */
    @Getter
    private String name;

    /**
     * Display name of this Arena, will be used when communicating a GameMode
     * to players. Ex: "Map 15", "Ice KOTH", ...
     */
    private String displayName;

    /**
     * If matches can be scheduled on an instance of this arena.
     * Only impacts match scheduling, admin commands are (ignoring visual differences) nonchanged
     */
    private boolean enabled = false;


    private int gridIndex;

    private ArenaType type = ArenaType.STANDALONE;
    private String displayIcon; // base64 gzipped icon
    private boolean pearlsAllowed = true;
    private int deathHeight = 0;
    private int buildHeight = 0;
    private int portalRadius = 3;
    private int protectionRadius = 2;
    private int ffaSpawnRadius = 10;

    // Spawn locations and bounds for model
    private org.bukkit.Location spawn1;
    private org.bukkit.Location spawn2;
    private org.bukkit.Location spectatorSpawn;
    private dev.stone.practice.util.Cuboid bounds;

    // Optional special locations/bounds per team
    private List<org.bukkit.Location> redSpecial = new ArrayList<>();
    private List<org.bukkit.Location> blueSpecial = new ArrayList<>();
    private dev.stone.practice.util.Cuboid redSpecialBounds;
    private dev.stone.practice.util.Cuboid blueSpecialBounds;

    /**
     * @param String kit name
     */
    private final Set<String> kits = new HashSet<>();

    public ArenaSchematic() {
    } // for gson

    public ArenaSchematic(String name) {
        this.name = Preconditions.checkNotNull(name, "name");
        this.displayName = name;
    }

    public File getSchematicFile() {
        return new File(ArenaHandler.WORLD_EDIT_SCHEMATICS_FOLDER, name + ".schematic");
    }

    public Vector getModelArenaLocation() {
        int xModifier = ArenaGrid.GRID_SPACING_X * gridIndex;

        return new Vector(
                ArenaGrid.STARTING_POINT.getBlockX() - xModifier,
                ArenaGrid.STARTING_POINT.getBlockY(),
                ArenaGrid.STARTING_POINT.getBlockZ()
        );
    }

    public void pasteModelArena() throws Exception {
        Vector start = getModelArenaLocation();
        WorldEditUtils.paste(this, start);
    }

    public void removeModelArena() throws Exception {
        Vector start = getModelArenaLocation();
        Vector size = WorldEditUtils.readSchematicSize(this);

        WorldEditUtils.clear(
                start,
                start.add(size)
        );
    }


    @Override
    public boolean equals(Object o) {
        return o instanceof ArenaSchematic && ((ArenaSchematic) o).name.equals(name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}