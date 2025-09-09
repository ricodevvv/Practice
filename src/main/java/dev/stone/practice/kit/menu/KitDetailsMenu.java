package dev.stone.practice.kit.menu;

import dev.stone.practice.config.Lenguaje;
import dev.stone.practice.kit.menu.buttons.impl.KitEditMatchTypeButton;
import dev.stone.practice.util.menu.button.BackButton;
import dev.stone.practice.util.menu.pagination.PageButton;
import lombok.RequiredArgsConstructor;
import dev.stone.practice.config.Config;
import dev.stone.practice.kit.Kit;
import dev.stone.practice.kit.menu.buttons.impl.KitRulesSetIntegerButton;
import dev.stone.practice.kit.menu.buttons.impl.KitRulesToggleButton;
import dev.stone.practice.util.menu.Button;
import dev.stone.practice.util.menu.Menu;
import dev.stone.practice.util.menu.pagination.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
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

@RequiredArgsConstructor
public class KitDetailsMenu extends PaginatedMenu {

    private final Kit kit;
    private final Menu backMenu;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return Lenguaje.KIT_MENU.MANAGEMENT_MENU_TITLE;
    }


    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        int minIndex = (int) ((double) (page - 1) * getMaxItemsPerPage(player));
        int maxIndex = (int) ((double) (page) * getMaxItemsPerPage(player));
        int topIndex = 0;

        for (Map.Entry<Integer, Button> entry : getAllPagesButtons(player).entrySet()) {
            int index = entry.getKey();

            if (index >= minIndex && index < maxIndex) {
                index -= (int) ((double) (getMaxItemsPerPage(player)) * (page - 1)) - 27;
                buttons.put(index, entry.getValue());

                if (index > topIndex) {
                    topIndex = index;
                }
            }
        }

        buttons.put(18, new PageButton(-1, this));
        buttons.put(26, new PageButton(1, this));

        Map<Integer, Button> global = getGlobalButtons(player);
        if (global != null) {
            buttons.putAll(global);
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        if (backMenu != null) {
            buttons.put(0, new BackButton(Material.STAINED_GLASS_PANE, 14, backMenu));
        }
        buttons.put(3, new KitEditMatchTypeButton(kit, this));
        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        for (Field field : kit.getGameRules().getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getType() == boolean.class) {
                buttons.put(buttons.size(), new KitRulesToggleButton(this, kit, field));
            } else if (field.getType() == int.class) {
                buttons.put(buttons.size(), new KitRulesSetIntegerButton(this, kit, field));
            } /*else {
                buttons.put(buttons.size(), new KitRulesSetStringButton(this, kit, field));
            } */
        }

        return buttons;
    }

    @Override
    public int getMaxItemsPerPage(Player player) {
        return 27;
    }

    @Override
    public int getSize() {
        return 54;
    }
}
