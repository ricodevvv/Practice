package dev.stone.practice.kit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import dev.stone.practice.match.Match;
import dev.stone.practice.queue.QueueType;

import java.lang.reflect.Field;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 09/06/2025
 * Project: PotPvpReprised
 */

@Setter
public class KitGameRules implements Cloneable {

    @Getter
    private boolean receiveKitLoadoutBook = true;
    @Getter
    private boolean deathOnWater = false;
    @Getter
    private boolean boxing = false;
    @Getter
    private boolean bed = false;
    @Getter
    private boolean breakGoal = false;
    @Getter
    private boolean portalGoal = false;
    @Getter
    private boolean projectileOnly = false;
    @Getter
    private boolean hypixelUHC = false;
    @Getter
    private boolean spleef = false;
    @Getter
    private boolean healthRegeneration = true;
    @Getter
    private boolean showHealth = true;
    @Getter
    private boolean foodLevelChange = true;
    @Getter
    private boolean point = false;
    @Getter
    private boolean rankedPoint = false;
    @Getter
    private boolean resetArenaWhenGetPoint = false;
    @Getter
    private boolean onlyLoserResetPositionWhenGetPoint = false;
    @Getter
    private boolean build = false;
    @Getter
    private boolean startFreeze = false;
    @Getter
    private boolean noDamage = false;
    @Getter
    private boolean instantGapple = false;
    @Getter
    private boolean enderPearlCooldown = false;
    @Getter
    private boolean clearBlock = false;
    @Getter
    private boolean dropItemWhenDie = true;
    @Getter
    private boolean noFallDamage = false;
    @Getter
    private boolean giveBackArrow = false;
    @Getter
    private boolean dropItems = true;
    @Getter
    private boolean teamProjectile = true;
    @Getter
    private boolean bowBoosting = true;
    @Getter
    private int respawnTime = 5;
    @Getter
    private int maximumPoints = 3;
    @Getter
    private int matchCountdownDuration = 5;
    @Getter
    private int newRoundTime = 5;
    @Getter
    private int clearBlockTime = 10;
    @Getter
    private String knockbackName = "default";

    @Override
    public KitGameRules clone() {
        KitGameRules rules = new KitGameRules();
        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
        }
        for (Field field : rules.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object obj = this.getClass().getDeclaredField(field.getName()).get(this);
                field.set(rules, obj);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
        return rules;
    }

    public boolean isPoint(Match match) {
        switch (match.getMatchType()) {
            case SOLO:
                return (point && match.getQueueType() != QueueType.RANKED) || (rankedPoint && match.getQueueType() == QueueType.RANKED);
            case SPLIT:
                //Need to check if the GameRule contains deathOnWater. This is to prevent if the GameRule contains point and the kit is sumo.
                //If it is sumo, then we should not display the point out because point should not be in sumo TeamMatch
                return ((point && match.getQueueType() != QueueType.RANKED) || (rankedPoint && match.getQueueType() == QueueType.RANKED)) && !match.getKit().getGameRules().isDeathOnWater();
            case FFA:
                return false;
        }
        return false;
    }

    @Getter
    @AllArgsConstructor
    public enum Readable {
        //boolean
        receiveKitLoadoutBook("Receive Kit Loadout Book", "Players will receive a kit loadout book when the match starts"),
        deathOnWater("Death on Water", "Players will die when they touch water"),
        boxing("Boxing", "Players will only be able to use their fists to fight"),
        bed("Bed", "Players will respawn at their bed when they die"),
        breakGoal("Break Goal", "Players need to break specific blocks to win"),
        portalGoal("Portal Goal", "Players need to enter a portal to win"),
        projectileOnly("Projectile Only", "Players can only use projectiles to fight"),
        hypixelUHC("Hypixel UHC", "Hypixel UHC mechanics will be applied"),
        spleef("Spleef", "Players need to break blocks under opponents to make them fall"),
        healthRegeneration("Health Regeneration", "Players' health will regenerate over time"),
        showHealth("Show Health", "Players' health will be visible to others"),
        foodLevelChange("Food Level Change", "Players' food level will decrease when performing actions"),
        point("Point", "Players need to reach certain points to win (Normal matches)"),
        rankedPoint("Ranked Point", "Players need to reach certain points to win (Ranked matches)"),
        resetArenaWhenGetPoint("Reset Arena on Point", "The arena will reset when a player gets a point"),
        onlyLoserResetPositionWhenGetPoint("Only Loser Reset Position", "Only the losing player will reset position when a point is scored"),
        build("Build", "Players can place and break blocks"),
        startFreeze("Start Freeze", "Players will be frozen at the start of the match"),
        noDamage("No Damage", "Players cannot deal or receive damage"),
        instantGapple("Instant Gapple", "Golden apples will heal instantly"),
        enderPearlCooldown("Ender Pearl Cooldown", "Ender pearls will have a cooldown"),
        clearBlock("Clear Block", "Placed blocks will be cleared after some time"),
        dropItemWhenDie("Drop Items on Death", "Players will drop their items when they die"),
        noFallDamage("No Fall Damage", "Players won't receive fall damage"),
        giveBackArrow("Give Back Arrow", "Players will get their arrows back when shooting"),
        dropItems("Drop Items", "Players can drop items manually"),
        teamProjectile("Team Projectile", "Projectiles won't hurt teammates"),
        bowBoosting("Bow Boosting", "Players can boost themselves using bows"),
        //integer
        respawnTime("Respawn Time", "Time in seconds before players respawn"),
        maximumPoints("Maximum Points", "Maximum points needed to win the match"),
        matchCountdownDuration("Match Countdown", "Time in seconds before match starts"),
        newRoundTime("New Round Time", "Time in seconds before new round starts"),
        clearBlockTime("Clear Block Time", "Time in seconds before placed blocks are cleared"),
        //String
        knockbackName("Knockback Profile", "The knockback profile to use for this kit");

        private final String rule;
        private final String description;
    }
}
