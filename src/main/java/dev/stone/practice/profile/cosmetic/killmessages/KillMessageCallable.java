package dev.stone.practice.profile.cosmetic.killmessages;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 23/06/2025
 * Project: Practice
 */
public interface KillMessageCallable {
    public String getFormatted(String var1, String var2, boolean var3);

    public List<String> getMessages();

    public List<String> getDescription();

    default public List<String> getFormattedLore() {
        ArrayList<String> stringList = new ArrayList<String>(this.getDescription());
        stringList.add(" ");
        this.getMessages().forEach(message -> stringList.add(ChatColor.GRAY + "... was " + "&b" + message + ChatColor.GRAY + " by ..."));
        stringList.add(" ");
        stringList.add(ChatColor.WHITE.toString() + "One of these messages will");
        stringList.add(ChatColor.WHITE.toString() + "appear when you kill someone.");
        return stringList;
    }
}
