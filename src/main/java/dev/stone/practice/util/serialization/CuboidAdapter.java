package dev.stone.practice.util.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import dev.stone.practice.util.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.lang.reflect.Type;

/**
 * Gson adapter to serialize/deserialize Cuboid.
 */
public class CuboidAdapter implements JsonSerializer<Cuboid>, JsonDeserializer<Cuboid> {

    @Override
    public JsonElement serialize(Cuboid src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) return null;
        JsonObject o = new JsonObject();
        o.addProperty("worldName", src.getWorld().getName());
        o.addProperty("x1", src.getLowerX());
        o.addProperty("y1", src.getLowerY());
        o.addProperty("z1", src.getLowerZ());
        o.addProperty("x2", src.getUpperX());
        o.addProperty("y2", src.getUpperY());
        o.addProperty("z2", src.getUpperZ());
        return o;
    }

    @Override
    public Cuboid deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || !json.isJsonObject()) return null;
        JsonObject o = json.getAsJsonObject();
        World world = Bukkit.getWorld(o.get("worldName").getAsString());
        int x1 = o.get("x1").getAsInt();
        int y1 = o.get("y1").getAsInt();
        int z1 = o.get("z1").getAsInt();
        int x2 = o.get("x2").getAsInt();
        int y2 = o.get("y2").getAsInt();
        int z2 = o.get("z2").getAsInt();
        return new Cuboid(world, x1, y1, z1, x2, y2, z2);
    }
}


