package dev.stone.practice.party.command;

import dev.stone.practice.commands.PotPvPCommand;
import dev.stone.practice.party.Party;
import dev.stone.practice.profile.Profile;
import dev.stone.practice.profile.ProfileState;
import org.bukkit.entity.Player;
import xyz.refinedev.command.annotation.Command;
import xyz.refinedev.command.annotation.Sender;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 28/06/2025
 * Project: Practice
 */
public class PartyCommands implements PotPvPCommand {


    @Command(name = "create", desc = "Create party for play with your friends")
    public void CreateParty(@Sender Player player) {
        if(Party.getByPlayer(player) != null) {
            return;
        }
        Profile profile = Profile.get(player);

        if(profile.getState() != ProfileState.LOBBY) {
            return;
        }
        Party party = new Party(player, 30);
    }

    @Command(name = "otherparties", desc = "")
    public void OtherPartyCommand(@Sender Player player) {
        Profile profile = Profile.get(player);

        if(profile.getState() != ProfileState.LOBBY) {
            return;
        }
        Party party = Party.getByPlayer(player);
        if (party == null) {
          //  Language.PARTY_NOT_IN_A_PARTY.sendMessage(player);
            return;
        }
        if (!party.getLeader().getPlayer().getUniqueId().equals(player.getUniqueId())) {
          //  Language.PARTY_ONLY_LEADER.sendMessage(player);
        }

      //  new OtherPartiesMenu().openMenu(player);
    }
    @Override
    public String getCommandName() {
        return "party";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }
}
