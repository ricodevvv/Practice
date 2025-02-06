package net.frozenorb.potpvp.events;

import com.qrakn.morpheus.game.Game;
import com.qrakn.morpheus.game.GameQueue;
import lombok.experimental.UtilityClass;
import net.frozenorb.potpvp.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static net.md_5.bungee.api.ChatColor.LIGHT_PURPLE;

@UtilityClass
public final class EventItems {

    public static ItemStack getEventItem() {
        List<Game> game = GameQueue.INSTANCE.getCurrentGames();

        if (!game.isEmpty()) {
            return ItemBuilder.of(Material.EMERALD).name(LIGHT_PURPLE + "Join An Event").build();
        }

        return null;
    }

}