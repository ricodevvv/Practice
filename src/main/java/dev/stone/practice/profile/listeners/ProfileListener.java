package dev.stone.practice.profile.listeners;

import dev.stone.practice.Phantom;
import dev.stone.practice.profile.Profile;
import dev.stone.practice.profile.ProfileState;
import dev.stone.practice.util.*;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 10/06/2025
 * Project: Phantom
 */
public class ProfileListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        try {
            Profile profile = new Profile(event.getUniqueId());
            profile.load();
            Profile.getProfiles().put(event.getUniqueId(), profile);
        } catch (Exception e) {
            e.printStackTrace();
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(ChatColor.RED + "Failed to load your profile. Try again later.");
            return;
        }
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        //Reset their inventory and their location, to prevent player stuck in other places or contains illegal items
        PlayerUtil.reset(player);
        Phantom.getInstance().getLobbyManager().teleport(player);
        try {
            Phantom.getInstance().getLobbyManager().sendToSpawnAndReset(player);
        } catch (Exception e) {
            Common.log("An error ocurred in " + getClass().getSimpleName() + "" + e.getMessage());
        }
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if(profile == null) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                profile.save();
            }
        }.runTaskAsynchronously(Phantom.getInstance());
        Profile.getProfiles().remove(player.getUniqueId());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (!Checker.canDamage(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        if (!Checker.canDamage(player)) {
            event.setFoodLevel(20);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (profile.getState() == ProfileState.LOBBY) {
            event.setCancelled(!profile.isBuild());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
    Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());
        if (!Checker.canDamage(event.getPlayer())) {
            event.setCancelled(!profile.isBuild());
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!Checker.canDamage(player)) {
            event.setCancelled(event.getPlayer().getGameMode() != GameMode.CREATIVE);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (!Checker.canDamage(player)) {
            event.setCancelled(event.getPlayer().getGameMode() != GameMode.CREATIVE);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());

        if(profile.getState() != ProfileState.FIGHTING) {
            event.setCancelled(!profile.isBuild());
        }
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }

        if (event.getAction().name().startsWith("RIGHT_")) {
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
            if (nmsItem.hasTag()) {
                NBTTagCompound compound = nmsItem.getTag();
                if (compound.hasKey("command")) {
                    String command = compound.getString("command");
                    Util.performCommand(player, command);
                }
            }
        }
    }
}
