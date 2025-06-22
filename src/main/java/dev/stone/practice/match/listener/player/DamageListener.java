package dev.stone.practice.match.listener.player;

import dev.stone.practice.kit.Kit;
import dev.stone.practice.kit.KitGameRules;
import dev.stone.practice.match.Match;
import dev.stone.practice.match.MatchState;
import dev.stone.practice.match.team.Team;
import dev.stone.practice.match.team.TeamPlayer;
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.ProfileState;
import dev.stone.practice.util.DamageCalculator;
import dev.stone.practice.util.Util;
import dev.stone.practice.util.exception.PracticeUnexpectedException;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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

    @EventHandler(priority = EventPriority.HIGHEST) //Allow the above EntityDamageEvent run first
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && (event.getDamager() instanceof Player || event.getDamager() instanceof FishHook || event.getDamager() instanceof Snowball || event.getDamager() instanceof Egg || event.getDamager() instanceof Arrow)) {
            Player entity = (Player) event.getEntity();
            Player damager = event.getDamager() instanceof Projectile ? (Player) ((Projectile) event.getDamager()).getShooter() : (Player) event.getDamager();

            //Damager might be null because there might be a chance when arrow hit the entity, the damager isn't online
            if (damager == null) {
                event.setCancelled(true);
                return;
            }

            PlayerProfile entityProfile = PlayerProfile.get(entity);
            PlayerProfile damagerProfile = PlayerProfile.get(damager);

            //profile will be null when damaged player is a citizens player NPC, but is not a pvp bot
            if (entityProfile == null) {
                return;
            }

            if (entityProfile.getState() == ProfileState.FIGHTING && damagerProfile.getState() == ProfileState.FIGHTING) {
                Match match = entityProfile.getMatch();
                Kit kit = match.getKit();



                //It is cancelled in EntityDamageEvent. Check this again to prevent Boxing hits.
                if (match.getState() != MatchState.FIGHTING) {
                    event.setCancelled(true);
                    return;
                }

                Team teamEntity = match.getTeam(entity);
                Team teamDamager = match.getTeam(damager);

                if (teamEntity == teamDamager) {
                    if (entity != damager) {
                        if (!kit.getGameRules().isTeamProjectile() && event.getDamager() instanceof Projectile) {
                            event.setCancelled(true);
                            return;
                        } else if (event.getDamager() instanceof Player) {
                            event.setCancelled(true);
                            return;
                        }
                    } else {
                        if (!kit.getGameRules().isBowBoosting() && event.getDamager() instanceof Arrow) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }


                if (kit.getGameRules().isProjectileOnly() && event.getDamager() instanceof Player) {
                    event.setCancelled(true);
                    return;
                }

                TeamPlayer teamPlayerEntity = match.getTeamPlayer(entity);
                TeamPlayer teamPlayerDamager = match.getTeamPlayer(damager);
                double damage = event.getDamage();

                if (!teamPlayerEntity.isAlive() || !teamPlayerDamager.isAlive() || teamPlayerEntity.isRespawning() || teamPlayerDamager.isRespawning()) {
                    event.setCancelled(true);
                    return;
                }

                if (kit.getGameRules().isBoxing() || kit.getGameRules().isNoDamage()) {
                    event.setDamage(0);
                    entity.setHealth(20.0);
                }

                if (kit.getGameRules().isBoxing()) {
                    //Check if the damage is critical damage
                    //The way bukkit handles critical damage is strange, because sometimes it might fire the same event two times with different damage
                    double predictDamage = 1 + DamageCalculator.getEnchantedDamage(damager.getItemInHand());
                    if (predictDamage > damage) {
                        return;
                    }
                }
                teamPlayerDamager.setProtectionUntil(0);
                teamPlayerDamager.handleHit(event.getFinalDamage());
                teamPlayerEntity.handleGotHit(match.getTeamPlayer(damager), entity.isBlocking());

                if (event.getDamager() instanceof Arrow && entity != damager) {
                    Util.sendArrowHitMessage(event);

                }
                if (kit.getGameRules().isBoxing() && match.getTeam(entity).getGotHits() >= match.getMaximumBoxingHits()) {
                    match.getTeam(entity).getAliveTeamPlayers().forEach(teamPlayer -> {
                        teamPlayer.setProtectionUntil(0); //Allow our system to damage the player
                       teamPlayer.getPlayer().damage(9999999);
                    });
                }
            }
        } /* else if (event.getEntity() instanceof Player && event.getDamager() instanceof Fireball && Config.MATCH_FIREBALL_ENABLED.toBoolean()) {
            Player player = (Player) event.getEntity();

            if (Config.MATCH_FIREBALL_KNOCKBACK_ENABLED.toBoolean()) {
                event.setCancelled(true);
                player.damage(event.getDamage() / Config.MATCH_FIREBALL_DIVIDE_DAMAGE.toDouble());
                Util.pushAway(player, event.getDamager().getLocation(), Config.MATCH_FIREBALL_KNOCKBACK_VERTICAL.toDouble(), Config.MATCH_FIREBALL_KNOCKBACK_HORIZONTAL.toDouble());
            } else {
                event.setDamage(event.getDamage() / Config.MATCH_FIREBALL_DIVIDE_DAMAGE.toDouble());
            }
        } else if (event.getEntity() instanceof Player && event.getDamager() instanceof TNTPrimed && Config.MATCH_TNT_ENABLED.toBoolean()) {
            Player player = (Player) event.getEntity();

            if (Config.MATCH_TNT_ENABLED.toBoolean()) {
                event.setCancelled(true);
                player.damage(event.getDamage() / Config.MATCH_TNT_DIVIDE_DAMAGE.toDouble());
                Util.pushAway(player, event.getDamager().getLocation(), Config.MATCH_TNT_KNOCKBACK_VERTICAL.toDouble(), Config.MATCH_TNT_KNOCKBACK_HORIZONTAL.toDouble());
            } else {
                event.setDamage(event.getDamage() / Config.MATCH_TNT_DIVIDE_DAMAGE.toDouble());
            }
        } */
    }
}
