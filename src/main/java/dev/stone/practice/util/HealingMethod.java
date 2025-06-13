package dev.stone.practice.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 11/06/2025
 * Project: Practice
 */

@Getter
@AllArgsConstructor
public enum HealingMethod {
    POTION("[Potion]", new ItemBuilder(Material.POTION).durability(16421).build().clone()),
    SOUP("[Soup]", new ItemStack(Material.MUSHROOM_SOUP).clone()),
    GOLDEN_APPLE("[Golden Apple]", new ItemStack(Material.GOLDEN_APPLE).clone()),
    GOD_APPLE("[God Apple]", new ItemBuilder(Material.GOLDEN_APPLE).durability(1).build().clone());

    private final String name;
    private final ItemStack item;

    public static HealingMethod getHealingMethod(ItemStack[] contents) {
        for (ItemStack itemStack : contents) {
            if (itemStack == null) {
                continue;
            }
            if (itemStack.isSimilar(HealingMethod.POTION.getItem())) {
                return HealingMethod.POTION;
            } else if (itemStack.isSimilar(HealingMethod.SOUP.getItem())) {
                return HealingMethod.SOUP;
            } else if (itemStack.isSimilar(HealingMethod.GOLDEN_APPLE.getItem())) {
                return HealingMethod.GOLDEN_APPLE;
            } else if (itemStack.isSimilar(HealingMethod.GOD_APPLE.getItem())) {
                return HealingMethod.GOD_APPLE;
            }
        }
        return null;
    }

    public static int getHealingLeft(HealingMethod healingMethod, ItemStack[] contents) {
        if (healingMethod == null) {
            return -1;
        }

        int amount = 0;
        for (ItemStack itemStack : contents) {
            if (itemStack != null && itemStack.isSimilar(healingMethod.getItem())) {
                amount += itemStack.getAmount();
            }
        }
        return amount;
    }
}
