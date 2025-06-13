package dev.stone.practice.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.stone.practice.Phantom;
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
            Phantom.getInstance().getServer().getScheduler().runTaskAsynchronously(Phantom.getInstance(), runnable);
        } else {
            runnable.run();
        }
    }

    public static void run(Runnable runnable) {
        Phantom.getInstance().getServer().getScheduler().runTask(Phantom.getInstance(), runnable);
    }

    public static void runAsync(Runnable runnable) {
       Phantom.getInstance().getServer().getScheduler().runTaskAsynchronously(Phantom.getInstance(), runnable);
    }

    public static void runLater(Runnable runnable, long delay) {
        Phantom.getInstance().getServer().getScheduler().runTaskLater(Phantom.getInstance(), runnable, delay);
    }

    public static void runAsyncLater(Runnable runnable, long delay) {
       Phantom.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(Phantom.getInstance(), runnable, delay);
    }

    public static void runTimer(Runnable runnable, long delay, long interval) {
        Phantom.getInstance().getServer().getScheduler().runTaskTimer(Phantom.getInstance(), runnable, delay, interval);
    }

    public static void runAsyncTimer(Runnable runnable, long delay, long interval) {
       Phantom.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Phantom.getInstance(), runnable, delay, interval);
    }

    public static BukkitScheduler getScheduler() {
        return Phantom.getInstance().getServer().getScheduler();
    }
}
