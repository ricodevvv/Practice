package net.frozenorb.potpvp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import net.frozenorb.potpvp.arena.ArenaHandler;
import net.frozenorb.potpvp.commands.binds.impl.ArenaCommands;
import net.frozenorb.potpvp.commands.binds.ChatColorProvider;
import net.frozenorb.potpvp.commands.binds.UUIDDrinkProvider;
import net.frozenorb.potpvp.commands.binds.impl.KitCommands;
import net.frozenorb.potpvp.config.Lenguage;
import net.frozenorb.potpvp.kit.KitHandler;
import net.frozenorb.potpvp.util.CC;
import net.frozenorb.potpvp.util.ChunkSnapshotAdapter;
import net.frozenorb.potpvp.util.config.impl.BasicConfigurationFile;
import net.frozenorb.potpvp.util.menu.MenuListener;
import net.frozenorb.potpvp.util.serialization.*;
import net.frozenorb.potpvp.util.uuid.UUIDCache;
import net.j4c0b3y.api.config.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import xyz.refinedev.command.CommandHandler;
import xyz.refinedev.spigot.api.chunk.ChunkSnapshot;

import java.util.UUID;

@Getter
public final class PotPvPRP extends JavaPlugin {

    //fuck you kotlin
    @Getter
    private static PotPvPRP instance;

    @Getter
    private final static Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter())
            .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
            .registerTypeHierarchyAdapter(Location.class, new LocationAdapter())
            .registerTypeHierarchyAdapter(Vector.class, new VectorAdapter())
            .registerTypeAdapter(BlockVector.class, new BlockVectorAdapter())
            .registerTypeAdapter(ChunkSnapshot.class, new ChunkSnapshotAdapter())
            .serializeNulls()
            .create();

    public static Gson plainGson = new GsonBuilder()
            .registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter())
            .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
            .registerTypeHierarchyAdapter(Location.class, new LocationAdapter())
            .registerTypeHierarchyAdapter(Vector.class, new VectorAdapter())
            .registerTypeAdapter(BlockVector.class, new BlockVectorAdapter())
            .serializeNulls()
            .create();

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private ArenaHandler arenaHandler;
    public CommandHandler commandHandler;
    public ConfigHandler configHandler;
    public UUIDCache uuidCache;
    public Lenguage lenguage;
    public KitHandler kitHandler;

    private final ChatColor dominantColor = ChatColor.GOLD;


    private BasicConfigurationFile hologramsConfig;

    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        this.consoleLog("&c------------------------------------------------");
        this.setupMongo();

        this.uuidCache = new UUIDCache();

        this.commandHandler = new CommandHandler(this);
        this.commandHandler.bind(ChatColor.class).toProvider(new ChatColorProvider());
        this.commandHandler.bind(UUID.class).toProvider(new UUIDDrinkProvider());

        this.kitHandler = new KitHandler();
        this.kitHandler.Onload();

        this.registerCommands();
        this.registerPermission();

        this.configHandler = new ConfigHandler();

        configHandler.setKeyFormatter(key -> key.replace("_", "-"));

        this.lenguage = new Lenguage("lenguage", configHandler);
        lenguage.load();

        if (this.getServer().getPluginManager().isPluginEnabled("HolographicDisplays")) {
            this.logger("&7Found &cHolographicDisplays&7, Hooking holograms....");
            hologramsConfig = new BasicConfigurationFile(this, "holograms");
        }

        for (World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doMobSpawning", "false");
            world.setTime(6_000L);
        }


        arenaHandler = new ArenaHandler();

        this.getServer().getPluginManager().registerEvents(new MenuListener(this), this);
        this.logger("Registering listeners...");


        this.consoleLog("");
        this.consoleLog("&7Initialized &cPotPvP &7Successfully!");
        this.consoleLog("&c------------------------------------------------");
    }

    @Override
    public void onDisable() {
        arenaHandler.saveSchematics();
        this.kitHandler.saveKits();
    }


    private void registerCommands() {
        commandHandler.register(new ArenaCommands(), "arena");
        commandHandler.register(new KitCommands(), "kit");
        commandHandler.registerCommands();
        this.logger("Registered commands!");
    }

    private void setupMongo() {
        if (this.getConfig().getBoolean("MONGO.URI-MODE")) {
            this.mongoClient = MongoClients.create(this.getConfig().getString("MONGO.URI.CONNECTION_STRING"));
            this.mongoDatabase = mongoClient.getDatabase(this.getConfig().getString("MONGO.URI.DATABASE"));

            this.logger("Initialized MongoDB successfully!");
            return;
        }

        boolean auth = this.getConfig().getBoolean("MONGO.NORMAL.AUTHENTICATION.ENABLED");
        String host = this.getConfig().getString("MONGO.NORMAL.HOST");
        int port = this.getConfig().getInt("MONGO.NORMAL.PORT");

        String uri = "mongodb://" + host + ":" + port;

        if (auth) {
            String username = this.getConfig().getString("MONGO.NORMAL.AUTHENTICATION.USERNAME");
            String password = this.getConfig().getString("MONGO.NORMAL.AUTHENTICATION.PASSWORD");
            uri = "mongodb://" + username + ":" + password + "@" + host + ":" + port;
        }


        this.mongoClient = MongoClients.create(uri);
        this.mongoDatabase = mongoClient.getDatabase(this.getConfig().getString("MONGO.URI.DATABASE"));

        this.logger("Initialized MongoDB successfully!");
    }


    public void registerPermission() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.addPermission(new Permission("potpvp.toggleduels", PermissionDefault.OP));
        pm.addPermission(new Permission("potpvp.togglelightning", PermissionDefault.OP));
        pm.addPermission(new Permission("potpvp.silent", PermissionDefault.OP));
        pm.addPermission(new Permission("potpvp.spectate", PermissionDefault.OP));

        this.commandHandler.registerPermissions();
        this.logger("Registered permissions!");
    }

    public void logger(String message) {
        this.getServer().getConsoleSender().sendMessage(CC.translate("&c• " + message));
    }

    public void consoleLog(String string) {
        this.getServer().getConsoleSender().sendMessage(CC.translate(string));
    }

}