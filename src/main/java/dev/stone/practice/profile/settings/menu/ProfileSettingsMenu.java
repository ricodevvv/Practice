package dev.stone.practice.profile.settings.menu;

import dev.stone.practice.config.Config;
import dev.stone.practice.profile.Profile;
import dev.stone.practice.profile.coinshop.ProfileCosmeticsMenu;
import dev.stone.practice.util.CC;
import dev.stone.practice.util.Constants;
import dev.stone.practice.util.ItemBuilder;
import dev.stone.practice.util.menu.Button;
import dev.stone.practice.util.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
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
public class ProfileSettingsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return Config.MENUS_CONFIG.PROFILE_MENU_TITLE;
    }

    @Override
    public int getSize() {
        return Config.MENUS_CONFIG.PROFILE_MENU_SIZE;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int i = 10;
        for (int j = 0; j < getSize(); ++j) {
            buttons.put(j, Constants.BLACK_PANE);
        }
        while (i == 17 || i == 18 || i == 27 || i == 36) {
            i++;
        }

        buttons.put(16, new LeaderboardButton());
        buttons.put(15, new HistoryButton());
        buttons.put(14, new DivisionsButton());
        buttons.put(13, new StatsButton());
        buttons.put(12, new CosmeticsButton());
        buttons.put(11, new SettingButton());
        buttons.put(10, new EventHostButton());

        return buttons;
    }

    @AllArgsConstructor
    private class SettingButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            List<String> lore = new ArrayList<>();
            lore.add(CC.translate("&fView or change your"));
            lore.add(CC.translate("&fpersonal profile settings."));
            lore.add(CC.translate(""));
            lore.add(CC.translate("&aClick to open"));

            return new ItemBuilder(Material.REDSTONE_COMPARATOR)
                    .name(CC.translate("&" + profile.getOptions().theme().getColor().getChar()) + "Settings Menu")
                    .lore(lore)
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            new SettingsMenu().openMenu(player);
        }
    }

    @AllArgsConstructor
    private class EventHostButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            List<String> lore = new ArrayList<>();
            lore.add(CC.translate("&fHost Events."));
            lore.add(CC.translate(""));
            lore.add(CC.translate("&aClick to open"));

            return new ItemBuilder(Material.PAPER)
                    .name(CC.translate("&" + profile.getOptions().theme().getColor().getChar()) + "Events Menu")
                    .lore(lore)

                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
          //  new EventHostMenu().openMenu(player);
        }
    }

    @AllArgsConstructor
    private class CosmeticsButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            List<String> lore = new ArrayList<>();
            lore.add(CC.translate("&fOpen Cosmetics Menu"));
            lore.add(CC.translate("&fto edit and buy stuffs."));
            lore.add(CC.translate(""));
            lore.add(CC.translate("&aClick to open"));

            return new ItemBuilder(Material.FIREWORK)
                    .name(CC.translate("&" + profile.getOptions().theme().getColor().getChar()) + "Cosmetics")
                    .lore(lore)

                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            new ProfileCosmeticsMenu().openMenu(player);
        }
    }

    @AllArgsConstructor
    private class StatsButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            List<String> lore = new ArrayList<>();
            lore.add(CC.translate("&fView your personal"));
            lore.add(CC.translate("&fstats."));
            lore.add(CC.translate(""));
            lore.add(CC.translate("&aClick to open"));

            return new ItemBuilder(Material.EMERALD)
                    .name(CC.translate("&" + profile.getOptions().theme().getColor().getChar()) + "Statistics")
                    .lore(lore)

                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
          //  new StatsMenu(player).openMenu(player);
        }
    }

    @AllArgsConstructor
    private class DivisionsButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            List<String> lore = new ArrayList<>();
            lore.add(CC.translate("&fView your personal"));
            lore.add(CC.translate("&fdivisions."));
            lore.add(CC.translate(""));
            lore.add(CC.translate("&aClick to open"));

            return new ItemBuilder(Material.ITEM_FRAME)
                    .name(CC.translate("&" + profile.getOptions().theme().getColor().getChar()) + "Divisions Menu")
                    .lore(lore)

                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
           // new ProfileDivisionsMenu(profile).openMenu(player);
        }
    }

    @AllArgsConstructor
    private class HistoryButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            List<String> lore = new ArrayList<>();
            lore.add(CC.translate("&fShow your"));
            lore.add(CC.translate("&fmatches history."));
            lore.add(CC.translate(""));
            lore.add(CC.translate("&aClick to open"));

            return new ItemBuilder(Material.REDSTONE_COMPARATOR)
                    .name(CC.translate("&" + profile.getOptions().theme().getColor().getChar()) + "Match History")
                    .lore(lore)
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
          //  new MatchHistoryMenu(profile).openMenu(player);
        }
    }

    @AllArgsConstructor
    private class LeaderboardButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            List<String> lore = new ArrayList<>();
            lore.add(CC.translate("&fOpen the leaderboard to see"));
            lore.add(CC.translate("&fthe best players on the list"));
            lore.add("");
            lore.add(CC.translate("&aClick to open"));

            return new ItemBuilder(Material.COMPASS)
                    .name(CC.translate("&" + profile.getOptions().theme().getColor().getChar()) + "Leaderboard")
                    .lore(lore)
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            player.performCommand("leaderboards");
        }
    }
}
