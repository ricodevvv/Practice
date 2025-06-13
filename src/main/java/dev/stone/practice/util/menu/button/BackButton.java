package dev.stone.practice.util.menu.button;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import dev.stone.practice.util.CC;
import dev.stone.practice.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.stone.practice.util.menu.Button;
import dev.stone.practice.util.menu.Menu;

@RequiredArgsConstructor
@AllArgsConstructor
public class BackButton extends Button {

    private final Material material;
    private int durability = 0;
    private final Menu back;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(material)
                .name(CC.RED + "Click to back")
                .durability(durability)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Button.playNeutral(player);
        back.openMenu(player);
    }

}
