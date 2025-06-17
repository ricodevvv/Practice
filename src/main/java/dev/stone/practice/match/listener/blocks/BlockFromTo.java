package dev.stone.practice.match.listener.blocks;

import dev.stone.practice.Phantom;
import dev.stone.practice.arena.Arena;
import dev.stone.practice.events.KitLoadoutReceivedEvent;
import dev.stone.practice.kit.Kit;
import dev.stone.practice.match.Match;
import dev.stone.practice.match.MatchHandler;
import dev.stone.practice.match.team.TeamPlayer;
import dev.stone.practice.util.Cuboid;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 16/06/2025
 * Project: Practice
 */
public class BlockFromTo implements Listener {

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        Block block = event.getToBlock();
        if (block == null) {
            return;
        }
       Phantom.getInstance().getMatchHandler().getMatches().forEach((uuid, match) -> {
           Arena arenaDetail = match.getArenaDetail();
           if (arenaDetail == null) {
               return;
           }

           Cuboid cuboid = arenaDetail.getBounds();
           Location blockLocation = block.getLocation();

           if (cuboid.contains(blockLocation)) {
               match.getPlacedBlocks().add(blockLocation);
           }
       });
    }

    @EventHandler
    public void onKitLoadoutReceived(KitLoadoutReceivedEvent event) {
        Player player = event.getPlayer();
        Match match = event.getMatch();
        TeamPlayer teamPlayer = match.getTeamPlayer(player);
        Kit kit = match.getKit();

        if (kit.getGameRules().isBed() || kit.getGameRules().isPoint(match)) {
            match.getTeam(player).dye(teamPlayer);
        }
        player.updateInventory();
    }
}
