package dev.stone.practice.util.menu.button;

import lombok.AllArgsConstructor;
import dev.stone.practice.util.CC;
import dev.stone.practice.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.stone.practice.util.menu.Button;
import dev.stone.practice.util.menu.Menu;
import dev.stone.practice.util.menu.TypeCallback;

@AllArgsConstructor
public class ConfirmationButton extends Button {

    private boolean confirm;
    private TypeCallback<Boolean> callback;
    private boolean closeAfterResponse;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.STAINED_GLASS_PANE)
                .durability(this.confirm ? 5 : 14)
                .name(this.confirm ? CC.GREEN + "Confirm" : CC.RED + "No confirm")
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (this.confirm) {
            player.playSound(player.getLocation(), Sound.NOTE_PIANO, 20f, 0.1f);
        } else {
            player.playSound(player.getLocation(), Sound.DIG_GRAVEL, 20f, 0.1F);
        }

        if (this.closeAfterResponse) {
            Menu menu = Menu.currentlyOpenedMenus.get(player.getUniqueId());

            if (menu != null) {
                menu.setClosedByMenu(true);
            }

            player.closeInventory();
        }

        this.callback.callback(this.confirm);
    }

}
