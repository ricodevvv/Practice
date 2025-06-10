package net.frozenorb.potpvp.kit.menu.buttons.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.frozenorb.potpvp.PotPvPRP;
import net.frozenorb.potpvp.kit.Kit;
import net.frozenorb.potpvp.kit.KitGameRules;
import net.frozenorb.potpvp.kit.menu.KitDetailsMenu;
import net.frozenorb.potpvp.util.CC;
import net.frozenorb.potpvp.util.menu.button.ToggleButton;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.lang.reflect.Field;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 09/06/2025
 * Project: PotPvpReprised
 */
@RequiredArgsConstructor
public class KitRulesToggleButton extends ToggleButton {

    private final KitDetailsMenu menu;
    private final Kit kit;
    private final Field field;

    @Override
    public String getOptionName() {
        return KitGameRules.Readable.valueOf(field.getName()).getRule();
    }

    @Override
    public String getDescription() {
        return KitGameRules.Readable.valueOf(field.getName()).getDescription();
    }

    @SneakyThrows
    @Override
    public boolean isEnabled(Player player) {
        return field.getBoolean(kit.getGameRules());
    }

    @SneakyThrows
    @Override
    public void onClick(Player player, int slot, ClickType clickType, int hotbarSlot) {
        field.setBoolean(kit.getGameRules(), !isEnabled(player));

        player.sendMessage(CC.GREEN + "Value updated correctly for " + kit.getName() + " Value" + (isEnabled(player) ? CC.GREEN + "Enabled" : CC.RED + "Disabled"));
        PotPvPRP.getInstance().getKitHandler().saveKits();
        menu.openMenu(player);
    }
}
