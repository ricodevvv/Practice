package dev.stone.practice.queue;

import dev.stone.practice.Phantom;
import dev.stone.practice.config.Config;
import dev.stone.practice.config.Lenguaje;
import dev.stone.practice.kit.Kit;
import dev.stone.practice.party.Party;
import dev.stone.practice.profile.Profile;
import dev.stone.practice.profile.ProfileState;
import dev.stone.practice.profile.data.ProfileKitData;
import dev.stone.practice.queue.task.QueueTask;
import dev.stone.practice.util.CC;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 17/06/2025
 * Project: Practice
 */
public class Queue {


    @Getter
    private static final Map<UUID, QueueProfile> players = new HashMap<>();

    public static void init() {
        new QueueTask();
    }

    public static List<QueueProfile> getUnmatchedPlayers() {
        return players.values().stream().filter(qProfile -> !qProfile.isFound()).collect(Collectors.toList());
    }

    public static void joinQueue(Player player, Kit kit, QueueType queueType) {
        if (player == null || !player.isOnline()) {
            return;
        }
        if (players.get(player.getUniqueId()) != null) {
            player.sendMessage(CC.translate(Lenguaje.QUEUE_MESSAGES.ERROR_FOUND_QUEUE_PROFILE));
            return;
        }
        if (Party.getByPlayer(player) != null) {
            player.sendMessage(CC.translate(Lenguaje.PARTY_MESSAGES.IN_A_PARTY));
            return;
        }

        Profile profile = Profile.get(player);
        if (profile.getState() != ProfileState.LOBBY) {
            player.sendMessage(CC.translate(Lenguaje.QUEUE_MESSAGES.WRONG_STATE));
            return;
        }

      /*  if (profile.getKitData().get(kit.getName()) == null) {
            Language.QUEUE_ERROR_KIT_DATA_NOT_FOUND.sendMessage(player);
            return;
        } */

        if (queueType == QueueType.RANKED) {
            int required = Config.RANKED_WINS_REQUIRED;
            int wins = profile.getKitData().values().stream().mapToInt(ProfileKitData::getUnrankedWon).sum();
            if (wins < required) {
                String formatted = Lenguaje.QUEUE_MESSAGES.ERROR_NOT_ENOUGH_WINS.replace("<require>", String.valueOf(required).replace("<wins>", String.valueOf(wins)));
                player.sendMessage(CC.translate(formatted));
                return;
            }
        }

        QueueProfile qProfile = new QueueProfile(player.getUniqueId(), kit, profile.getKitData().get(kit).getElo(), queueType);
        players.put(player.getUniqueId(), qProfile);
        profile.setPlayerState(ProfileState.QUEUEING);
        profile.setupItems();
        player.sendMessage(CC.translate( Lenguaje.QUEUE_MESSAGES.SUCCESS_JOIN.replace("<kit>", kit.getDisplayName())));
    }

    public static void leaveQueue(Player player) {
        Profile profile = Profile.get(player);
        QueueProfile qProfile = players.get(player.getUniqueId());

        if (profile.getState() != ProfileState.QUEUEING) {
            player.sendMessage(Lenguaje.QUEUE_MESSAGES.CANNOT_QUIT_QUEUE);
            return;
        }
        if (qProfile == null) {
            player.sendMessage(CC.translate(Lenguaje.QUEUE_MESSAGES.ERROR_FOUND_QUEUE_PROFILE));
            return;
        }

        players.remove(player.getUniqueId());

        Phantom.getInstance().getLobbyManager().reset(player);

        player.sendMessage(CC.translate(Lenguaje.QUEUE_MESSAGES.SUCCESS_QUIT.replace("<kit>", qProfile.getKit().getDisplayName())));
    }


}
