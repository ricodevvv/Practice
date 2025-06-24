package dev.stone.practice.profile.coinshop.killeffects;

import dev.stone.practice.profile.Profile;
import dev.stone.practice.profile.coinshop.CoinShopMenu;
import dev.stone.practice.profile.cosmetic.killeffect.SpecialEffects;
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
public class SpecialEffectsPurchaseMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&8Coin Shop";
    }

    public int size(Map<Integer, Button> buttons) {
        return 36;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        HashMap<Integer, Button> buttons = new HashMap<>();
        for (int k = 0; k < 36; k++) {
            buttons.put(k, Constants.BLACK_PANE);
        }
        int y = 1;
        int x = 1;
        for (SpecialEffects effects : SpecialEffects.values()) {
            if (effects.equals(SpecialEffects.NONE)) continue;
            buttons.put(this.getSlot(x++, y), new SpecialEffectPurchaseButton(effects));
            if (x != 8) continue;
            ++y;
            x = 1;
        }
        buttons.put(0, new BacksButton());
        return buttons;
    }
    private static class BacksButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = new ArrayList<>();
            Profile profile = Profile.getByUuid(player.getUniqueId());
            lore.add("");
            lore.add("&aClick Here to go to the previous page");
            return new ItemBuilder(Material.ARROW).name(CC.translate("&b&lGo Back")).lore(lore).build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            new CoinShopMenu().openMenu(player);
        }
    }
}
