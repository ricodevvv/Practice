package dev.stone.practice.kit.menu;

import dev.stone.practice.config.Config;
import dev.stone.practice.kit.KitHandler;
import dev.stone.practice.util.ItemBuilder;
import dev.stone.practice.util.menu.Button;
import dev.stone.practice.util.menu.pagination.PaginatedMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 09/06/2025
 * Project: PotPvpReprised
 */
public class KitsManagementMenu extends PaginatedMenu {
    @Override
    public String getPrePaginatedTitle(Player player) {
        return Config.KIT_MENU.MANAGEMENT_MENU_TITLE;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        KitHandler.getKits().forEach(kit -> buttons.put(buttons.size(), new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(kit.getDisplayIcon().clone())
                        .name(Config.KIT_MENU.KIT_MANAGE_BUTTON.replace("<kit>", kit.getDisplayName()))
                        .build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
                new KitDetailsMenu(kit, KitsManagementMenu.this).openMenu(player);
            }
        }));
        return buttons;
    }
}
