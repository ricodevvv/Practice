package dev.stone.practice.kit;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 09/06/2025
 * Project: Phantom
 */
@Getter
@Setter
public class Kit {

    private String name;
    private String displayName;
    private int priority;
    private int damageTicks = 19;
    private boolean enabled;
    private boolean ranked;
    private ItemStack displayIcon;
    private List<String> description = new ArrayList<>();
    private Collection<PotionEffect> effects = new ArrayList<>();
    private final KitLoadout kitLoadout = new KitLoadout();
    KitGameRules gameRules = new KitGameRules();
    private List<KitMatchType> kitMatchTypes = new ArrayList<>();
    private int slot;


    public Kit(String name) {
        this.name = name;
        this.displayName = name;
        this.priority = 0;
        this.displayIcon = new ItemStack(Material.DIAMOND_SWORD);

        KitHandler.getKits().add(this);
    }

}
