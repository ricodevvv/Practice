package dev.stone.practice.match.team;

import lombok.Getter;
import lombok.Setter;
import dev.stone.practice.config.Config;
import dev.stone.practice.kit.KitLoadout;
import dev.stone.practice.match.Match;
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.util.Tasks;
import dev.stone.practice.util.TitleSender;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.util.UUID;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 11/06/2025
 * Project: Practice
 */
@Getter
public class TeamPlayer {

    private final UUID uuid;
    private final String username;
    @Setter private boolean alive = true;
    @Setter private boolean respawning = false;
    @Setter private boolean disconnected = false;
    @Setter private int potionsThrown;
    @Setter private int potionsMissed;
    @Setter private int hits;
    @Setter private int blockedHits;
    @Setter private int gotHits;
    @Setter private TeamPlayer lastHitDamager;
    @Setter private int combo;
    @Setter private int longestCombo;
    @Setter private double damageDealt;
    @Setter private KitLoadout kitLoadout;
    @Setter private long protectionUntil = -1;

    public TeamPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.username = player.getName();
    }

    public TeamPlayer(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public PlayerProfile getPlayerProfile() {
        return PlayerProfile.getByUuid(uuid);
    }

    public int getPing() {
        Player player = getPlayer();
        return player == null ? 0 : player.spigot().getPing();
    }

    public void broadcastTitle(String title, String subtitle) {
        TitleSender.sendTitle(getPlayer(), title, PacketPlayOutTitle.EnumTitleAction.TITLE, 0, Config.MATCH_SETTINGS.END_DURATION ,5);
        TitleSender.sendTitle(getPlayer(), subtitle, PacketPlayOutTitle.EnumTitleAction.SUBTITLE, 0, Config.MATCH_SETTINGS.END_DURATION ,5);
    }

    public void teleport(Location location) {
        if (getPlayer() == null) {
            return;
        }
        Tasks.run(()-> getPlayer().teleport(location));
    }

    public void addPotionsThrown() {
        potionsThrown++;
    }

    public void addPotionsMissed() {
        potionsMissed++;
    }

    public void handleHit(double damage) {
        hits++;
        combo++;
        damageDealt = damageDealt + damage;
        if (combo > longestCombo) {
            longestCombo = combo;
        }
        protectionUntil = -1;
    }

    public void handleGotHit(TeamPlayer damager, boolean blockedHit) {
        gotHits++;
        combo = 0;
        lastHitDamager = damager;
        if (blockedHit) {
            blockedHits++;
        }
    }

    public void respawn(Match match) {
        if (getPlayer() == null) {
            return;
        }
        kitLoadout.apply(match, getPlayer());
        ((CraftPlayer) getPlayer()).getHandle().setAbsorptionHearts(0);
        getPlayer().setHealth(getPlayer().getMaxHealth());
        getPlayer().getActivePotionEffects().clear();
        lastHitDamager = null;
    }

}

