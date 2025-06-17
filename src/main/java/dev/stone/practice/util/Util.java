package dev.stone.practice.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityTNTPrimed;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftTNTPrimed;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 10/06/2025
 * Project: Practice
 */
public class Util {


    /**
     * executes a command for a specific player
     * @param player the player that will be used to execute the command
     * @param command the command to be executed
     */
    public static void performCommand(Player player, String command) {
        PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, "/" + command);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            player.performCommand(command);
        }
    }

    /**
     *
     * @param loc the location where it should be scanned
     * @param radius the block radius that should be scanned
     * @return the blocks in the specified radius
     */
    public static List<Block> getBlocksAroundCenter(Location loc, int radius) {
        List<Block> blocks = new ArrayList<>();

        for (int x = (loc.getBlockX() - radius); x <= (loc.getBlockX() + radius); x++) {
            for (int y = (loc.getBlockY() - radius); y <= (loc.getBlockY() + radius); y++) {
                for (int z = (loc.getBlockZ() - radius); z <= (loc.getBlockZ() + radius); z++) {
                    Location l = new Location(loc.getWorld(), x, y, z);
                    if (l.distance(loc) <= radius) {
                        blocks.add(l.getBlock());
                    }
                }
            }
        }

        return blocks;
    }

    /**
     * scans an area in search of a bed is used for kits like BedFight
     * @param location the location where you should iterate over the blocks
     * @return the location of the bed if found
     */
    public static Location getBedBlockNearBy(Location location) {
        Location bedLocation2 = location.clone();

        if (location.clone().add(1,0,0).getBlock().getType() == Material.BED_BLOCK) {
            return location.clone().add(1,0,0);
        }if (location.clone().add(-1,0,0).getBlock().getType() == Material.BED_BLOCK) {
            return location.clone().add(-1,0,0);
        }if (location.clone().add(0,0,1).getBlock().getType() == Material.BED_BLOCK) {
            return location.clone().add(0,0,1);
        }if (location.clone().add(0,0,-1).getBlock().getType() == Material.BED_BLOCK) {
            return location.clone().add(0,0,-1);
        }
        Common.log("Cannot get another side of bed block!");
        return bedLocation2;
    }

    /**
     * links a TNT to a player
     * @param tntPrimed the TNT to be linked
     * @param player the player to whom it will be linked
     */
    public static void setSource(final TNTPrimed tntPrimed, final Player player) {
        EntityLiving handle = ((CraftLivingEntity)player).getHandle();
        EntityTNTPrimed handle2 = ((CraftTNTPrimed)tntPrimed).getHandle();
        try {
            Field declaredField = EntityTNTPrimed.class.getDeclaredField("source");
            declaredField.setAccessible(true);
            declaredField.set(handle2, handle);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static List<Player> getOnlinePlayers() {
        return ImmutableList.copyOf(Bukkit.getOnlinePlayers());
    }
}
