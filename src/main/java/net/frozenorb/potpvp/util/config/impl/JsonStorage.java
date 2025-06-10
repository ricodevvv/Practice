package net.frozenorb.potpvp.util.config.impl;

import com.google.gson.Gson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 09/06/2025
 * Project: PotPvpReprised
 */
@SuppressWarnings("unused")
public class JsonStorage<T> {

    private static final Logger LOGGER = LogManager.getLogger(JsonStorage.class);

    private final String name;
    private final File file;
    private final Gson gson;

    public JsonStorage(String name, JavaPlugin plugin, Gson gson) {
        this(name, new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "data"), gson);
    }

    public JsonStorage(String name, File directory, Gson gson) {
        if (!directory.exists()) {
            boolean created = directory.mkdir();
            if (!created) {
                LOGGER.info("[Storage] Couldn't create " + name + "'s storage");
            }
        }

        this.file = new File(directory, name + ".json");
        this.gson = gson;
        this.name = name;

        if (!this.file.exists()) {
            try {
                boolean created = this.file.createNewFile();
                if (!created) LOGGER.info("[Storage] Couldn't create " + name + "'s storage");
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Returns the data, in the way you stored.
     * Ex: List<Object>, then it will return that as it is.
     *
     * @param type {@link Type type token of that particular data}
     * @return {@link T data}
     */
    public T getData(Type type) {
        try (FileReader reader = new FileReader(this.file)) {
            return this.gson.fromJson(reader, type);
        } catch (IOException exception) {
            LOGGER.info("[Storage] Unable to load JSON Storage for " + name + ", check for syntax errors!");
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * Save data asynchronously
     *
     * @param list {@link T type}
     */
    public void saveAsync(T list) {
        CompletableFuture.runAsync(() -> this.save(list));
    }

    /**
     * Save data to storage
     *
     * @param list {@link T type}
     */
    public void save(T list) {
        try (FileWriter fileWriter = new FileWriter(this.file)) {
            this.gson.toJson(list, fileWriter);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
