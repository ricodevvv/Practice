package dev.stone.practice.match.listener.blocks;

import dev.stone.practice.config.Config;
import dev.stone.practice.match.Match;
import dev.stone.practice.match.MatchState;
import dev.stone.practice.profile.Profile;
import dev.stone.practice.profile.ProfileState;
import dev.stone.practice.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 16/06/2025
 * Project: Practice
 */
public class BlockPlace implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.get(player);
        Block block = event.getBlockPlaced();

        if (profile.getState() == ProfileState.FIGHTING && profile.getMatch() != null) {
            Match match = profile.getMatch();
            if (match.getState() == MatchState.STARTING && match.getKit().getGameRules().isStartFreeze()) {
                event.setCancelled(true);
                return;
            }
            if (match.getState() == MatchState.ENDING) {
                event.setCancelled(true);
                return;
            }
            if (!match.getTeamPlayer(player).isAlive() || match.getTeamPlayer(player).isRespawning()) {
                event.setCancelled(true);
                return;
            }
            if (!match.getKit().getGameRules().isBuild()) {
                event.setCancelled(true);
                return;
            }
            if (match.isProtected(block.getLocation(), true, block)) {
                event.setCancelled(true);
                return;
            }

            if (block.getType() == Material.TNT) {
                ItemStack itemStack = player.getItemInHand();
                itemStack.setAmount(itemStack.getAmount() - 1);
                player.setItemInHand(itemStack);

                final TNTPrimed tntPrimed = event.getBlock().getLocation().getWorld().spawn(event.getBlock().getLocation().clone().add(0.5, 0.0, 0.5), TNTPrimed.class);
                tntPrimed.setYield((float) Config.EXPLOSIVE_CONFIG.TNT_CONFIG.YIELD);
                tntPrimed.setFuseTicks(Config.EXPLOSIVE_CONFIG.TNT_CONFIG.FUSE_TICKS);

                Util.setSource(tntPrimed, player);

                event.setCancelled(true);
                return;
            }

            match.getPlacedBlocks().add(block.getLocation());

         /*  if (match.getKit().getGameRules().isClearBlock()) {
                TeamPlayer teamPlayer = match.getTeamPlayer(player);

                new MatchClearBlockTask(match, match.getKit().getGameRules().getClearBlockTime(), block.getWorld(), block.getLocation(), teamPlayer, (itemStacks) -> {
                    if (player.isOnline() && match == profile.getMatch() && !teamPlayer.isRespawning() && teamPlayer.isAlive()) {
                        itemStacks.forEach(i -> player.getInventory().addItem(i));
                    }
                });
            } */
        }
    }
}
