package dev.stone.practice.profile.coinshop;

import dev.stone.practice.profile.settings.menu.CosmeticsMenu;
import dev.stone.practice.util.CC;
import dev.stone.practice.util.Constants;
import dev.stone.practice.util.ItemBuilder;
import dev.stone.practice.util.menu.Button;
import dev.stone.practice.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 23/06/2025
 * Project: Practice
 */
public class ProfileCosmeticsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&8Profile Cosmetics";
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        for (int k = 0; k < 27; k++) {
            buttons.put(k, Constants.BLACK_PANE);
        }
        buttons.put(12, new CoinShopButton());
        buttons.put(14, new CosmeticsButton());
        return buttons;
    }

    @Override
    public int size(Map<Integer, Button> buttons) {
        return 27;
    }

    private static class CoinShopButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = new ArrayList<>();
            lore.add(CC.translate("&fBuy cosmetics to make"));
            lore.add(CC.translate("&fthe match more funny!"));
            lore.add("");
            lore.add(CC.translate("&aClick here to purchase some cosmetics!"));
            return new ItemBuilder(Material.BEACON).name(CC.translate("&cCoin Shop")).lore(lore).build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            new CoinShopMenu().openMenu(player);
        }
    }

    private static class CosmeticsButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = new ArrayList<>();
            lore.add(CC.translate("&fMatch Killeffects"));
            lore.add(CC.translate("&fMatch Kill Messages"));
            lore.add("");
            lore.add(CC.translate("&aClick here to open your cosmetics menu!"));
            return new ItemBuilder(Material.CHEST).name(CC.translate("&cYour Cosmetics")).lore(lore).build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            new CosmeticsMenu().openMenu(player);
        }
    }
}
