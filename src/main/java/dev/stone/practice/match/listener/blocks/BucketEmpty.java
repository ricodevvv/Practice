package dev.stone.practice.match.listener.blocks;

import dev.stone.practice.match.Match;
import dev.stone.practice.match.MatchState;
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.ProfileState;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 16/06/2025
 * Project: Practice
 */
public class BucketEmpty implements Listener {

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = PlayerProfile.get(player);
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());

        if (profile.getState() == ProfileState.FIGHTING && profile.getMatch() != null) {
            Match match = profile.getMatch();
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
            if (match.isProtected(block.getLocation(), true)) {
                event.setCancelled(true);
                return;
            }

            match.getPlacedBlocks().add(block.getLocation());
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = PlayerProfile.get(player);
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());

        if (profile.getState() == ProfileState.FIGHTING && profile.getMatch() != null) {
            Match match = profile.getMatch();
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
            if (match.isProtected(block.getLocation(), true)) {
                event.setCancelled(true);
                return;
            }

            match.getPlacedBlocks().remove(block.getLocation());
        }
    }
}
