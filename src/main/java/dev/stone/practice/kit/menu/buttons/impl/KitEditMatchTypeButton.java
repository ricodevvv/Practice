package dev.stone.practice.kit.menu.buttons.impl;

import dev.stone.practice.Phantom;
import dev.stone.practice.kit.Kit;
import dev.stone.practice.kit.KitMatchType;
import dev.stone.practice.kit.menu.buttons.KitButton;
import dev.stone.practice.util.CC;
import dev.stone.practice.util.ItemBuilder;
import dev.stone.practice.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 17/06/2025
 * Project: Practice
 */
public class KitEditMatchTypeButton extends KitButton {


    private final Menu menu;

    public KitEditMatchTypeButton(Kit kit, Menu menu) {
        super(kit);
        this.menu = menu;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.FLINT)
                .name(CC.GREEN + "Click to change")
                .lore(
                        "",
                        (kit.getKitMatchTypes().contains(KitMatchType.SOLO) ? CC.GREEN + " » " : CC.GRAY + "   ") + "Solo " + "Left click to set",
                        (kit.getKitMatchTypes().contains(KitMatchType.FFA) ? CC.GREEN + " » " : CC.GRAY + "   ") + "FFA " + "Click medium to set",
                        (kit.getKitMatchTypes().contains(KitMatchType.SPLIT) ? CC.GREEN + " » " : CC.GRAY + "   ") + "Split " + "Right click to set"
                )
                .lore(Arrays.asList(
                        "",
                        "&e&nClick to change match type"
                ))
                .build();
    }
    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        playNeutral(player);
        switch (clickType) {
            case LEFT:
                if (kit.getKitMatchTypes().contains(KitMatchType.SOLO)) {
                    kit.getKitMatchTypes().remove(KitMatchType.SOLO);
                } else {
                    kit.getKitMatchTypes().add(KitMatchType.SOLO);
                }
                break;
            case MIDDLE:
                if (kit.getKitMatchTypes().contains(KitMatchType.FFA)) {
                    kit.getKitMatchTypes().remove(KitMatchType.FFA);
                } else {
                    kit.getKitMatchTypes().add(KitMatchType.FFA);
                }
                break;
            case RIGHT:
                if (kit.getKitMatchTypes().contains(KitMatchType.SPLIT)) {
                    kit.getKitMatchTypes().remove(KitMatchType.SPLIT);
                } else {
                    kit.getKitMatchTypes().add(KitMatchType.SPLIT);
                }
                break;
            default:
                break;
        }
        Phantom.getInstance().getKitHandler().saveKits();
        menu.openMenu(player);
    }
}
