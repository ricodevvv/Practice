package dev.stone.practice.util;

import dev.stone.practice.util.menu.Button;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 23/06/2025
 * Project: Practice
 */
public class Constants {

    @Getter private static Random random = new Random();

    public static final Button BLACK_PANE = new Button() {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.STAINED_GLASS_PANE)
                    .durability(7) //7
                    .name(" ")
                    .build();
        }
    };

}
