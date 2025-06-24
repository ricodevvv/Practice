package dev.stone.practice.profile.cosmetic.killmessages.menu;

import java.util.HashMap;
import java.util.Map;

import dev.stone.practice.profile.cosmetic.killmessages.KillMessages;
import dev.stone.practice.util.Constants;
import dev.stone.practice.util.menu.Button;
import dev.stone.practice.util.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 23/06/2025
 * Project: Practice
 */
public class KillMessagesMenu extends Menu {

    public KillMessagesMenu() {
        this.setAutoUpdate(true);
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @NotNull
    public String getTitle(@NotNull Player player) {
        return "&8Kill Messages";
    }

    @NotNull
    public Map<Integer, Button> getButtons(@NotNull Player player) {
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        int y = 1;
        int x = 1;
        for (KillMessages messages : KillMessages.values()) {
            if (messages.hasPermission(player)) {
                buttons.put(this.getSlot(x++, y), new KillMessagesButton(messages));
            }
            if (x != 8) continue;
            ++y;
            x = 1;
        }
        for (int i = 0; i < 27; ++i) {
            buttons.putIfAbsent(i, Constants.BLACK_PANE);
        }
        return buttons;
    }

    public int size(Map<Integer, Button> buttons) {
        return 27;
    }
}
