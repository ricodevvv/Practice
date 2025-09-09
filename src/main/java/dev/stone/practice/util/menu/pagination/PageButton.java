package dev.stone.practice.util.menu.pagination;

import lombok.AllArgsConstructor;
import dev.stone.practice.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import dev.stone.practice.util.menu.Button;

@AllArgsConstructor
public class PageButton extends Button {

    private int mod;
    private PaginatedMenu menu;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.mod > 0) {
            if (hasNext(player)) {
                return new ItemBuilder(Material.ARROW)
                        .name(dev.stone.practice.config.Lenguaje.MENU_UI.NEXT_PAGE_NAME)
                        .lore(dev.stone.practice.config.Lenguaje.MENU_UI.NEXT_PAGE_LORE)
                        .build();
            } else {
                return new ItemBuilder(Material.ARROW)
                        .name(dev.stone.practice.config.Lenguaje.MENU_UI.DISABLED_NAME)
                        .lore(dev.stone.practice.config.Lenguaje.MENU_UI.DISABLED_LORE)
                        .build();
            }
        } else {
            if (hasPrevious(player)) {
                return new ItemBuilder(Material.ARROW)
                        .name(dev.stone.practice.config.Lenguaje.MENU_UI.PREVIOUS_PAGE_NAME)
                        .lore(dev.stone.practice.config.Lenguaje.MENU_UI.PREVIOUS_PAGE_LORE)
                        .build();
            } else {
                return new ItemBuilder(Material.ARROW)
                        .name(dev.stone.practice.config.Lenguaje.MENU_UI.DISABLED_NAME)
                        .lore(dev.stone.practice.config.Lenguaje.MENU_UI.DISABLED_LORE)
                        .build();
            }
        }
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (this.mod > 0) {
            if (hasNext(player)) {
                this.menu.modPage(player, this.mod);
                Button.playNeutral(player);
            } else {
                Button.playFail(player);

            }
        } else {
            if (hasPrevious(player)) {
                this.menu.modPage(player, this.mod);
                Button.playNeutral(player);
            } else {
                Button.playFail(player);
            }
        }
    }

    private boolean hasNext(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return this.menu.getPages(player) >= pg;
    }

    private boolean hasPrevious(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return pg > 0;
    }

}
