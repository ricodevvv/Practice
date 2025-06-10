package net.frozenorb.potpvp.kit;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import net.frozenorb.potpvp.PotPvPRP;
import net.frozenorb.potpvp.util.CC;
import net.frozenorb.potpvp.util.config.impl.JsonStorage;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 09/06/2025
 * Project: PotPvpReprised
 */

@Getter
public class KitHandler {

    public JsonStorage<Set<Kit>> storage;
    @Getter
    private static Set<Kit> kits = new HashSet<>();

    public KitHandler() {
        PotPvPRP.getInstance().consoleLog(CC.RED + "Starting kit handler...");
        this.storage = new JsonStorage<>("kits", PotPvPRP.getInstance(), PotPvPRP.getGson());
    }

    public void Onload() {
        Type kitSetType = new TypeToken<Set<Kit>>() {
        }.getType();
        Set<Kit> loadedKits = storage.getData(kitSetType);
        kits.addAll(loadedKits);
        PotPvPRP.getInstance().consoleLog(CC.RED + "Loaded " + loadedKits.size() + " kits");
    }

    public void saveKits() {
        storage.saveAsync(kits);
    }

    public static Kit getByName(String name) {
        for (Kit kit : kits) {
            if (kit.getName().equalsIgnoreCase(name)) {
                return kit;
            }
        }
        return null;
    }
}
