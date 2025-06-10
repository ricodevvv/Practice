package net.frozenorb.potpvp.commands.binds.impl;


import net.frozenorb.potpvp.PotPvPRP;
import net.frozenorb.potpvp.arena.Arena;
import net.frozenorb.potpvp.arena.ArenaGrid;
import net.frozenorb.potpvp.arena.ArenaHandler;
import net.frozenorb.potpvp.arena.ArenaSchematic;
import net.frozenorb.potpvp.commands.PotPvPCommand;
import net.frozenorb.potpvp.util.CC;
import net.frozenorb.potpvp.util.LocationUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.refinedev.command.annotation.Command;
import xyz.refinedev.command.annotation.Require;
import xyz.refinedev.command.annotation.Sender;

import java.io.File;

/**
 * This Project is property of Refine Development © 2021 - 2022
 * Redistribution of this Project is not allowed
 *
 * @author Drizzy
 * Created: 4/5/2022
 * Project: potpvp-reprised
 */
public class ArenaCommands implements PotPvPCommand {

  /*  private static final String[] HELP_MESSAGE = {
            ChatColor.DARK_PURPLE + PotPvPLang.LONG_LINE,
            "§5§lArena Commands",
            ChatColor.DARK_PURPLE + PotPvPLang.LONG_LINE,
            "§c " + PotPvPLang.LEFT_ARROW_NAKED + " §a/arena free",
            "§c " + PotPvPLang.LEFT_ARROW_NAKED + " §a/arena createSchematic <schematic>",
            "§c " + PotPvPLang.LEFT_ARROW_NAKED + " §a/arena listArenas <schematic>",
            "§c " + PotPvPLang.LEFT_ARROW_NAKED + " §a/arena repasteSchematic <schematic>",
            "§c " + PotPvPLang.LEFT_ARROW_NAKED + " §a/arena rescaleall <schematic>",
            "§c " + PotPvPLang.LEFT_ARROW_NAKED + " §a/arena listSchematics",
            ChatColor.DARK_PURPLE + PotPvPLang.LONG_LINE,

    }; */

    @Command(name = "", desc = "Help message for arenas")
    @Require("potpvp.arena.admin")
    public void help(@Sender Player sender) {
        sender.sendMessage("Change this");
    }

    @Command(name = "free", desc = "Free all arenas")
    @Require("potpvp.arena.admin")
    public void arenaFree(@Sender Player sender) {
        PotPvPRP.getInstance().getArenaHandler().getGrid().free();
        sender.sendMessage(ChatColor.GREEN + "Arena grid has been freed.");
    }

    @Command(name = "createSchematic", usage = "<schematic>", desc = "Create and load a schematic from world edit as an arena")
    @Require("potpvp.arena.admin")
    public void arenaCreateSchematic(@Sender Player sender, String schematicName) {
        ArenaHandler arenaHandler = PotPvPRP.getInstance().getArenaHandler();

        if (arenaHandler.getSchematic(schematicName) != null) {
            sender.sendMessage(ChatColor.RED + "Schematic " + schematicName + " already exists");
            return;
        }

        ArenaSchematic schematic = new ArenaSchematic(schematicName);
        File schemFile = schematic.getSchematicFile();

        if (!schemFile.exists()) {
            sender.sendMessage(ChatColor.RED + "No file for " + schematicName + " found. (" + schemFile.getPath() + ")");
            return;
        }

        arenaHandler.registerSchematic(schematic);

        try {
            schematic.pasteModelArena();
            arenaHandler.saveSchematics();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        sender.sendMessage(ChatColor.GREEN + "Schematic created.");
    }

    @Command(name = "setdisplayname", usage = "<schematic> <DisplayName>", desc = "Set display name for one arena")
    @Require("phantom.arena.admin")
    public void onSetCustomName(@Sender Player sender, String schematicName) {
        ArenaHandler arenaHandler = PotPvPRP.getInstance().getArenaHandler();

        ArenaSchematic schematic = arenaHandler.getSchematic(schematicName);
        if (schematic == null) {
            sender.sendMessage(ChatColor.RED + "Schematic with the name" + schematicName + " it does not exist");
            return;
        }
        schematic.setDisplayName(schematicName);
        arenaHandler.saveSchematics();
    }


    @Command(name = "scale", usage = "<schematic> <amount", desc = "Scale any arena ")
    @Require("phantom.arena.admin")
    public void onRescale(@Sender Player player, String schematicName, int a) {
        ArenaHandler arenaHandler = PotPvPRP.getInstance().getArenaHandler();

        ArenaSchematic schematic = arenaHandler.getSchematic(schematicName);
        if (schematic == null) {
            player.sendMessage(ChatColor.RED + "Schematic with the name" + schematicName + " it does not exist");
            return;
        }
        int existing = arenaHandler.countArenas(schematic);
        int create = 1;
        int desired = existing + create;

        if (arenaHandler.getGrid().isBusy()) {
            player.sendMessage(ChatColor.DARK_RED + "✖ " + ChatColor.RED + "Grid is busy.");
            return;
        }

        try {
            player.sendMessage(ChatColor.GREEN + "Starting...");

            arenaHandler.getGrid().scaleCopies(schematic, desired, () -> {
                player.sendMessage(ChatColor.GREEN + "Scaled " + schematic.getName() + " to " + desired + ".");
            });
        } catch (Exception ex) {
            player.sendMessage(ChatColor.DARK_RED + "✖ " + ChatColor.RED + "Failed to paste " + schematic.getName() + ": " + ex.getMessage());
            ex.printStackTrace();
        }

        arenaHandler.saveSchematics();
    }

    @Command(name = "addkit", usage = "<schematic> <kitname>", desc = "Add kit to arena ")
    @Require("phantom.arena.admin")
    public void addKit(@Sender Player player, String schematicName, String kitname) {

        //TODO ADD VERIFICATION TO CHECK IF THE KIT EXISTS
        ArenaHandler arenaHandler = PotPvPRP.getInstance().getArenaHandler();

        ArenaSchematic schematic = arenaHandler.getSchematic(schematicName);
        if (schematic == null) {
            player.sendMessage(ChatColor.RED + "Schematic with the name" + schematicName + " it does not exist");
            return;
        }
        schematic.getKits().add(kitname);
        player.sendMessage(CC.GREEN + "Kit added successfully");
        arenaHandler.saveSchematics();
    }


    @Command(name = "listArenas", usage = "<schematic>", desc = "List all arenas")
    @Require("potpvp.arena.admin")
    public void arenaListArenas(@Sender Player sender, String schematicName) {
        ArenaHandler arenaHandler = PotPvPRP.getInstance().getArenaHandler();
        ArenaSchematic schematic = arenaHandler.getSchematic(schematicName);

        if (schematic == null) {
            sender.sendMessage(ChatColor.RED + "Schematic " + schematicName + " not found.");
            sender.sendMessage(ChatColor.RED + "List all schematics with /arena listSchematics");
            return;
        }

        sender.sendMessage(ChatColor.RED + "------ " + ChatColor.WHITE + schematic.getName() + " Arenas" + ChatColor.RED + " ------");

        for (Arena arena : arenaHandler.getArenas(schematic)) {
            String locationStr = LocationUtils.locToStr(arena.getSpectatorSpawn());
            String occupiedStr = arena.isInUse() ? ChatColor.RED + "In Use" : ChatColor.GREEN + "Open";

            sender.sendMessage(arena.getCopy() + ": " + ChatColor.GREEN + locationStr + ChatColor.GRAY + " - " + occupiedStr);
        }
    }

    @Command(name = "repasteSchematic", usage = "<schematic>", desc = "Repaste a schematic's arenas")
    @Require("potpvp.arena.admin")
    public void arenaRepasteSchematic(@Sender Player sender, String schematicName) {
        ArenaHandler arenaHandler = PotPvPRP.getInstance().getArenaHandler();
        ArenaSchematic schematic = arenaHandler.getSchematic(schematicName);

        if (schematic == null) {
            sender.sendMessage(ChatColor.RED + "Schematic " + schematicName + " not found.");
            sender.sendMessage(ChatColor.RED + "List all schematics with /arena listSchematics");
            return;
        }

        int currentCopies = arenaHandler.countArenas(schematic);

        if (currentCopies == 0) {
            sender.sendMessage(ChatColor.RED + "No copies of " + schematic.getName() + " exist.");
            return;
        }

        ArenaGrid arenaGrid = arenaHandler.getGrid();

        sender.sendMessage(ChatColor.GREEN + "Starting...");

        arenaGrid.scaleCopies(schematic, 0, () -> {
            sender.sendMessage(ChatColor.GREEN + "Removed old maps, creating new copies...");

            arenaGrid.scaleCopies(schematic, currentCopies, () -> {
                sender.sendMessage(ChatColor.GREEN + "Repasted " + currentCopies + " arenas using the newest " + schematic.getName() + " schematic.");
            });
        });
    }

    @Command(name = "scale", usage = "<schematic> <count>", desc = "Scale schematics to a specific size")
    @Require("potpvp.arena.admin")
    public void arenaScale(@Sender Player sender, String schematicName, int count) {
        ArenaHandler arenaHandler = PotPvPRP.getInstance().getArenaHandler();
        ArenaSchematic schematic = arenaHandler.getSchematic(schematicName);

        if (schematic == null) {
            sender.sendMessage(ChatColor.RED + "Schematic " + schematicName + " not found.");
            sender.sendMessage(ChatColor.RED + "List all schematics with /arena listSchematics");
            return;
        }

        sender.sendMessage(ChatColor.GREEN + "Starting...");

        arenaHandler.getGrid().scaleCopies(schematic, count, () -> {
            sender.sendMessage(ChatColor.GREEN + "Scaled " + schematic.getName() + " to " + count + " copies.");
        });
    }

    @Command(name = "rescaleall", desc = "Rescale all schematics and their arenas")
    @Require("potpvp.arena.admin")
    public void arenaRescaleAll(@Sender Player sender) {
        PotPvPRP.getInstance().getArenaHandler().getSchematics().forEach(schematic -> {
            ArenaHandler arenaHandler = PotPvPRP.getInstance().getArenaHandler();
            int totalCopies = arenaHandler.getArenas(schematic).size();

            arenaScale(sender, schematic.getName(), 0);
            arenaScale(sender, schematic.getName(), totalCopies);
        });
    }

    @Command(name = "listSchematics", aliases = "listSchems", desc = "List all potpvp schematics")
    @Require("potpvp.arena.admin")
    public void arenaListSchems(@Sender Player sender) {
        ArenaHandler arenaHandler = PotPvPRP.getInstance().getArenaHandler();
        //  sender.sendMessage(ChatColor.DARK_PURPLE + PotPvPLang.LONG_LINE);
        sender.sendMessage(CC.translate("&5&lPotPvP Schematics"));
        // sender.sendMessage(ChatColor.DARK_PURPLE + PotPvPLang.LONG_LINE);
        arenaHandler.getSchematics().forEach(schematic -> {
            int size = arenaHandler.getArenas(schematic).size();
            sender.sendMessage(CC.translate("&c" + schematic.getName() + " &7| &cArenas using: &f" + size));
        });
        // sender.sendMessage(ChatColor.DARK_PURPLE + PotPvPLang.LONG_LINE);
    }

    @Override
    public String getCommandName() {
        return "arena";
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }
}
