package dev.stone.practice.arena;

import java.util.*;
import java.util.function.Predicate;

import dev.stone.practice.util.Callback;
import dev.stone.practice.util.Cuboid;
import org.apache.commons.lang.Validate;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHash;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import xyz.refinedev.spigot.api.chunk.ChunkSnapshot;
import dev.stone.practice.Phantom;
import dev.stone.practice.util.AngleUtils;

/**
 * Represents a pasted instance of an {@link ArenaSchematic}.
 * See {@link dev.stone.practice.arena} for a comparision of
 * {@link Arena}s and {@link ArenaSchematic}s.
 */
@Getter
public final class Arena {

    /**
     * The name of the {@link ArenaSchematic} this Arena is
     * copied from.
     *
     * @see dev.stone.practice.arena
     */
    @Getter
    private String schematic;

    /**
     * What number copy this arena is.
     *
     * @see dev.stone.practice.arena
     */
    @Getter
    private int copy;

    /**
     * Bounding box for this arena
     */
    @Getter
    private Cuboid bounds;

    /**
     * Team 1 spawn location for this arena
     * For purposes of arena definition we ignore
     * non-two-teamed matches.
     */
    @Getter
    private Location team1Spawn;

    /**
     * Team 2 spawn location for this arena
     * For purposes of arena definition we ignore
     * non-two-teamed matches.
     */
    @Getter
    private Location team2Spawn;

    /**
     * Spectator spawn location for this arena
     */
    private Location spectatorSpawn;

    @Getter
    private List<Location> eventSpawns;

    private int deadzone;
    @Setter public transient boolean Using;
    private int maxbuild;
    private final int portalProtecion = 5;
    private Location cageBlueMin = null;
    private Location cageBlueMax = null;
    private Location cageRedMin = null;
    private Location cageRedMax = null;

    /**
     * If this arena is currently being used
     *
     * @see ArenaHandler#allocateUnusedArena(Predicate)
     * @see ArenaHandler#releaseArena(Arena)
     */
    // AccessLevel.NONE so arenas can only marked as in use
    // or not in use by the appropriate methods in ArenaHandler
    @Getter
    @Setter(AccessLevel.PACKAGE)

    private static Set<String> kits = new HashSet<>();

    public Arena() {
    } // for gson

    public Arena(String schematic, int copy, Cuboid bounds) {
        this.schematic = Preconditions.checkNotNull(schematic);
        this.copy = copy;
        this.bounds = Preconditions.checkNotNull(bounds);

        scanLocations();
        this.maxbuild = calculateMaxBuild();
        this.deadzone = calculateDeadzone();
    }

    public Location getSpectatorSpawn() {
        // if it's been defined in the actual map file or calculated before
        if (spectatorSpawn != null) {
            return spectatorSpawn;
        }

        int xDiff = Math.abs(team1Spawn.getBlockX() - team2Spawn.getBlockX());
        int yDiff = Math.abs(team1Spawn.getBlockY() - team2Spawn.getBlockY());
        int zDiff = Math.abs(team1Spawn.getBlockZ() - team2Spawn.getBlockZ());

        int newX = Math.min(team1Spawn.getBlockX(), team2Spawn.getBlockX()) + (xDiff / 2);
        int newY = Math.min(team1Spawn.getBlockY(), team2Spawn.getBlockY()) + (yDiff / 2);
        int newZ = Math.min(team1Spawn.getBlockZ(), team2Spawn.getBlockZ()) + (zDiff / 2);

        ArenaHandler arenaHandler = Phantom.getInstance().getArenaHandler();
        spectatorSpawn = new Location(arenaHandler.getArenaWorld(), newX, newY, newZ);

        while (spectatorSpawn.getBlock().getType().isSolid()) {
            spectatorSpawn = spectatorSpawn.add(0, 1, 0);
        }

        return spectatorSpawn;
    }

    private void scanLocations() {
        // iterating the cuboid doesn't work because
        // its iterator is broken :(
        forEachBlock(block -> {
            Material type = block.getType();

            if (block.getState() instanceof Sign) {
                org.bukkit.block.Sign sign = (org.bukkit.block.Sign) block.getState();
                for (String line : sign.getLines()) {
                    if (line == null) continue;

                    String lower = line.toLowerCase();

                    if (lower.contains("cage_blue_min")) {
                        cageBlueMin = block.getLocation().clone();
                        block.setType(Material.AIR);
                    } else if (lower.contains("cage_blue_max")) {
                        cageBlueMax = block.getLocation().clone();
                        block.setType(Material.AIR);
                    } else if (lower.contains("cage_red_min")) {
                        cageRedMin = block.getLocation().clone();
                        block.setType(Material.AIR);
                    } else if (lower.contains("cage_red_max")) {
                        cageRedMax = block.getLocation().clone();
                        block.setType(Material.AIR);
                    }
                }
            }

            if (type != Material.SKULL) {
                return;
            }

            Skull skull = (Skull) block.getState();
            Block below = block.getRelative(BlockFace.DOWN);

            Location skullLocation = block.getLocation().clone().add(0.5, 1.5, 0.5);
            skullLocation.setYaw(AngleUtils.faceToYaw(skull.getRotation()) + 90);

            switch (skull.getSkullType()) {
                case SKELETON:
                    spectatorSpawn = skullLocation;

                    block.setType(Material.AIR);

                    if (below.getType() == Material.FENCE) {
                        below.setType(Material.AIR);
                    }

                    break;
                case PLAYER:
                    if (team1Spawn == null) {
                        team1Spawn = skullLocation;
                    } else {
                        team2Spawn = skullLocation;
                    }

                    block.setType(Material.AIR);

                    if (below.getType() == Material.FENCE) {
                        below.setType(Material.AIR);
                    }

                    break;
                case CREEPER:
                    block.setType(Material.AIR);

                    if (below.getType() == Material.FENCE) {
                        below.setType(Material.AIR);
                    }

                    if (eventSpawns == null) {
                        eventSpawns = new ArrayList<>();
                    }

                    if (!(eventSpawns.contains(skullLocation))) {
                        eventSpawns.add(skullLocation);
                    }
                    break;
                case WITHER:
                    team1Spawn = skullLocation;

                    block.setType(Material.AIR);

                    if (below.getType() == Material.FENCE) {
                        below.setType(Material.AIR);
                    }

                    break;
                case ZOMBIE:
                    team2Spawn = skullLocation;

                    block.setType(Material.AIR);

                    if (below.getType() == Material.FENCE) {
                        below.setType(Material.AIR);
                    }

                    break;
                default:
                    break;
            }
        });

        Preconditions.checkNotNull(team1Spawn, "Team 1 spawn (player skull) cannot be null.");
        Preconditions.checkNotNull(team2Spawn, "Team 2 spawn (player skull) cannot be null.");
    }

    private void forEachBlock(Callback<Block> callback) {
        Location start = bounds.getLowerNE();
        Location end = bounds.getUpperSW();
        World world = bounds.getWorld();

        for (int x = start.getBlockX(); x < end.getBlockX(); x++) {
            for (int y = start.getBlockY(); y < end.getBlockY(); y++) {
                for (int z = start.getBlockZ(); z < end.getBlockZ(); z++) {
                    callback.callback(world.getBlockAt(x, y, z));
                }
            }
        }
    }


    private void forEachChunk(Callback<Chunk> callback) {
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

    @Override
    public boolean equals(Object o) {
        if (o instanceof Arena) {
            Arena a = (Arena) o;
            return a.schematic.equals(schematic) && a.copy == copy;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(schematic, copy);
    }

    /**
     * @return ArenaSchematic linked to this arena
     */
    public ArenaSchematic getParentArena() {
        Validate.notNull(getSchematic());
        return Phantom.getInstance().getArenaHandler().getSchematic(getSchematic());
    }

    /**
     *
     * @return
     */
    public int calculateMaxBuild() {
        Location spec = getSpectatorSpawn().clone();
        return spec.getBlockY() - 2;
    }

    /**
     *
     * @return
     */
    public int calculateDeadzone() {
        Location spec = getSpectatorSpawn().clone();
        return spec.getBlockY() - 20;
    }
}