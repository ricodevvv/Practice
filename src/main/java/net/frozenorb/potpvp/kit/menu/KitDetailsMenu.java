package net.frozenorb.potpvp.kit.menu;

import lombok.RequiredArgsConstructor;
import net.frozenorb.potpvp.config.Config;
import net.frozenorb.potpvp.kit.Kit;
import net.frozenorb.potpvp.kit.menu.buttons.impl.KitRulesSetIntegerButton;
import net.frozenorb.potpvp.kit.menu.buttons.impl.KitRulesToggleButton;
import net.frozenorb.potpvp.util.menu.Button;
import net.frozenorb.potpvp.util.menu.Menu;
import net.frozenorb.potpvp.util.menu.pagination.PaginatedMenu;
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
