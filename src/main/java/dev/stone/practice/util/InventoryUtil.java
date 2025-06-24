package dev.stone.practice.util;


import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.Iterator;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 24/06/2025
 * Project: Practice
 */
public class InventoryUtil {

    public static ItemStack[] fixInventoryOrder(ItemStack[] source) {
        ItemStack[] fixed = new ItemStack[36];

        System.arraycopy(source, 0, fixed, 27, 9);
        System.arraycopy(source, 9, fixed, 0, 27);

        return fixed;
    }

  /*  public static void handleRemoveCrafting() {
        if (!Config.CRAFTING_ENABLED.toBoolean()) {
            Iterator<Recipe> iterator = Eden.INSTANCE.getServer().recipeIterator();

            while (iterator.hasNext()) {
                Recipe recipe = iterator.next();
                if (recipe != null && !Config.CRAFTING_WHITELISTED_ITEMS.toStringList().contains(recipe.getResult().getType().name())) {
                    iterator.remove();
                }
            }
        }
    } */

}
