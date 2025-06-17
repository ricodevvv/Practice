package dev.stone.practice.util;

import dev.stone.practice.config.Config;
import dev.stone.practice.match.Match;
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.ProfileState;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 16/06/2025
 * Project: Practice
 */
@UtilityClass
public class VisibilityController {

    public void updateVisibility(Player player) {
        Tasks.run(() -> {
            for (Player target : Util.getOnlinePlayers()) {
                if (shouldSeePlayer(target, player)) {
                    target.showPlayer(player);
                } else {
                    target.hidePlayer(player);
                }

                if (shouldSeePlayer(player, target)) {
                    player.showPlayer(target);
                } else {
                    player.hidePlayer(target);
                }
            }
        });
    }

    private boolean shouldSeePlayer(Player viewer, Player target) {
        if (viewer == null || target == null) {
            return false;
        }

        if (viewer == target) {
            return true;
        }

        PlayerProfile pViewer = PlayerProfile.get(viewer);
        PlayerProfile pTarget = PlayerProfile.get(target);

        Match targetMatch = pTarget.getMatch();

        if (targetMatch == null) {
            //We're not in a match, so we hide other players based on their party/match
          //  Party targetParty = Party.getByPlayer(target);

            boolean configSettings = Config.LOBBY_DISPLAY_PLAYERS;
            boolean viewerPlayingMatch = pViewer.getState() == ProfileState.FIGHTING && pViewer.getMatch() != null;
          //  boolean viewerSameParty = targetParty != null && targetParty.getMember(viewer.getUniqueId()) != null;

            return configSettings || viewerPlayingMatch; //|| viewerSameParty;
        } else {
            //We're in a match, so we only hide other spectators (if our settings say so)
            boolean targetIsSpectator = targetMatch.getSpectators().contains(target) || !targetMatch.getTeamPlayer(target).isAlive() || targetMatch.getTeamPlayer(target).isRespawning();
          // boolean viewerSpectateSetting = pViewer.getSettings().get(ProfileSettings.SPECTATOR_VISIBILITY).isEnabled();
            boolean viewerIsSpectator = pViewer.getState() == ProfileState.SPECTATING && pViewer.getMatch() != null;
            //Also check if the match is the same or not
            boolean viewerMatchIsSame = targetMatch == pViewer.getMatch();

            return (!targetIsSpectator || (/* viewerSpectateSetting && */ viewerIsSpectator)) && viewerMatchIsSame;
        }
    }

}
