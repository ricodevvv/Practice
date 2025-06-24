package dev.stone.practice.commands.impl.match;

import dev.stone.practice.match.Match;
import dev.stone.practice.match.menu.ViewInventoryMenu;
import dev.stone.practice.util.CC;
import org.bukkit.entity.Player;
import xyz.refinedev.command.annotation.Command;
import xyz.refinedev.command.annotation.Sender;

import java.util.UUID;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 23/06/2025
 * Project: Practice
 */
public class ViewInventoryCommand {


    @Command(name = "", desc = "View player inventory")
    public void OpenInviMenu(@Sender Player player, String ID) {
        UUID uuid = UUID.fromString(ID);
        if (!Match.getPostMatchInventories().containsKey(uuid)) {
            player.sendMessage(CC.RED + "The inventory could not be found.");
            return;
        }
        new ViewInventoryMenu(Match.getPostMatchInventories().get(uuid)).openMenu(player);
    }

}
