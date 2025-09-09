package dev.stone.practice.queue.menu;

import dev.stone.practice.Phantom;
import dev.stone.practice.config.Lenguaje;
import dev.stone.practice.kit.Kit;
import dev.stone.practice.kit.KitHandler;
import dev.stone.practice.queue.Queue;
import dev.stone.practice.queue.QueueType;
import dev.stone.practice.util.ItemBuilder;
import dev.stone.practice.util.menu.Button;
import dev.stone.practice.util.menu.Menu;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 17/06/2025
 * Project: Practice
 */
@RequiredArgsConstructor
public class QueueMenu extends Menu {

    private final QueueType queueType;

    @Override
    public String getTitle(Player player) {
        return Lenguaje.QUEUE_MESSAGES.MENU_TITLE.replace("<type>", queueType.getReadable());
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();
        KitHandler.getKits().stream()
                .filter(Kit::isEnabled)
                .filter(kit -> queueType == QueueType.UNRANKED || kit.isRanked())
                .forEach(kit -> buttons.put(buttons.size(), new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return new ItemBuilder(kit.getDisplayIcon().clone())
                                .name("&b" + kit.getDisplayName())
                                .lore(kit.getDescription())
                                .lore(Lenguaje.QUEUE_MESSAGES.MENU_BUTTON_LORE.stream()
                                        .map(s -> s
                                                .replace("<in-queue>", String.valueOf(
                                                        Queue.getPlayers().values().stream()
                                                                .filter(profile -> profile.getKit() == kit && profile.getQueueType() == queueType)
                                                                .count()
                                                ))
                                                .replace("<in-game>", String.valueOf(
                                                        Phantom.getInstance().getMatchHandler().getMatches().values().stream()
                                                                .filter(match -> match.getKit() == kit && match.getQueueType() == queueType)
                                                                .mapToInt(match -> match.getMatchPlayers().size())
                                                                .sum()
                                                ))
                                                .replace("<kit>", kit.getDisplayName())
                                        )
                                        .collect(Collectors.toList()))
                                .lore("")
                                .lore("&aClick to queue")
                                .build();
                    }

                    @Override
                    public void clicked(Player player, ClickType clickType) {
                        player.closeInventory();
                        Queue.joinQueue(player, kit, queueType);
                    }
                }));
        return buttons;
    }
}
