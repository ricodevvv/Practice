package dev.stone.practice.commands.impl;

import dev.stone.practice.Phantom;
import dev.stone.practice.commands.PotPvPCommand;
import dev.stone.practice.kit.Kit;
import dev.stone.practice.kit.KitHandler;

import dev.stone.practice.kit.menu.KitsManagementMenu;
import dev.stone.practice.util.CC;
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

    @Command(name = "", desc = "Help message for kits")
    @Require("phantom.admin.kit")
    public void help(@Sender Player player) {
        player.sendMessage(CC.translate(dev.stone.practice.config.Lenguaje.HELP.SEPARATOR));
        player.sendMessage(CC.translate(dev.stone.practice.config.Lenguaje.HELP.HEADER.replace("<name>", "Kit")));
        dev.stone.practice.config.Lenguaje.HELP.KIT.forEach(line -> player.sendMessage(CC.translate(line)));
        player.sendMessage(CC.translate(dev.stone.practice.config.Lenguaje.HELP.SEPARATOR));
    }

    @Command(name = "create", desc = "Create kit", usage = "/kit create <name>")
    @Require("phantom.admin.kit")
    public void onKitCreate(@Sender Player player, String kitname) {
        KitHandler handler = Phantom.getInstance().getKitHandler();
        Kit kit = KitHandler.getByName(kitname);
        if (kit != null) {
            player.sendMessage(CC.RED + " The kit with that name already exists.");
            return;
        }

        new Kit(kitname);
        player.sendMessage(CC.GREEN + " Kit created: " + kitname);
        handler.saveKits();
    }

    @Command(name = "manage", desc = "Manage the kits", usage = "/kit manage")
    @Require("phantom.admin.kit")
    public void OnKitManage(@Sender Player player) {
        new KitsManagementMenu().openMenu(player);
    }

    @Command(name = "debugdev", desc = "Set", usage = "/kit debugdev <kitname>")
    @Require("phantom.admin.kit")
    public void DebugCommandExecute(@Sender Player player, String kitName) {
       Kit kit = KitHandler.getByName(kitName);
       if(kit == null) return;
       kit.getKitLoadout().setContents(player.getInventory().getContents());
       kit.getKitLoadout().setArmor(player.getInventory().getArmorContents());
       Phantom.getInstance().getKitHandler().saveKits();
      player.sendMessage(CC.GREEN + "Kit contents set");
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
