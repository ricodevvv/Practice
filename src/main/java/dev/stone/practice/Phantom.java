package dev.stone.practice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import dev.stone.practice.adapter.scoreboard.ScoreboardAdapter;
import dev.stone.practice.arena.ArenaHandler;
import dev.stone.practice.arena.regen.IArenaRegen;
import dev.stone.practice.arena.regen.RegenerationManager;
import dev.stone.practice.arena.regen.impl.legacy.chunk.ChunkRestorationManager;
import dev.stone.practice.commands.binds.ChatColorProvider;
import dev.stone.practice.commands.binds.UUIDDrinkProvider;
import dev.stone.practice.commands.impl.ArenaCommands;
import dev.stone.practice.commands.impl.KitCommands;
import dev.stone.practice.commands.impl.dev.MatchDebugCommand;
import dev.stone.practice.commands.impl.match.ViewInventoryCommand;
import dev.stone.practice.commands.impl.misc.SetLobbyCommand;
import dev.stone.practice.commands.impl.profile.ProfileBuildCommand;
import dev.stone.practice.commands.impl.profile.ProfileSettingsCommand;
import dev.stone.practice.commands.impl.queue.QueueCommands;
import dev.stone.practice.config.Config;
import dev.stone.practice.config.Lenguaje;
import dev.stone.practice.config.Scoreboard;
import dev.stone.practice.kit.KitHandler;
import dev.stone.practice.lobby.LobbyListener;
import dev.stone.practice.lobby.LobbyManager;
import dev.stone.practice.match.MatchHandler;
import dev.stone.practice.match.listener.blocks.BlockBreak;
import dev.stone.practice.match.listener.blocks.BlockFromTo;
import dev.stone.practice.match.listener.blocks.BlockPlace;
import dev.stone.practice.match.listener.blocks.BucketEmpty;
import dev.stone.practice.match.listener.custom.MatchStartListener;
import dev.stone.practice.match.listener.player.DamageListener;
import dev.stone.practice.match.listener.player.PlayerDeathEvent;
import dev.stone.practice.match.listener.player.PlayerMove;
import dev.stone.practice.match.listener.player.PlayerQuitEvent;
import dev.stone.practice.match.listener.potion.PotionListener;
import dev.stone.practice.profile.Profile;
import dev.stone.practice.profile.division.DivisionsManager;
import dev.stone.practice.profile.listeners.ProfileListener;
import dev.stone.practice.queue.Queue;
import dev.stone.practice.util.CC;
import dev.stone.practice.util.config.impl.BasicConfigurationFile;
import dev.stone.practice.util.menu.MenuListener;
import dev.stone.practice.util.procedure.ProcedureListener;
import dev.stone.practice.util.serialization.*;
import io.github.epicgo.sconey.SconeyHandler;
import lombok.Getter;
import net.j4c0b3y.api.config.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import xyz.refinedev.command.CommandHandler;

import java.util.Arrays;
import java.util.UUID;


@Getter
public final class Phantom extends JavaPlugin {

    //fuck you kotlin
    @Getter
    private static Phantom instance;

    @Getter
    private final static Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter())
            .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
            .registerTypeHierarchyAdapter(Location.class, new LocationAdapter())
            .registerTypeHierarchyAdapter(Vector.class, new VectorAdapter())
            .registerTypeAdapter(BlockVector.class, new BlockVectorAdapter())
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
    public Config language;
    public KitHandler kitHandler;
    public LobbyManager lobbyManager;
    public MatchHandler matchHandler;
    public BasicConfigurationFile hotbar;
    private final ChatColor dominantColor = ChatColor.GOLD;
    public BasicConfigurationFile databaseConfig, scoreboardConfig, divisionsConfig;
    public Cache cache;
    public Placeholders placeholders;
    private SconeyHandler scoreboardHandler;
    private DivisionsManager divisionsManager;
    private ChunkRestorationManager chunkRestorationManager;
    private IArenaRegen regeneration;


    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();
        this.databaseConfig = new BasicConfigurationFile(this, "database");
        this.setupMongo();
    }

    @Override
    public void onEnable() {
        this.configHandler = new ConfigHandler();
        configHandler.setKeyFormatter(key -> key.replace("_", "-"));
        configHandler.setKeyFormatter(CC::translate);

        this.language = new Config("config", configHandler);
        Lenguaje lang = new Lenguaje("lang", configHandler);
        Scoreboard scoreboard = new Scoreboard("test-score", configHandler);
        scoreboard.load();
        lang.load();
        language.load();
        this.divisionsConfig = new BasicConfigurationFile(this, "divisions");
        this.scoreboardConfig = new BasicConfigurationFile(this, "scoreboard");
        this.hotbar = new BasicConfigurationFile(this, "hotbar");

        this.commandHandler = new CommandHandler(this);
        this.commandHandler.bind(ChatColor.class).toProvider(new ChatColorProvider());
        this.commandHandler.bind(UUID.class).toProvider(new UUIDDrinkProvider());

        this.kitHandler = new KitHandler();
        this.kitHandler.Onload();
        this.cache = new Cache();
        this.placeholders = new Placeholders(this);

        this.registerCommands();
        this.registerPermission();
        this.loadListeners();
        this.logger("Registering listeners...");

        Profile.init();
        Queue.init();
        this.divisionsManager = new DivisionsManager(this);
        this.divisionsManager.init();

        this.lobbyManager = new LobbyManager(this);

        for (World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doMobSpawning", "false");
            world.setTime(6_000L);

            world.getEntities().stream().filter(entity -> entity.getType() != EntityType.PLAYER && entity.getType() != EntityType.ITEM_FRAME).forEach(Entity::remove);
            world.setStorm(false);
            world.setThundering(false);
            world.setTime(0L);
        }

        arenaHandler = new ArenaHandler();
        this.matchHandler = new MatchHandler("Starting MatchHandler");
        this.chunkRestorationManager = new ChunkRestorationManager();
        this.regeneration = RegenerationManager.get();

        // Scoreboard load
        this.scoreboardHandler = new SconeyHandler(this, new ScoreboardAdapter());
        this.consoleLog("");
        this.consoleLog("&7Initialized &cPhantom &7Successfully!");
    }

    @Override
    public void onDisable() {
        arenaHandler.saveSchematics();
        this.kitHandler.saveKits();
    }


    private void registerCommands() {
        commandHandler.register(new ArenaCommands(), "arena");
        commandHandler.register(new KitCommands(), "kit");
        commandHandler.register(new ProfileBuildCommand(), "build");
        commandHandler.register(new SetLobbyCommand(), "setlobby");
        commandHandler.register(new MatchDebugCommand(), "debugmatch");
        commandHandler.register(new QueueCommands(), "queue");
        commandHandler.register(new ProfileSettingsCommand(), "psettings");
        commandHandler.register(new ViewInventoryCommand(), "viewinv");
        commandHandler.registerCommands();
        this.logger("Registered commands!");
    }

    private void loadListeners() {
        Arrays.asList(
                new MenuListener(this),
                new ProcedureListener(),
                new ProfileListener(),
                new LobbyListener(),
                new MatchStartListener(),
                new PlayerDeathEvent(),
                new PlayerQuitEvent(),
                new DamageListener(),
                new BlockFromTo(),
                new BucketEmpty(),
                new PotionListener(),
                new BlockBreak(),
                new BlockPlace(),
                new PlayerMove()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void setupMongo() {
        if (this.databaseConfig.getBoolean("MONGO.URI-MODE")) {
            this.mongoClient = MongoClients.create(this.databaseConfig.getString("MONGO.URI.CONNECTION_STRING"));
            this.mongoDatabase = mongoClient.getDatabase(this.databaseConfig.getString("MONGO.URI.DATABASE"));
            return;
        }

        boolean auth = this.databaseConfig.getBoolean("MONGO.NORMAL.AUTHENTICATION.ENABLED");
        String host = this.databaseConfig.getString("MONGO.NORMAL.HOST");
        int port = this.databaseConfig.getInteger("MONGO.NORMAL.PORT");

        String uri = "mongodb://" + host + ":" + port;

        if (auth) {
            String username = this.databaseConfig.getString("MONGO.NORMAL.AUTHENTICATION.USERNAME");
            String password = this.databaseConfig.getString("MONGO.NORMAL.AUTHENTICATION.PASSWORD");
            uri = "mongodb://" + username + ":" + password + "@" + host + ":" + port;
        }


        this.mongoClient = MongoClients.create(uri);
        this.mongoDatabase = mongoClient.getDatabase(this.databaseConfig.getString("MONGO.URI.DATABASE"));
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
        this.getServer().getConsoleSender().sendMessage(CC.translate("§8[§bPractice§8] &f" + message));
    }

    public void consoleLog(String string) {
        this.getServer().getConsoleSender().sendMessage(CC.translate("§8[§bPractice§8] §f" + string));
    }

}