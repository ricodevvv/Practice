package dev.stone.practice.kit.menu;

import lombok.RequiredArgsConstructor;
import dev.stone.practice.config.Config;
import dev.stone.practice.kit.Kit;
import dev.stone.practice.kit.menu.buttons.impl.KitRulesSetIntegerButton;
import dev.stone.practice.kit.menu.buttons.impl.KitRulesToggleButton;
import dev.stone.practice.util.menu.Button;
import dev.stone.practice.util.menu.Menu;
import dev.stone.practice.util.menu.pagination.PaginatedMenu;
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
        return Config.KIT_MENU.MANAGEMENT_MENU_TITLE;
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
    public int getSize() {
        return Config.KIT_MENU.KIT_DETAILS_MENU_SIZE;
    }
}
