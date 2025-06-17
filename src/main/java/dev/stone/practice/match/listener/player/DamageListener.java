package dev.stone.practice.match.listener.player;

import dev.stone.practice.kit.KitGameRules;
import dev.stone.practice.match.Match;
import dev.stone.practice.match.team.TeamPlayer;
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.ProfileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 14/06/2025
 * Project: Practice
 */
public class DamageListener implements Listener {


    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true) //A fix for #307 point 1 - try to cancel the hits which anticheat cancelled
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        PlayerProfile profile = PlayerProfile.get(player);

        //profile will be null when damaged player is a citizens player NPC, but is not a pvp bot
        if (profile == null) {
            return;
        }

        if (profile.getState() == ProfileState.FIGHTING && profile.getMatch() != null) {
            Match match = profile.getMatch();
            TeamPlayer teamPlayer = match.getTeamPlayer(player);
            KitGameRules rules = match.getKit().getGameRules();

            if (teamPlayer.getProtectionUntil() > System.currentTimeMillis()) {
                event.setCancelled(true);
                return;
            }
            if (teamPlayer.isRespawning()) {
                event.setCancelled(true);
                return;
            }
            if (!teamPlayer.isAlive()) {
                event.setCancelled(true);
                return;
            }
            if (rules.isNoFallDamage() && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
                return;
            }
            if (rules.isNoDamage() && !rules.isBoxing()) { //We handle boxing damages in MatchListener.onDamageEntity
                event.setDamage(0);
                return;
            }
            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                player.damage(999999);
                event.setCancelled(true);
            }
        }
    }
}
