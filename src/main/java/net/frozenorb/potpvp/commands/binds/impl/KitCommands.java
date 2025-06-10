package net.frozenorb.potpvp.commands.binds.impl;

import net.frozenorb.potpvp.PotPvPRP;
import net.frozenorb.potpvp.commands.PotPvPCommand;
import net.frozenorb.potpvp.kit.Kit;
import net.frozenorb.potpvp.kit.KitHandler;

import net.frozenorb.potpvp.kit.menu.KitsManagementMenu;
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
 * Created: 09/06/2025
 * Project: PotPvpReprised
 */
public class KitCommands implements PotPvPCommand {

    @Command(name = "create", desc = "Create kit", usage = "/kit create <name>")
    @Require("phantom.admin.kit")
    public void onKitCreate(@Sender Player player, String kitname) {
        KitHandler handler = PotPvPRP.getInstance().getKitHandler();
        Kit kit = KitHandler.getByName(kitname);
        if (kit != null) {
            player.sendMessage(CC.RED + " The kit with that name already exists.");
            return;
        }

        Kit created = new Kit(kitname);
        handler.saveKits();
    }

    @Command(name = "manage", desc = "Manage the kits", usage = "/kit manage")
    @Require("phantom.admin.kit")
    public void OnKitManage(@Sender Player player) {
        new KitsManagementMenu().openMenu(player);
    }


    @Override
    public String getCommandName() {
        return "kit";
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }
}
