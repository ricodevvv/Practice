package net.frozenorb.potpvp.util.menu.button;

import net.frozenorb.potpvp.util.CC;
import net.frozenorb.potpvp.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import net.frozenorb.potpvp.util.menu.Button;

public abstract class ToggleButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(isEnabled(player) ? Material.REDSTONE_TORCH_ON : Material.LEVER)
                .name(CC.GREEN + "Click to change " + getOptionName())
                .lore("", CC.GRAY + getDescription(), "", CC.GREEN + (isEnabled(player) ? " » " : "   ") + "Enabled", CC.RED + (!isEnabled(player) ? " » " : "   ") + "Disabled", "")
                .build();
    }

    public abstract String getOptionName();

    public abstract String getDescription();

    public abstract boolean isEnabled(Player player);

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        playNeutral(player);
        onClick(player, slot, clickType, hotbarSlot);
    }

    public abstract void onClick(Player player, int slot, ClickType clickType, int hotbarSlot);
}
