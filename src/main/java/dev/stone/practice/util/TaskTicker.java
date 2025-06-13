package dev.stone.practice.util;

import lombok.Getter;
import lombok.Setter;
import dev.stone.practice.Phantom;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.ArrayList;
import java.util.List;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 09/06/2025
 * Project: PotPvpReprised
 */

public abstract class TaskTicker extends BukkitRunnable {

    @Getter
    private static final List<TaskTicker> tickers = new ArrayList<>();

    @Getter
    @Setter
    private int ticks;
    private boolean finishPreRun = false;

    public TaskTicker(int delay, int period, boolean async) {
        if (async) {
            this.runTaskTimerAsynchronously(Phantom.getInstance(), delay, period);
        } else {
            this.runTaskTimer(Phantom.getInstance(), delay, period);
        }
        tickers.add(this);
    }

    @Override
    public void run() {
        if (!finishPreRun) {
            ticks = getStartTick();
            preRun();
            finishPreRun = true;
        }
        onRun();
        if (getTickType() == TickType.COUNT_UP) {
            countUp();
        } else if (getTickType() == TickType.COUNT_DOWN) {
            countDown();
        }
    }

    public abstract void onRun();

    public void preRun() {

    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();

        tickers.remove(this);
    }

    public abstract TickType getTickType();

    public abstract int getStartTick();

    public void countUp() {
        ticks++;
    }

    public void countDown() {
        ticks--;
    }

    public enum TickType {
        COUNT_UP,
        COUNT_DOWN,
        NONE
    }

}
