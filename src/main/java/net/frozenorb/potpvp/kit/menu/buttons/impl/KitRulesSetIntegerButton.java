package net.frozenorb.potpvp.kit.menu.buttons.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.frozenorb.potpvp.kit.Kit;
import net.frozenorb.potpvp.kit.KitGameRules;
import net.frozenorb.potpvp.kit.menu.KitDetailsMenu;
import net.frozenorb.potpvp.util.CC;
import net.frozenorb.potpvp.util.ItemBuilder;
import net.frozenorb.potpvp.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
public class KitRulesSetIntegerButton extends Button {

    private final KitDetailsMenu menu;
    private final Kit kit;
    private final Field field;


    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.DOUBLE_PLANT)
                .durability(0)
                .name(CC.GREEN + "Click to set " + getName())
                .build();
    }

    public String getName() {
        return KitGameRules.Readable.valueOf(field.getName()).getRule();
    }

    @SneakyThrows
    public int getValue(Player player) {
        return field.getInt(kit.getGameRules());
    }
}
