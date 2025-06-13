package dev.stone.practice.kit;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import dev.stone.practice.Phantom;
import dev.stone.practice.util.CC;
import dev.stone.practice.util.config.impl.JsonStorage;

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
        Phantom.getInstance().consoleLog(CC.RED + "Starting kit handler...");
        this.storage = new JsonStorage<>("kits", Phantom.getInstance(), Phantom.getGson());

    }

    public void Onload() {
        Type kitSetType = new TypeToken<Set<Kit>>() {
        }.getType();
        Set<Kit> loadedKits = storage.getData(kitSetType);
        if (loadedKits != null && !loadedKits.isEmpty()) {
            kits.addAll(loadedKits);
            Phantom.getInstance().consoleLog(CC.RED + "Loaded " + loadedKits.size() + " kits");
        } else {
            Phantom.getInstance().consoleLog(CC.RED + "No kits found to load.");
        }
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
