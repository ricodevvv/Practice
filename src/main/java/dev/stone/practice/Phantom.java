package dev.stone.practice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import dev.stone.practice.adapter.board.NameThreadFactory;
import dev.stone.practice.adapter.board.listener.ScoreboardListener;
import dev.stone.practice.adapter.board.task.BoardTask;
import dev.stone.practice.arena.ArenaHandler;
import dev.stone.practice.commands.binds.ChatColorProvider;
import dev.stone.practice.commands.binds.UUIDDrinkProvider;
import dev.stone.practice.commands.impl.ArenaCommands;
import dev.stone.practice.commands.impl.KitCommands;
import dev.stone.practice.commands.impl.dev.MatchDebugCommand;
import dev.stone.practice.commands.impl.misc.SetLobbyCommand;
import dev.stone.practice.commands.impl.profile.ProfileBuildCommand;
import dev.stone.practice.commands.impl.queue.QueueCommands;
import dev.stone.practice.config.Config;
import dev.stone.practice.config.DatabaseConfig;
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
import dev.stone.practice.profile.PlayerProfile;
import dev.stone.practice.profile.listeners.ProfileListener;
import dev.stone.practice.queue.Queue;
import dev.stone.practice.util.CC;
import dev.stone.practice.util.ChunkSnapshotAdapter;
import dev.stone.practice.util.config.impl.BasicConfigurationFile;
import dev.stone.practice.util.menu.MenuListener;
import dev.stone.practice.util.procedure.ProcedureListener;
import dev.stone.practice.util.serialization.*;
import lombok.Getter;
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

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    public Config language;
    public KitHandler kitHandler;
    public LobbyManager lobbyManager;
    public MatchHandler matchHandler;
    public DatabaseConfig databaseConfig;
    public BasicConfigurationFile hotbar;
    private final ChatColor dominantColor = ChatColor.GOLD;

    private ScheduledExecutorService executor;


    private BasicConfigurationFile hologramsConfig;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.consoleLog("&c------------------------------------------------");
        this.configHandler = new ConfigHandler();
        configHandler.setKeyFormatter(key -> key.replace("_", "-"));
        databaseConfig = new DatabaseConfig("database", configHandler);
        databaseConfig.load();
        this.setupMongo();

        this.language = new Config("lenguage", configHandler);
        Lenguaje lang = new Lenguaje("lang", configHandler);
        Scoreboard scoreboard = new Scoreboard("scoreboard", configHandler);
        scoreboard.load();
        lang.load();
        language.load();
        this.hotbar = new BasicConfigurationFile(this, "hotbar");

        this.commandHandler = new CommandHandler(this);
        this.commandHandler.bind(ChatColor.class).toProvider(new ChatColorProvider());
        this.commandHandler.bind(UUID.class).toProvider(new UUIDDrinkProvider());

        this.kitHandler = new KitHandler();
        this.kitHandler.Onload();

        this.registerCommands();
        this.registerPermission();
        this.loadListeners();
        this.logger("Registering listeners...");

        PlayerProfile.init();
        Queue.init();

        this.lobbyManager = new LobbyManager(this);

        for (World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doMobSpawning", "false");
            world.setTime(6_000L);
        }

        // Scoreboard load
        this.executor = Executors.newScheduledThreadPool(1, new NameThreadFactory("Amber - BoardThread"));
        this.executor.scheduleAtFixedRate(new BoardTask(), 0L, 100L, TimeUnit.MILLISECONDS);


        arenaHandler = new ArenaHandler();
        this.matchHandler = new MatchHandler("Starting MatchHandler");

        this.consoleLog("");
        this.consoleLog("&7Initialized &cPhantom &7Successfully!");
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
        commandHandler.register(new ProfileBuildCommand(), "build");
        commandHandler.register(new SetLobbyCommand(), "setlobby");
        commandHandler.register(new MatchDebugCommand(), "debugmatch");
        commandHandler.register(new QueueCommands(), "queue");
        commandHandler.registerCommands();
        this.logger("Registered commands!");
    }

    private void loadListeners() {
        Arrays.asList(
                new MenuListener(this),
                new ProcedureListener(),
                new ScoreboardListener(),
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
        if (DatabaseConfig.URI_MODE) {
            this.mongoClient = MongoClients.create(DatabaseConfig.URI.CONNECTION_STRING);
            this.mongoDatabase = mongoClient.getDatabase(DatabaseConfig.URI.DATABASE);

            this.logger("Initialized MongoDB successfully!");
            return;
        }

        boolean auth = DatabaseConfig.NORMAL.AUTHENTICATION.ENABLED;
        String host = DatabaseConfig.NORMAL.HOST;
        int port = DatabaseConfig.NORMAL.PORT;

        String uri = "mongodb://" + host + ":" + port;

        if (auth) {
            String username = DatabaseConfig.NORMAL.AUTHENTICATION.USERNAME;
            String password = DatabaseConfig.NORMAL.AUTHENTICATION.PASSWORD;
            uri = "mongodb://" + username + ":" + password + "@" + host + ":" + port;
        }

        this.mongoClient = MongoClients.create(uri);
        this.mongoDatabase = mongoClient.getDatabase(DatabaseConfig.URI.DATABASE);

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