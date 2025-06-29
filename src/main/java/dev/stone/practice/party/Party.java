package dev.stone.practice.party;

import dev.stone.practice.profile.Profile;
import dev.stone.practice.profile.ProfileState;
import dev.stone.practice.util.Clickable;
import dev.stone.practice.util.Common;
import dev.stone.practice.util.VisibilityController;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;


import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Party {

    @Getter private static final Map<UUID, Party> parties = new HashMap<>();

    private final UUID uniqueID = UUID.randomUUID();
    private final long createdAt = System.currentTimeMillis();
    @Setter private PartyMember leader;
    private final List<PartyMember> partyMembers = new ArrayList<>();
    private final Map<UUID, PartyInvite> invites = new HashMap<>();
    private int maxSize;
    private boolean muted = false;
    private PartyPrivacy privacy = PartyPrivacy.CLOSED;

    @Setter private long lastAnnounced = 0;

    public static Party getByPlayer(Player player) {
        return Profile.get(player).getParty();
    }

    public Party(Player player, int size) {
        this.leader = new PartyMember(player);
        this.maxSize = size;
        parties.put(uniqueID, this);

        Profile profile = Profile.get(player);
        profile.setParty(this);
        profile.setupItems();

      //  Language.PARTY_CREATED.sendMessage(player);
    }

    public void setMaxSize(int size) {
        if (leader == null) {

            return;
        }

        maxSize = size;

    }

    public void toggleChatRoom() {
        if (leader == null) {

            return;
        }
        muted = !muted;


    }

    public void announce() {
        if (leader == null) {
            return;
        }

      //  Clickable clickable = new Clickable(Language.PARTY_ANNOUNCE_MESSAGE.toString(leader.getUsername()));
       // clickable.add(Language.PARTY_ANNOUNCE_CLICKABLE.toString(), Language.PARTY_ANNOUNCE_HOVER.toString(), "/party join " + leader.getUsername());

      //  Bukkit.getOnlinePlayers().forEach(clickable::sendToPlayer);

        lastAnnounced = System.currentTimeMillis();
    }

    public void setPrivacy(PartyPrivacy privacy) {
        this.privacy = privacy;

      //  broadcast(Language.PARTY_PRIVACY_MESSAGE.toString(privacy.getReadable()));
    }

    public void broadcast(String... message) {
        getAllPartyMembers().forEach(partyMember -> {
            if (partyMember == null) {
            }
         //   Arrays.stream(message).forEach(msg -> partyMember.sendMessage(Language.PARTY_BROADCAST_FORMAT.toString(msg)));
        });
    }

    public void teleport(Location location) {
        getAllPartyMembers().forEach(partyMember -> {
            if (partyMember == null) {
                return;
            }
        partyMember.getPlayer().teleport(location);
        });
    }

    public void join(Player player, boolean force) {
        Profile profile = Profile.get(player);
        if (profile == null) {
            return;
        }

      //  PartyJoinEvent event = new PartyJoinEvent(this, force);
       // event.call();

      //  if (event.isCancelled()) {
        //    Common.sendMessage(player, event.getCancelReason());
         //   return;
      //  }

        invites.remove(player.getUniqueId());
        partyMembers.add(new PartyMember(player));
        profile.setParty(this);
        profile.setupItems();

       // broadcast(force ? Language.PARTY_JOIN_MESSAGE_FORCE.toString(player.getName()) : Language.PARTY_JOIN_MESSAGE_NORMAL.toString(player.getName()));

        getAllPartyMembers().forEach(partyMember -> VisibilityController.updateVisibility(partyMember.getPlayer()));
    }

    public void leave(UUID targetUUID, boolean kick) {
        PartyMember member = getPartyMembers().stream().filter(partyMember -> partyMember.getUniqueID().equals(targetUUID)).findFirst().orElse(null);
        Player target = Bukkit.getPlayer(targetUUID);

        if (member == null) {
            if (target != null) {
              //  Language.PARTY_OWN_PROFILE_NOT_FOUND.sendMessage(target);
            }
            return;
        }

        Profile profile = Profile.get(target);
        if (profile == null) {
          //  Language.PARTY_OWN_PROFILE_NOT_FOUND.sendMessage(target);
            return;
        }

        partyMembers.removeIf(pm -> pm.getUniqueID().equals(member.getUniqueID()));
        profile.setParty(null);
        profile.setupItems();

      //  broadcast(kick ? Language.PARTY_LEAVE_MESSAGE_FORCE.toString(member.getUsername()) : Language.PARTY_LEAVE_MESSAGE_NORMAL.toString(member.getUsername()));

        VisibilityController.updateVisibility(target);
        getAllPartyMembers().forEach(partyMember -> VisibilityController.updateVisibility(partyMember.getPlayer()));
    }

    public void disband(boolean forced) {
        Party party = parties.remove(uniqueID);
     //   broadcast(Language.PARTY_DISBAND.toString());
        getAllPartyMembers().stream().map(partyMember -> Profile.getByUuid(partyMember.getUniqueID())).forEach(profile -> {
            profile.setParty(null);
            //Check if the player is in a match. This is to prevent player's inventory get reset when they are in match
            if (profile.getState() != ProfileState.FIGHTING && profile.getMatch() == null) {
                profile.setupItems();
            }
        });
        getAllPartyMembers().forEach(partyMember -> VisibilityController.updateVisibility(partyMember.getPlayer()));

      // PartyDisbandEvent event = new PartyDisbandEvent(party, forced);
      //  event.call();
    }

    public void invite(Player player) {
        PartyInvite invite = new PartyInvite(player);
        invites.put(player.getUniqueId(), invite);

      //  broadcast(Language.PARTY_INVITE_TEAM_MESSAGE.toString(leader.getUsername(), invite.getUsername()));

     //   Clickable clickable = new Clickable(Language.PARTY_INVITE_INVITE_MESSAGE.toString(leader.getUsername()));
       // clickable.add(Language.PARTY_INVITE_CLICKABLE.toString(), Language.PARTY_INVITE_HOVER.toString(), "/party join " + leader.getUsername());
      //  clickable.sendToPlayer(player);
    }

    public void sendInformation(Player player) {
      //  Language.PARTY_INFORMATION.sendListOfMessage(player, getLeader().getUsername(), getAllPartyMembers().size(), maxSize, getPartyMembers().stream().map(PartyMember::getUsername).collect(Collectors.joining(", ")), privacy.getReadable(), muted ? Language.DISABLED.toString() : Language.ENABLED.toString());
    }

    /**
     * Get all party members, including the leader
     */
    public List<PartyMember> getAllPartyMembers() {
        List<PartyMember> members = new ArrayList<>();
        members.add(leader);
        members.addAll(partyMembers);
        return members;
    }

    public PartyMember getMember(Player player) {
        return getAllPartyMembers().stream().filter(partyMember -> partyMember.getUniqueID().equals(player.getUniqueId())).findFirst().orElse(null);
    }

    public PartyMember getMember(UUID uuid) {
        return getAllPartyMembers().stream().filter(partyMember -> partyMember.getUniqueID().equals(uuid)).findFirst().orElse(null);
    }

    public PartyMember getMember(String username) { //Need to use real name, not disguised name
        return getAllPartyMembers().stream().filter(partyMember -> partyMember.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);
    }

    public boolean isFull() {
        return getAllPartyMembers().size() >= maxSize;
    }

    public boolean isAllPlayersInState(ProfileState... playerStates) {
        return !getAllPartyMembers().stream().map(partyMember -> Profile.getByUuid(partyMember.getUniqueID())).filter(Objects::nonNull).allMatch(practicePlayer -> Arrays.asList(playerStates).contains(practicePlayer.getState()));
    }

}
