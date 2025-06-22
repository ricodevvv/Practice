package dev.stone.practice.profile.settings;

import dev.stone.practice.config.Config;
import dev.stone.practice.profile.themes.Themes;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 22/06/2025
 * Project: Practice
 */
@Accessors(fluent = true)
public class ProfileOptions {

    @Getter @Setter private boolean showScoreboard = true;
    @Getter @Setter private boolean receiveDuelRequests = true;
    @Getter @Setter private boolean allowSpectators = true;
    @Getter @Setter private boolean spectatorMessages = true;
    @Getter @Setter private boolean showPlayers = true;
   // @Getter @Setter private Times time = Times.DAY;
   // @Getter @Setter private SpecialEffects killEffect = SpecialEffects.NONE;
    @Getter @Setter private boolean menuSounds = false;
   // @Getter @Setter private KillMessages killMessage = KillMessages.NONE;
  //  @Getter @Setter private Trail trail = Trail.NONE;
    @Getter @Setter private int pingRange = 250;
    @Getter @Setter private Themes theme = Themes.valueOf(Config.DEFAULT_THEME);
}
