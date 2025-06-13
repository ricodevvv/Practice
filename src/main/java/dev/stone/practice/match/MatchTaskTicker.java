package dev.stone.practice.match;

import dev.stone.practice.util.TaskTicker;

/**
 * This Project is property of Desroyed Development © 2025
 * Redistribution of this Project is not allowed
 *
 * @author ricadev
 * Created: 11/06/2025
 * Project: Practice
 */
public abstract class MatchTaskTicker extends TaskTicker {

    private final Match match;

    public MatchTaskTicker(int delay, int period, boolean async, Match match) {
        super(delay, period, async);

        this.match = match;

        match.getTasks().add(this);
    }

    @Override
    public abstract void onRun();

    @Override
    public abstract void preRun();

    @Override
    public abstract TickType getTickType();

    @Override
    public abstract int getStartTick();

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        if (match.getState() == MatchState.FIGHTING) {
            match.getTasks().remove(this);
        }
    }
}
