package net.frozenorb.potpvp.commands.impl.profile;

import net.frozenorb.potpvp.commands.PotPvPCommand;
import net.frozenorb.potpvp.profile.Profile;
import net.frozenorb.potpvp.util.CC;
import org.bukkit.entity.Player;
import xyz.refinedev.command.annotation.Command;
import xyz.refinedev.command.annotation.Require;
import xyz.refinedev.command.annotation.Sender;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 10/06/2025
 * Project: Practice
 */
public class ProfileBuildCommand implements PotPvPCommand {
    
    @Command(name = "", desc = "activate construction mode for your profile")
    @Require("phantom.profile.build")
    public void BuilcDommandExecute(@Sender Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if(profile == null) return;
        profile.setBuild(!profile.isBuild());
        player.sendMessage("Build mode " + (profile.isBuild() ? CC.GREEN + "Enabled" : CC.RED + "Disabled") + ".");
    }
    
    @Override
    public String getCommandName() {
        return "build";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }
}
