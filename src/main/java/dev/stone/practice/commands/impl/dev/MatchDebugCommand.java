package dev.stone.practice.commands.impl.dev;

import dev.stone.practice.Phantom;
import dev.stone.practice.arena.Arena;
import dev.stone.practice.commands.PotPvPCommand;
import dev.stone.practice.kit.Kit;
import dev.stone.practice.kit.KitHandler;
import dev.stone.practice.match.Match;
import dev.stone.practice.match.impl.SoloMatch;
import dev.stone.practice.match.team.Team;
import dev.stone.practice.match.team.TeamPlayer;
import dev.stone.practice.queue.QueueType;
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
 * Created: 11/06/2025
 * Project: Practice
 */
public class MatchDebugCommand implements PotPvPCommand {

    @Command(name = "", desc = "Developer debug command for match test")
    @Require("phantom.debug.command")
public void DebugCommandExecute(@Sender Player player, Player target) {

        Kit kit = KitHandler.getByName("wasa");
        Arena arena = Phantom.getInstance().getArenaHandler().getRandomArena(kit);
        Team teamA = new Team(new TeamPlayer(player));
        Team teamB = new Team(new TeamPlayer(target));
        if(arena == null) {
            player.sendMessage(CC.RED + "Arena not found");
            return;
        }
        Match match = new SoloMatch(arena, kit, teamA, teamB, QueueType.UNRANKED, false);
        match.start();
    }

    @Override
    public String getCommandName() {
        return "debug";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }
}
