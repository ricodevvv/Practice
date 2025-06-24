package dev.stone.practice.profile.cosmetic.killmessages.menu;

import com.google.common.base.Preconditions;
import java.util.List;
import com.google.common.collect.Lists;
import dev.stone.practice.profile.Profile;
import dev.stone.practice.profile.cosmetic.killmessages.KillMessages;
import dev.stone.practice.util.CC;
import dev.stone.practice.util.ItemBuilder;
import dev.stone.practice.util.menu.Button;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 23/06/2025
 * Project: Practice
 */
public class KillMessagesButton extends Button {

    private final KillMessages killMessages;

    public KillMessagesButton(KillMessages killMessages) {
        this.killMessages = Preconditions.checkNotNull(killMessages, "killMessages");
    }

    public boolean shouldUpdate(Player player, ClickType clickType) {
        return true;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (!this.killMessages.hasPermission(player)) {
            player.sendMessage(ChatColor.RED + "No permission.");
            return;
        }
        if (profile.getOptions().killMessage().getName().equals(this.killMessages.getName())) {
            player.sendMessage(ChatColor.RED + "This kill message is already in use.");
        } else {
            profile.getOptions().killMessage(this.killMessages);
            player.sendMessage(CC.translate("&fYou have selected &" + profile.getOptions().theme().getColor().getChar() + this.killMessages.getName() + " &fkill messages!"));
        }
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        List<String> lore = Lists.newArrayList();
        if (profile.getOptions().killMessage().getName().equals(this.killMessages.getName())) {
            lore.add(ChatColor.WHITE + "You have this equipped.");
            lore.addAll(this.killMessages.getCallable().getFormattedLore());
            lore.add(ChatColor.RED + "[Already equipped]");
        } else {
            lore.add(ChatColor.WHITE + "You don't have this equipped.");
            lore.addAll(this.killMessages.getCallable().getFormattedLore());
            lore.add(ChatColor.GREEN + "[Click to equip]");
        }
        return new ItemBuilder(killMessages.getIcon()).name(profile.getOptions().killMessage().getName().equals(this.killMessages.getName()) ? "&" +
                profile.getOptions().theme().getColor().getChar() + this.killMessages.getName() + ChatColor.GREEN + " (Equipped)" : "&" + profile.getOptions().theme().getColor().getChar() + this.killMessages.getName()).lore(lore).build();
    }
}
