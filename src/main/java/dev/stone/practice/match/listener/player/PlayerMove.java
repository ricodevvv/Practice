package dev.stone.practice.match.listener.player;

import dev.stone.practice.arena.Arena;
import dev.stone.practice.kit.Kit;
import dev.stone.practice.kit.KitGameRules;
import dev.stone.practice.match.Match;
import dev.stone.practice.match.MatchState;
import dev.stone.practice.match.task.MatchRespawnTask;
import dev.stone.practice.match.team.Team;
import dev.stone.practice.match.team.TeamPlayer;
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.ProfileState;
import dev.stone.practice.profile.cooldown.CooldownType;
import dev.stone.practice.util.Common;
import dev.stone.practice.util.Cuboid;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Comparator;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 16/06/2025
 * Project: Practice
 */
public class PlayerMove implements Listener {


    @EventHandler
    public void OnPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        PlayerProfile profile = PlayerProfile.get(player);
        if(profile == null) return;

        Block block = to.getBlock();
        Block underBlock = to.clone().add(0, -1, 0).getBlock();

        if(profile.getState() == ProfileState.FIGHTING && profile.getMatch() != null) {
            Match match = profile.getMatch();
            Arena arena = match.getArenaDetail();
            Kit kit = match.getKit();
            KitGameRules gameRules = kit.getGameRules();

            if (gameRules.isStartFreeze() && match.getState() == MatchState.STARTING && (from.getX() != to.getX() || from.getZ() != to.getZ())) {
                Location location = match.getTeam(player).getSpawnLocation();
                //https://github.com/diamond-rip/Eden/issues/389#issuecomment-1630048579 - Smoother looking by only changing the player's x and z location
                location.setY(from.getY());
                location.setPitch(from.getPitch());
                location.setYaw(from.getYaw());
                player.teleport(location);
                return;
            }
            if ((!arena.getBounds().clone().outset(Cuboid.CuboidDirection.HORIZONTAL, 10).contains(player) || arena.getDeadzone() > player.getLocation().getY())) {
                onDeath(player);
                return;
            }
            //Prevent any duplicate scoring
            //If two people go into the portal at the same time in bridge, it will count as +2 points
            //If player go into the water and PlayerMoveEvent is too slow to perform teleportation, it will run MatchNewRoundTask multiple times
            if (match.getMatchPlayers().stream().allMatch(p -> PlayerProfile
                    .get(p)
                    .getCooldowns()
                    .get(CooldownType.SCORE)
                    .isExpired())) {
                TeamPlayer teamPlayer = match.getTeamPlayer(player);
                if (match.getState() == MatchState.FIGHTING && !teamPlayer.isRespawning()) {
                    //Check KitGameRules for Death on Water
                    if (gameRules.isDeathOnWater() && (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER)) {
                        if (gameRules.isPoint(match)) {
                            TeamPlayer lastHitDamager = teamPlayer.getLastHitDamager();
                            //Players have a chance to die without being attacked by the enemy, such as lava. If so, just randomly draw a player from the enemy team.
                            if (lastHitDamager == null) {
                                lastHitDamager = match.getOpponentTeam(match.getTeam(player)).getAliveTeamPlayers().get(0);
                            }
                            match.score(profile, teamPlayer, lastHitDamager);
                        } else {
                           player.damage(99999);
                        }
                        return;
                    }

                    //Check KitGameRules into target
                    if (gameRules.isPortalGoal() && block.getType() == Material.ENDER_PORTAL) {
                        Team playerTeam = match.getTeam(player);
                        Team portalBelongsTo = match.getTeams().stream().min(Comparator.comparing(team -> team.getSpawnLocation().distance(to))).orElse(null);
                        if (portalBelongsTo == null) {
                            Common.log("An error occurred while finding portalBelongsTo, please contact GoodestEnglish to fix");
                            return;
                        }
                        if (portalBelongsTo != playerTeam) {
                            match.score(profile, null, match.getTeamPlayer(player));
                        } else {
                            //Prevent player scoring their own goal
                            player.damage(999999);

                        }
                    }
                }
            }
        } else if (profile.getState() == ProfileState.SPECTATING && profile.getMatch() != null) {
            Match match = profile.getMatch();
            Arena arena = match.getArenaDetail();
            if (!arena.getBounds().clone().outset(Cuboid.CuboidDirection.HORIZONTAL, 40).contains(player) || arena.getDeadzone() > player.getLocation().getY()) {
                player.teleport(arena.getSpectatorSpawn());
            }
        }
    }
    public void onDeath(Player player) {
        PlayerProfile profile = PlayerProfile.get(player);
        if(profile.getMatch().canEnd()) {
            return;
        }
        if (profile.getState() == ProfileState.FIGHTING && profile.getMatch() != null) {
            Match match = profile.getMatch();
            TeamPlayer teamPlayer = match.getTeamPlayer(player);
            KitGameRules gameRules = match.getKit().getGameRules();
            if ((gameRules.isBed() && !match.getTeam(player).isBedDestroyed()) || gameRules.isBreakGoal() || gameRules.isPortalGoal()) {
                new MatchRespawnTask(match, teamPlayer);
            } else if (gameRules.isPoint(match)) {
                TeamPlayer lastHitDamager = teamPlayer.getLastHitDamager();
                //Players have a chance to die without being attacked by the enemy, such as lava. If so, just randomly draw a player from the enemy team.
                if (lastHitDamager == null) {
                    lastHitDamager = match.getOpponentTeam(match.getTeam(player)).getAliveTeamPlayers().get(0);
                }
                match.score(profile, teamPlayer, lastHitDamager);
            } else {
                match.die(player, false);
            }
          /*  if (gameRules.isClearBlock()) {
                match.getTasks().stream()
                        .filter(task -> task instanceof MatchClearBlockTask)
                        .map(task -> (MatchClearBlockTask) task)
                        .filter(task -> task.getBlockPlacer() == teamPlayer)
                        .forEach(task -> task.setActivateCallback(false));
            } */
        }
        player.setHealth(20);
        player.setVelocity(new Vector());
        player.teleport(player.getLocation().clone().add(0,2,0)); //Teleport 2 blocks higher, to try to re-do what MineHQ did (Make sure to place this line of code after setHealth, otherwise it won't work)
    }
}
