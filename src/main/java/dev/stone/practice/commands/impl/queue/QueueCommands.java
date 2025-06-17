package dev.stone.practice.commands.impl.queue;

import dev.stone.practice.commands.PotPvPCommand;
import dev.stone.practice.config.Lenguaje;
import dev.stone.practice.party.Party;
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.ProfileState;
import dev.stone.practice.queue.Queue;
import dev.stone.practice.queue.QueueProfile;
import dev.stone.practice.queue.QueueType;
import dev.stone.practice.queue.menu.QueueMenu;
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
 * Created: 17/06/2025
 * Project: Practice
 */
public class QueueCommands implements PotPvPCommand {

    @Command(name = "leave", desc = "Leave from kit queue")
    @Require("phantom.queue.leave")
    public void OnLeave(@Sender Player player) {
        QueueProfile qProfile = Queue.getPlayers().get(player.getUniqueId());
        if (qProfile == null) {
            player.sendMessage(CC.translate(Lenguaje.QUEUE_MESSAGES.CANNOT_QUIT_QUEUE));
            return;
        }
        Queue.leaveQueue(player);
        return;
    }

    @Command(name = "", desc = "Leave from kit queue")
    @Require("phantom.queue.join")
    public void OnJoin(@Sender Player player, String queue) {
        PlayerProfile profile = PlayerProfile.get(player);
        if(profile.getState() != ProfileState.LOBBY) {
            player.sendMessage(CC.translate(Lenguaje.QUEUE_MESSAGES.CANNOT_QUIT_QUEUE));
            return;
        }
        if(Party.getByPlayer(player) != null) {
            player.sendMessage(CC.translate(Lenguaje.PARTY_MESSAGES.IN_A_PARTY));
            return;
        }
        QueueType queueType = QueueType.valueOf(queue.toUpperCase());
        new QueueMenu(queueType).openMenu(player);
    }



    @Override
    public String getCommandName() {
        return "queue";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }
}
