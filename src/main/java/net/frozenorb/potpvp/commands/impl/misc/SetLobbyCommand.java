package net.frozenorb.potpvp.commands.impl.misc;

import net.frozenorb.potpvp.PotPvPRP;
import net.frozenorb.potpvp.commands.PotPvPCommand;
import net.frozenorb.potpvp.config.Config;
import net.frozenorb.potpvp.util.CC;
import net.frozenorb.potpvp.util.LocationSerialization;
import net.frozenorb.potpvp.util.serialization.LocationSerializer;
import org.bukkit.Location;
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

public class SetLobbyCommand implements PotPvPCommand {

    @Command(name = "", desc = "Set lobby location")
    @Require("phantom.lobby.set")
    public void SetLobbyCommandExecute(@Sender Player player) {
        Location location = player.getLocation();
        Config.LOBBY_LOCATION = LocationSerialization.serializeLocation(location);
        PotPvPRP.getInstance().getLanguage().save();
        player.sendMessage(CC.GREEN + "Lobby location set correctly");
    }

    @Override
    public String getCommandName() {
        return "setlobby";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }
}
