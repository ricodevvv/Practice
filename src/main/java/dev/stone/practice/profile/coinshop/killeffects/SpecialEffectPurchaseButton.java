package dev.stone.practice.profile.coinshop.killeffects;

import dev.stone.practice.kit.Kit;
import dev.stone.practice.kit.KitHandler;
import dev.stone.practice.profile.Profile;
import dev.stone.practice.profile.cosmetic.killeffect.SpecialEffects;
import dev.stone.practice.util.CC;
import dev.stone.practice.util.ItemBuilder;
import dev.stone.practice.util.PurchaseUtil;
import dev.stone.practice.util.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 23/06/2025
 * Project: Practice
 */
@AllArgsConstructor
public class SpecialEffectPurchaseButton extends Button {
    private final SpecialEffects effect;

    public boolean shouldUpdate(Player player, ClickType clickType) {
        return true;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
       Profile profile = Profile.getByUuid(player.getUniqueId());
        if (this.effect.hasPermission(player)) {
            player.sendMessage(ChatColor.RED + "You already have this one.");
            return;
        }
        PurchaseUtil.purchaseItem(player, effect);
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        List<String> lore = Lists.newArrayList();
        final int previousPrice = effect.getPrice();
        for (Kit kit : KitHandler.getKits()) {
            if (profile.getKitData().get(kit).getWinstreak() >= 15 && effect.getPrice() > 1000) {
                effect.setPrice(effect.getPrice() - 250);
                break;
            }
        }
        //if (player.hasPermission(effect.getPermission())) {
        if (profile.hasPermission(/*player, */effect.getPermission())) {
            lore.add(ChatColor.WHITE + " ");
            lore.add(ChatColor.AQUA + "Cost");
            lore.add(ChatColor.GOLD + " ┃ &fPrice: " + "&b" + effect.getPrice() + "$");
            lore.add(ChatColor.WHITE + " ");
            lore.add(ChatColor.RED + "Already purchased!");
        } else {
            lore.add(ChatColor.WHITE + " ");
            lore.add(ChatColor.AQUA + "Cost");
            lore.add(ChatColor.GOLD + " ┃ &fPrice: " + (previousPrice == effect.getPrice() ? "&b" + effect.getPrice() : CC.GRAY + CC.STRIKE_THROUGH + previousPrice + CC.RESET + " " + "&b" + effect.getPrice()) + "$");
            lore.add(ChatColor.WHITE + " ");
            lore.add(profile.getCoins() >= effect.getPrice() ? ChatColor.GREEN + "Click to purchase!" : ChatColor.RED + "You don't have enough coins.");
        }
        return new ItemBuilder(effect.getIcon()).name("&b" + this.effect.getName()).lore(lore).build();
    }
}
