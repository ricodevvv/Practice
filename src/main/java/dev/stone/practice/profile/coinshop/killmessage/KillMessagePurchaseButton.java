package dev.stone.practice.profile.coinshop.killmessage;

import com.google.common.collect.Lists;
import dev.stone.practice.kit.Kit;
import dev.stone.practice.kit.KitHandler;
import dev.stone.practice.profile.Profile;
import dev.stone.practice.profile.cosmetic.killmessages.KillMessages;
import dev.stone.practice.util.CC;
import dev.stone.practice.util.ItemBuilder;
import dev.stone.practice.util.PurchaseUtil;
import dev.stone.practice.util.menu.Button;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

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
public class KillMessagePurchaseButton extends Button {

    private final KillMessages killMessage;

    public boolean shouldUpdate(Player player, ClickType clickType) {
        return true;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (this.killMessage.hasPermission(player)) {
            player.sendMessage(ChatColor.RED + "You already have this one.");
            return;
        }
        PurchaseUtil.purchaseItem(player, killMessage);
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        List<String> lore = Lists.newArrayList();
        final int previousPrice = killMessage.getPrice();
        for (Kit kit : KitHandler.getKits()) {
            if (profile.getKitData().get(kit).getWinstreak() >= 15 && killMessage.getPrice() > 1000) {
                killMessage.setPrice(killMessage.getPrice() - 250);
                break;
            }
        }
        //if (player.hasPermission(killMessage.getPermission())) {
        if (profile.hasPermission(/*player, */killMessage.getPermission())) {
            lore.add(ChatColor.WHITE + " ");
            lore.add(ChatColor.AQUA + "Cost");
            lore.add(ChatColor.GOLD + " ┃ &fPrice: " + ChatColor.AQUA + killMessage.getPrice() + "$");
            lore.add(ChatColor.WHITE + " ");
            lore.add(ChatColor.RED + "Already purchased!");
        } else {
            lore.add(ChatColor.GRAY + " ");
            lore.add(ChatColor.AQUA + "Cost");
            lore.add(ChatColor.GOLD + " ┃ &fPrice: " + (previousPrice == killMessage.getPrice() ? "&b" + killMessage.getPrice() :  CC.GRAY + CC.STRIKE_THROUGH + previousPrice + CC.RESET + " " + "&b" + killMessage.getPrice()) + "$");
            lore.add(ChatColor.WHITE + " ");
            lore.add(profile.getCoins() >= killMessage.getPrice() ? ChatColor.GREEN + "Click to purchase!" : ChatColor.RED + "You don't have enough coins.");
        }
        return new ItemBuilder(killMessage.getIcon()).name("&b" + this.killMessage.getName()).lore(lore).build();
    }
}
