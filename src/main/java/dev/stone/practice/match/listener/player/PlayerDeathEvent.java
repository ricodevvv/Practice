package dev.stone.practice.match.listener.player;

import dev.stone.practice.kit.KitGameRules;
import dev.stone.practice.match.Match;
import dev.stone.practice.match.team.TeamPlayer;
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.ProfileState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 13/06/2025
 * Project: Practice
 */
public class PlayerDeathEvent implements Listener {

    @EventHandler
    public void OnPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent event) {
        event.setDeathMessage(null);

        Player player = event.getEntity();
        PlayerProfile profile = PlayerProfile.get(player);

        if(profile == null) return;

        if(profile.getState() == ProfileState.FIGHTING && profile.getMatch() != null) {
            Match match = profile.getMatch();
            TeamPlayer teamPlayer = match.getTeamPlayer(player);
            KitGameRules gameRules = match.getKit().getGameRules();
            if ((gameRules.isBed() && !match.getTeam(player).isBedDestroyed()) || gameRules.isBreakGoal() || gameRules.isPortalGoal()) {
            //    new MatchRespawnTask(match, teamPlayer);
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

            if (gameRules.isDropItemWhenDie()) {

                //This drops List will filter useless and banned items, and pots/bowls if the match is ending
                List<ItemStack> drops = event.getDrops();
                drops.removeIf(i -> i == null || i.getType() == Material.AIR || i.getType() == Material.BOOK || i.getType() == Material.ENCHANTED_BOOK);
                if (match.canEnd()) {
                    drops.removeIf(i -> i.getType() == Material.POTION || i.getType() == Material.GLASS_BOTTLE || i.getType() == Material.MUSHROOM_SOUP || i.getType() == Material.BOWL);
                }

                for (ItemStack itemStack : drops) {
               //     Item item = Util.dropItemNaturally(player.getLocation(), itemStack, player);
                  //  match.addDroppedItem(item, null); //Already modified the f value of EntityItem, therefore no need to put anything in 2nd variables
                }
            }

        }

        player.setHealth(20);
        player.setVelocity(new Vector());

        event.setDroppedExp(0);
        event.getDrops().clear();


          player.teleport(player.getLocation().clone().add(0,2,0));    //Teleport 2 blocks higher, to try to re-do what MineHQ did (Make sure to place this line of code after setHealth, otherwise it won't work)
        }
    }

