package dev.stone.practice.commands.impl.misc;

import dev.stone.practice.Phantom;
import dev.stone.practice.commands.PotPvPCommand;
import dev.stone.practice.config.Config;
import dev.stone.practice.util.CC;
import dev.stone.practice.util.LocationSerialization;
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
        Phantom.getInstance().getLanguage().save();
        Phantom.getInstance().getLanguage().load();
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
