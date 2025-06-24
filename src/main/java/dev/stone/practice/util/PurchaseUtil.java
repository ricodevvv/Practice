package dev.stone.practice.util;

import dev.stone.practice.profile.Profile;
import dev.stone.practice.profile.cosmetic.killmessages.KillMessages;
import dev.stone.practice.profile.cosmetic.killeffect.SpecialEffects;
import dev.stone.practice.util.menu.Button;
import org.bukkit.entity.Player;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 23/06/2025
 * Project: Practice
 */
public class PurchaseUtil {

    public static void purchaseItem(Player player, Object i) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (i instanceof SpecialEffects) {
            SpecialEffects item = (SpecialEffects) i;
            if (profile.getCoins() >= item.getPrice()) {
                profile.setCoins(profile.getCoins() - item.getPrice());
                profile.addPermission(item.getPermission());
                player.sendMessage(CC.translate("&aPurchased the " + item.getName() + " kill effect!"));
                Button.playSuccess(player);
            } else {
                player.sendMessage(CC.translate("&cYou don't have enough coins."));
                Button.playFail(player);
            }
        } if (i instanceof KillMessages) {
            KillMessages item = (KillMessages) i;
            if (profile.getCoins() >= item.getPrice()) {
                profile.setCoins(profile.getCoins() - item.getPrice());
                profile.addPermission(item.getPermission());
                player.sendMessage(CC.translate("&aPurchased the " + item.getName() + " kill message!"));
                Button.playSuccess(player);
            } else {
                player.sendMessage(CC.translate("&cYou don't have enough coins."));
                Button.playFail(player);
            }
        }
    }

}
