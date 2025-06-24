package dev.stone.practice.profile.settings.menu;


import dev.stone.practice.profile.Profile;
import dev.stone.practice.profile.cosmetic.killeffect.menu.KillEffectsMenu;
import dev.stone.practice.profile.cosmetic.killmessages.menu.KillMessagesMenu;
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
public class CosmeticsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&8Cosmetics";
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
        buttons.put(10, new KillEffectsButton());
        buttons.put(12, new KillMessagesButton());
        return buttons;
    }

    @Override
    public int size(Map<Integer, Button> buttons) {
        return 27;
    }

    private static class KillEffectsButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            List<String> lore = new ArrayList<>();
            lore.add(CC.translate("&fKill Effects make your matches look better!"));
            lore.add(CC.translate("&fThey show up when someone dies in a match."));
            lore.add(CC.translate("&fCurrently Selected: &" + profile.getOptions().theme().getColor().getChar() + profile.getOptions().killEffect().getName()));
            lore.add("");
            lore.add(CC.translate("&aClick here to open the menu!"));
            return new ItemBuilder(Material.BLAZE_ROD).name(CC.translate("&" + profile.getOptions().theme().getColor().getChar() + "Kill Effects")).lore(lore).build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            new KillEffectsMenu().openMenu(player);

        }
    }

    private static class KillMessagesButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            List<String> lore = new ArrayList<>();
            lore.add(CC.translate("&fKill Messages annoy your opponent!"));
            lore.add(CC.translate("&fThey show up when a match ends."));
            lore.add(CC.translate("&fCurrently Selected: &" + profile.getOptions().theme().getColor().getChar() + profile.getOptions().killMessage().getName()));
            lore.add("");
            lore.add(CC.translate("&aClick here to open the menu!"));
            return new ItemBuilder(Material.CHEST).name(CC.translate("&" + profile.getOptions().theme().getColor().getChar() + "Kill Messages")).lore(lore).build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            new KillMessagesMenu().openMenu(player);
        }
    }

}
