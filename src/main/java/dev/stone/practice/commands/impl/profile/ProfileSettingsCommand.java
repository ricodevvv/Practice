package dev.stone.practice.commands.impl.profile;

import dev.stone.practice.commands.PotPvPCommand;
import dev.stone.practice.profile.settings.menu.ProfileSettingsMenu;
import org.bukkit.entity.Player;
import xyz.refinedev.command.annotation.Command;
import xyz.refinedev.command.annotation.Sender;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 23/06/2025
 * Project: Practice
 */
public class ProfileSettingsCommand implements PotPvPCommand {

    @Command(name = "", desc = "Open profile menu", aliases = {"psettings"})
    public void execute(@Sender Player player) {
        new ProfileSettingsMenu().openMenu(player);
    }

    @Override
    public String getCommandName() {
        return "profilesettings";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }
}
