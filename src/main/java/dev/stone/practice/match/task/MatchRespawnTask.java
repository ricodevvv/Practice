package dev.stone.practice.match.task;

import dev.stone.practice.config.Lenguaje;
import dev.stone.practice.match.Match;
import dev.stone.practice.match.MatchState;
import dev.stone.practice.match.MatchTaskTicker;
import dev.stone.practice.match.team.TeamPlayer;
import dev.stone.practice.util.Common;
import dev.stone.practice.util.TitleSender;
import dev.stone.practice.util.VisibilityController;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.entity.Player;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 13/06/2025
 * Project: Practice
 */
public class MatchRespawnTask extends MatchTaskTicker {
    private final Match match;
    private final TeamPlayer teamPlayer;

    public MatchRespawnTask(Match match, TeamPlayer teamPlayer) {
        super(0, 20, false, match);
        this.match = match;
        this.teamPlayer = teamPlayer;
    }

    @Override
    public void onRun() {
        if (teamPlayer == null || !teamPlayer.isRespawning() || teamPlayer.isDisconnected() || teamPlayer.getPlayer() == null || match.getState() == MatchState.STARTING || match.getState() == MatchState.ENDING) {
            cancel();
            return;
        }
        Player player = teamPlayer.getPlayer();

        if (getTicks() <= 0) {
            cancel();
            match.respawn(teamPlayer);
            return;
        }
        Common.sendMessage(player, Lenguaje.MATCH_MESSAGES.RESPAWN_IN_MESSAGE.replace("<seconds>", String.valueOf(getTicks())));
        TitleSender.sendTitle(player, Lenguaje.MATCH_MESSAGES.RESPAWN_IN_TITLE, PacketPlayOutTitle.EnumTitleAction.TITLE, 0, 21, 0);
        TitleSender.sendTitle(player, Lenguaje.MATCH_MESSAGES.RESPAWN_IN_SUBTITLE.replace("<seconds>", String.valueOf(getTicks())), PacketPlayOutTitle.EnumTitleAction.SUBTITLE, 0, 21, 0);
    }

    @Override
    public void preRun() {
        Player player = teamPlayer.getPlayer();

      //  match.displayDeathMessage(teamPlayer, teamPlayer.getPlayer());
        teamPlayer.setRespawning(true);
        match.getMatchPlayers().forEach(VisibilityController::updateVisibility);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        player.setAllowFlight(true);
        player.setFlying(true);

        //I don't know why, these two things need to be used again to function properly.
        player.setAllowFlight(true);
        player.setFlying(true);

        player.getInventory().clear();
    }

    @Override
    public TickType getTickType() {
        return TickType.COUNT_DOWN;
    }

    @Override
    public int getStartTick() {
        return match.getKit().getGameRules().getRespawnTime();
    }

    public void instantRespawn() {
        setTicks(0);
        onRun();
    }
}
