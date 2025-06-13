package dev.stone.practice.util.uuid.listener;

import dev.stone.practice.Phantom;
import dev.stone.practice.util.uuid.UUIDCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public final class UUIDListener implements Listener {

    private Phantom plugin = Phantom.getInstance();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (plugin.getUuidCache().cached(event.getUniqueId())) {
            plugin.getUuidCache().update(event.getUniqueId(), event.getName());
        } else {
            plugin.getUuidCache().updateAll(event.getUniqueId(), event.getName());
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().isOp() || UUIDCache.MONITOR_CACHE.containsKey(event.getPlayer().getUniqueId())) {
            return;
        }

        UUIDCache.MONITOR_CACHE.put(event.getPlayer().getUniqueId(), true);
    }

}