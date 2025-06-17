package dev.stone.practice.match.listener.blocks;

import dev.stone.practice.config.Lenguaje;
import dev.stone.practice.kit.Kit;
import dev.stone.practice.match.Match;
import dev.stone.practice.match.MatchState;
import dev.stone.practice.match.team.Team;
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.ProfileState;
import dev.stone.practice.util.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 16/06/2025
 * Project: Practice
 */
public class BlockBreak implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = PlayerProfile.get(player);
        Block block = event.getBlock();

        if (profile.getState() == ProfileState.FIGHTING && profile.getMatch() != null) {
            Match match = profile.getMatch();
            if (match.getState() != MatchState.FIGHTING) {
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
            if (match.isProtected(block.getLocation(), false)) {
                event.setCancelled(true);
                return;
            }

            match.getPlacedBlocks().remove(block.getLocation());

            Kit kit = match.getKit();
            if (block.getType() == Material.BED_BLOCK) {
                //Now get the bed location
                Location bedLocation1 = new Location(block.getLocation().getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ());
                Location bedLocation2 = Util.getBedBlockNearBy(bedLocation1).clone(); //因為一張床等於兩個方塊, 所以需要床的另一邊位置

                Team team = match.getTeam(player);
                Team opponentTeam = match.getTeams().stream().min(Comparator.comparing(t -> t.getSpawnLocation().distance(bedLocation1))).orElse(null);

                event.setCancelled(true);

                if (team == opponentTeam) {
                    player.sendMessage(Lenguaje.MATCH_MESSAGES.CANNOT_BREAK_OWN_BED);
                    return;
                }

                if (kit.getGameRules().isBed()) {
                    match.broadcastSound(team, Sound.ENDERDRAGON_GROWL);
                    match.broadcastSound(opponentTeam, Sound.WITHER_DEATH);
                    match.broadcastSpectatorsSound(Sound.ENDERDRAGON_GROWL);
                    match.broadcastTitle(opponentTeam, Lenguaje.MATCH_MESSAGES.BED_BREAK_TITLE);
                    match.broadcastSubTitle(opponentTeam, Lenguaje.MATCH_MESSAGES.BED_BREAK_SUBTITLE);
                    for (String line : Lenguaje.MATCH_MESSAGES.BED_BREAK_MESSAGE) {
                        match.broadcastMessage(line.replaceAll("<team>", opponentTeam.getTeamColor().getTeamName())
                                .replace("<player>", player.getName()));
                    }
                    opponentTeam.setBedDestroyed(true);
                    bedLocation1.getBlock().setType(Material.AIR);
                    bedLocation2.getBlock().setType(Material.AIR);
                    return;
                } else if (kit.getGameRules().isBreakGoal()) {
                    match.score(profile, null, match.getTeamPlayer(player));
                    return;
                }
            }
            if (kit.getGameRules().isSpleef()) {
                if (block.getType() == Material.SNOW_BLOCK && player.getInventory().firstEmpty() != -1 && 3 > ThreadLocalRandom.current().nextInt(0, 100)) {
                    player.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 3));
                }
            } else {
                block.getDrops().forEach(itemStack -> {
                    Item item = block.getLocation().getWorld().dropItemNaturally(block.getLocation().clone().add(0.5,0.5,0.5), itemStack);
                    match.addDroppedItem(item, player.getName());
                });
            }
            block.setType(Material.AIR);
            event.setCancelled(true);
        }
    }

}
