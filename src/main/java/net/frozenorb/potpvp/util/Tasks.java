package net.frozenorb.potpvp.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.frozenorb.potpvp.PotPvPRP;
import org.bukkit.scheduler.BukkitScheduler;
import java.util.concurrent.ThreadFactory;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 10/06/2025
 * Project: Practice
 */
public class Tasks {

    public static ThreadFactory newThreadFactory(String name) {
        return new ThreadFactoryBuilder().setNameFormat(name).build();
    }

    public static void run(Runnable runnable, boolean async) {
        if(async) {
            PotPvPRP.getInstance().getServer().getScheduler().runTaskAsynchronously(PotPvPRP.getInstance(), runnable);
        } else {
            runnable.run();
        }
    }

    public static void run(Runnable runnable) {
        PotPvPRP.getInstance().getServer().getScheduler().runTask(PotPvPRP.getInstance(), runnable);
    }

    public static void runAsync(Runnable runnable) {
       PotPvPRP.getInstance().getServer().getScheduler().runTaskAsynchronously(PotPvPRP.getInstance(), runnable);
    }

    public static void runLater(Runnable runnable, long delay) {
        PotPvPRP.getInstance().getServer().getScheduler().runTaskLater(PotPvPRP.getInstance(), runnable, delay);
    }

    public static void runAsyncLater(Runnable runnable, long delay) {
       PotPvPRP.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(PotPvPRP.getInstance(), runnable, delay);
    }

    public static void runTimer(Runnable runnable, long delay, long interval) {
        PotPvPRP.getInstance().getServer().getScheduler().runTaskTimer(PotPvPRP.getInstance(), runnable, delay, interval);
    }

    public static void runAsyncTimer(Runnable runnable, long delay, long interval) {
       PotPvPRP.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(PotPvPRP.getInstance(), runnable, delay, interval);
    }

    public static BukkitScheduler getScheduler() {
        return PotPvPRP.getInstance().getServer().getScheduler();
    }
}
