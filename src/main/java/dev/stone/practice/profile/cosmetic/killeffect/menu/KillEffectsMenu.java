package dev.stone.practice.profile.cosmetic.killeffect.menu;

import java.util.HashMap;
import java.util.Map;

import dev.stone.practice.profile.cosmetic.killeffect.SpecialEffects;
import dev.stone.practice.util.Constants;
import dev.stone.practice.util.menu.Button;
import dev.stone.practice.util.menu.Menu;
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
public class KillEffectsMenu extends Menu {

    public KillEffectsMenu() {

        this.setAutoUpdate(true);
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @NotNull
    public String getTitle(@NotNull Player player) {
        return "&8Kill Effects";
    }

    @NotNull
    public Map<Integer, Button> getButtons(@NotNull Player player) {
        HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
        int y = 1;
        int x = 1;
        for (SpecialEffects effects : SpecialEffects.values()) {
            if (effects.hasPermission(player)) {
                buttons.put(this.getSlot(x++, y), new KillEffectButton(effects));
            }
            if (x != 8) continue;
            ++y;
            x = 1;
        }
        for (int i = 0; i < 36; ++i) {
            buttons.putIfAbsent(i, Constants.BLACK_PANE);
        }
        return buttons;
    }

    public int size(Map<Integer, Button> buttons) {
        return 36;
    }
}
