package net.frozenorb.potpvp.profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import lombok.Setter;
import net.frozenorb.potpvp.PotPvPRP;
import net.frozenorb.potpvp.kit.Kit;
import net.frozenorb.potpvp.kit.KitHandler;
import net.frozenorb.potpvp.kit.KitLoadout;
import net.frozenorb.potpvp.match.Match;
import net.frozenorb.potpvp.profile.data.ProfileKitData;
import net.frozenorb.potpvp.util.BukkitSerialization;
import net.frozenorb.potpvp.util.CC;
import net.frozenorb.potpvp.util.Common;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 10/06/2025
 * Project: Phantom
 */
@Getter @Setter
public class Profile {


   @Getter private static Map<UUID, Profile> profiles = new HashMap<>();

    public static MongoCollection<Document> collection;

    private UUID uuid;
    private int globalElo = 1000;
    private int globalExp = 0;
    private String name;
    private final List<UUID> followers;
    private ProfileState state;
    private Match match;
    private final Map<Kit, ProfileKitData> kitData;

    /**
     * This stores a player's build mode.
     */
    @Getter @Setter private boolean build;

    /**
     * This stores a player's amount of coins.
     */
    @Getter @Setter private int coins;

    /**
     * This stores a player's amount of event tokens.
     */
    @Getter @Setter private int eventTokens;

    /**
     * This stores a player's exp.
     */
    @Getter @Setter private int experience;

    /**
     * This stores a player's level.
     */
    @Getter @Setter private int level;

    /**
     * This stores a player's ranked ban status.
     */
    @Getter @Setter private boolean rankedBan;

    /**
     * This stores the reason for a player's ranked ban.
     */
    @Getter @Setter private String rankedBanReason;

    /**
     * This stores the ID for a player's ranked ban.
     */
    @Getter @Setter private String rankedBanID;


    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.build = false;
        this.state = ProfileState.LOBBY;
        this.kitData = new HashMap<>();
        this.followers = new ArrayList<>();
        this.rankedBan = false;
        this.rankedBanReason = "None";
        this.rankedBanID = "None";
        this.coins = 0;
        this.eventTokens = 0;
        this.experience = 0;
        this.level = 0;


        for (Kit kit : KitHandler.getKits()) {
            kitData.put(kit, new ProfileKitData());
        }

    }

    public Profile(Player player) {
        this.build = false;
        this.uuid = uuid;
        this.state = ProfileState.LOBBY;
        this.kitData = new HashMap<>();
        this.followers = new ArrayList<>();
        this.rankedBan = false;
        this.rankedBanReason = "None";
        this.rankedBanID = "None";
        this.coins = 0;
        this.eventTokens = 0;
        this.experience = 0;
        this.level = 0;

    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }


    public boolean isInMatch() {
        return match != null;
    }

    public boolean isInFight() {
        return state == ProfileState.FIGHTING && match != null;
    }

    public boolean isBusy() {
        return state != ProfileState.LOBBY;
    }

    public void load() {
        Document document = collection.find(Filters.eq("uuid", uuid.toString())).first();

        if (document == null) {
            this.save();
            return;
        }

        Document options = (Document) document.get("options");

        Document playerStatus = (Document) document.get("status");
        this.setRankedBan(((Boolean) playerStatus.get("rankedBan")).booleanValue());
        this.setRankedBanID((String) playerStatus.get("rankedBanID"));
        this.setRankedBanReason((String) playerStatus.get("rankedBanReason"));
        this.setCoins(((Integer) playerStatus.get("coins")).intValue());
        this.setEventTokens(((Integer) playerStatus.get("eventTokens")).intValue());
        this.setLevel(((Integer) playerStatus.get("level")).intValue());
        this.setExperience(((Integer) playerStatus.get("experience")).intValue());
        List<String> permissionsList = (List<String>) playerStatus.get("permissions");



        Document kitStatistics = (Document) document.get("kitStatistics");

        for (String key : kitStatistics.keySet()) {
            Document kitDocument = (Document) kitStatistics.get(key);
            Kit kit = KitHandler.getByName(key);

            if (kit != null) {
                ProfileKitData profileKitData = new ProfileKitData();
                profileKitData.setElo(((Integer) kitDocument.get("elo")).intValue());
                profileKitData.setUnrankedWon(((Integer) kitDocument.get("won")).intValue());
                profileKitData.setUnrankedLost(((Integer) kitDocument.get("lost")).intValue());
                profileKitData.setWinstreak(((Integer) kitDocument.get("winstreak")).intValue());

                kitData.put(kit, profileKitData);
            }
        }

        Document kitsDocument = (Document) document.get("loadouts");

        for (String key : kitsDocument.keySet()) {
            Kit kit = KitHandler.getByName(key);

            if (kit != null) {
                JsonArray kitsArray = new JsonParser().parse((String) kitsDocument.get(key)).getAsJsonArray();
                KitLoadout[] loadouts = new KitLoadout[4];

                for (JsonElement kitElement : kitsArray) {
                    JsonObject kitObject = kitElement.getAsJsonObject();

                    KitLoadout loadout = new KitLoadout(kitObject.get("name").getAsString());
                    loadout.setArmor(BukkitSerialization.itemStackArrayFromBase64(kitObject.get("armor").getAsString()));
                    loadout.setContents(BukkitSerialization.itemStackArrayFromBase64(kitObject.get("contents").getAsString()));

                    loadouts[kitObject.get("index").getAsInt()] = loadout;
                }

                kitData.get(kit).setLoadouts(loadouts);
            }
        }
    }

    public void save() {
        Document document = new Document();
        document.put("uuid", uuid.toString());

        Document optionsDocument = new Document();
        document.put("options", optionsDocument);
        Document statusDocument = new Document();
        statusDocument.put("rankedBan", rankedBan);
        statusDocument.put("rankedBanID", rankedBanID);
        statusDocument.put("rankedBanReason", rankedBanReason);
        statusDocument.put("coins", coins);
        statusDocument.put("eventTokens", eventTokens);
        statusDocument.put("level", level);
        statusDocument.put("experience", experience);
        document.put("status", statusDocument);


        Document kitStatisticsDocument = new Document();

        for (Map.Entry<Kit, ProfileKitData> entry : kitData.entrySet()) {
            Document kitDocument = new Document();
            kitDocument.put("elo", entry.getValue().getElo());
            kitDocument.put("won", entry.getValue().getWon());
            kitDocument.put("lost", entry.getValue().getUnrankedLost());
            kitDocument.put("kills", entry.getValue().getWon());
            kitDocument.put("winstreak", entry.getValue().getWinstreak());
            kitStatisticsDocument.put(entry.getKey().getName(), kitDocument);
        }

        document.put("kitStatistics", kitStatisticsDocument);

        Document kitsDocument = new Document();

        for (Map.Entry<Kit, ProfileKitData> entry : kitData.entrySet()) {
            JsonArray kitsArray = new JsonArray();

            for (int i = 0; i < 4; i++) {
                KitLoadout loadout = entry.getValue().getLoadout(i);

                if (loadout != null) {
                    JsonObject kitObject = new JsonObject();
                    kitObject.addProperty("index", i);
                    kitObject.addProperty("name", loadout.getCustomName());
                    kitObject.addProperty("armor", BukkitSerialization.itemStackArrayToBase64(loadout.getArmor()));
                    kitObject.addProperty("contents", BukkitSerialization.itemStackArrayToBase64(loadout.getContents()));
                    kitsArray.add(kitObject);
                }
            }

            kitsDocument.put(entry.getKey().getName(), kitsArray.toString());
        }

        document.put("loadouts", kitsDocument);


        collection.replaceOne(Filters.eq("uuid", uuid.toString()), document, new ReplaceOptions().upsert(true));
    }

    public static void init () {
        collection = PotPvPRP.getInstance().getMongoDatabase().getCollection("profiles");
        // Players might have joined before the plugin finished loading
        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = new Profile(player.getUniqueId());

            try {
                profile.load();
            } catch (Exception e) {
                player.kickPlayer(CC.RED + "The server is loading...");
                continue;
            }

            profiles.put(player.getUniqueId(), profile);
        }
    }


    public static Profile getByUuid(UUID uuid) {
        Profile profile = profiles.get(uuid);

        if (profile == null) {
            Common.debug(CC.RED + "Unable to find perfil for uuid: " + uuid);
            return null;
        }

        return profile;
    }
}
