package dev.stone.practice.match.menu;

import dev.stone.practice.config.Lenguaje;
import dev.stone.practice.match.PostMatchInventory;
import dev.stone.practice.util.*;
import dev.stone.practice.util.menu.Button;
import dev.stone.practice.util.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 24/06/2025
 * Project: Practice
 */
@AllArgsConstructor
public class ViewInventoryMenu extends Menu {

    private PostMatchInventory info;

    @Override
    public String getTitle(Player player) {
        return Lenguaje.MATCH_MESSAGES.POST_INVENTORY.MENU_TITLE.replace("<player>", info.getOwner());
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i = 0; i < info.getContents().length; i++) {
            ItemStack itemStack = InventoryUtil.fixInventoryOrder(info.getContents())[i];

            if (itemStack != null && itemStack.getType() != Material.AIR) {
                buttons.put(i, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return itemStack;
                    }
                });
            }
        }

        for (int i = 36; i < 45; i++) {
            buttons.put(i, placeholderButton);
        }

        for (int i = 0; i < info.getArmor().length; i++) {
            ItemStack itemStack = info.getArmor()[i];
            if (itemStack != null && itemStack.getType() != Material.AIR) {
                buttons.put(48-i, new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return itemStack;
                    }
                });
            }
        }

        buttons.put(49, new PlayerInformationButton());
        buttons.put(50, new EffectsButton());
        buttons.put(51, new HealingButton());
        buttons.put(52, new StatisticsButton());
        if (info.getSwitchToUUID() != null) {
            buttons.put(53, new SwitchInventoryButton());
        }

        return buttons;
    }

    @Override
    public void onOpen(Player player) {
        player.sendMessage("&fYou are now viewing &b<player>'s &finventory".replace("<player>", info.getOwner()));
    }

    private class SwitchInventoryButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.LEVER)
                    .name(Lenguaje.MATCH_MESSAGES.POST_INVENTORY.VIEW_INVENTORY.replace("<player", info.getSwitchTo()))
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            Util.performCommand(player, "viewinv " + info.getSwitchToUUID().toString());
        }
    }

    private class PlayerInformationButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> LORE = Lenguaje.MATCH_MESSAGES.POST_INVENTORY.PLAYER_INFO.stream()
                    .map(key -> key
                            .replace("<hearts>", String.valueOf(info.getHealth()))
                            .replace("<max_heart>", String.valueOf(info.getMaxHealth()))
                            .replace("<hunger>", String.valueOf(info.getHunger()))
                    )
                    .collect(Collectors.toList());

            return new ItemBuilder(Material.SKULL_ITEM)
                    .durability(3)
                    .headTexture(info.getOwnerHeadValue())
                    .name(Lenguaje.MATCH_MESSAGES.POST_INVENTORY.PLAYER_INFO_NAME)
                    .lore(LORE)
                    .build();
        }
    }

    @AllArgsConstructor
    private class EffectsButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder builder = new ItemBuilder(Material.POTION).name("&fNo potion effects found");

            if (info.getEffects().isEmpty()) {
                builder.lore("&bPotion Effects)");
            } else {
                List<String> lore = new ArrayList<>();
                info.getEffects().forEach(effect -> {
                    String name = WordUtil.formatWords(effect.getType().getName()) + " " + (effect.getAmplifier() + 1);
                    String duration = TimeUtil.millisToTimer((effect.getDuration() / 20) * 1000L);
                    lore.add(Arrays.asList(
                            "",
                            "&fNo potion effects found",
                            ""
                    ).toString());
                });
                lore.add(0, "");
                lore.add("");
                builder.lore(lore);
            }
            return builder.build();
        }
    }

    @AllArgsConstructor
    private class HealingButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(info.getHealingMethod() == null ? new ItemBuilder(Material.STAINED_GLASS_PANE).durability(14).build() : info.getHealingMethod().getItem().clone())
                  //  .name(Language.MATCH_VIEW_INVENTORY_MENU_HEALING_BUTTON_NAME.toString())
                  //  .lore(info.getHealingMethod() == null ? Language.MATCH_VIEW_INVENTORY_MENU_HEALING_BUTTON_NO_HEALING_LORE.toStringList(player) : Language.MATCH_VIEW_INVENTORY_MENU_HEALING_BUTTON_HEALING_LORE.toStringList(info.getOwner(), HealingMethod.getHealingLeft(info.getHealingMethod(), info.getContents()), info.getHealingMethod().getName()))
                    .build();
        }
    }

    private class StatisticsButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAPER)
                   // .name(Language.MATCH_VIEW_INVENTORY_MENU_STATISTICS_BUTTON_NAME.toString())
                   // .lore(Language.MATCH_VIEW_INVENTORY_MENU_STATISTICS_BUTTON_LORE.toStringList(player, info.getHits(), info.getBlockedHits(), info.getLongestCombo(), info.getPotionsThrown(), info.getPotionsMissed(), info.getPotionAccuracy()))
                    .build();
        }
    }

}
