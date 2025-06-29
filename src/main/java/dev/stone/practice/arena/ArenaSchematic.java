package dev.stone.practice.arena;

import com.google.common.base.Preconditions;


import com.sk89q.worldedit.Vector;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an arena schematic. See {@link net.frozenorb.potpvp.arena}
 * for a comparision of {@link Arena}s and {@link ArenaSchematic}s.
 */
@Getter
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
    @Getter
    @Setter
    private String displayName;

    /**
     * If matches can be scheduled on an instance of this arena.
     * Only impacts match scheduling, admin commands are (ignoring visual differences) nonchanged
     */
    @Setter
    private boolean enabled = false;


    @Getter
    @Setter
    private int gridIndex;

    /**
     * @param String kit name
     */
    @Getter
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

    public boolean isEnabled() {
        return enabled;
    }
}