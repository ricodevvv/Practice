package dev.stone.practice.party;

import dev.stone.practice.profile.Profile;
import dev.stone.practice.util.Common;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@Getter
public class PartyMember {

    private final UUID uniqueID;
    private final String username;
    private boolean partyChat = false;

    public PartyMember(Player player) {
        this.uniqueID = player.getUniqueId();
        this.username = player.getName();
    }

    public Profile getProfile() {
        return Profile.get(getPlayer());
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uniqueID);
    }

    public void sendMessage(String... messages) {
        if (getPlayer() == null) {
            return;
        }
        Common.sendMessage(getPlayer(), messages);
    }

    public void sendMessage(List<String> messages) {
        if (getPlayer() == null) {
            return;
        }
        Common.sendMessage(getPlayer(), messages);
    }

    public void toggleChat() {
        this.partyChat = !this.partyChat;

      //  Language.PARTY_TOGGLE_PARTY_CHAT.sendMessage(getPlayer(), this.partyChat ? Language.ENABLED.toString() : Language.DISABLED.toString());
    }
}
