/*
 * HBW Helper: Hypixel Bed Wars Helper Minecraft Forge Mod
 * Copyright (C) 2018 Leo3418
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.leo3418.hbwhelper.util;

import io.github.leo3418.hbwhelper.EventManager;
import io.github.leo3418.hbwhelper.event.TickCounterTimeUpEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

/**
 * Counts number of ticks happened in client.
 * <p>
 * A {@code TickCounter} can update itself automatically, but it
 * can also be manually started or stopped.
 * <p>
 * A {@code TickCounter} can also act as a timer of ticks.
 * <p>
 * This class also provides methods for converting ticks to seconds.
 *
 * @author Leo
 */
public class TickCounter {
    /**
     * Number of ticks per second in Minecraft Forge.
     * <p>
     * Note that this is different from game tick in Minecraft.
     */
    public static final int TICKS_PER_SECOND = 40;

    /**
     * Whether this {@code TickCounter} is running
     */
    private boolean running;

    /**
     * Ticks since creation of this {@code TickCounter}
     */
    private int tick;

    /**
     * Number of ticks set for timer
     */
    private int timer;

    /**
     * Constructs a new {@code TickCounter}, and starts counting ticks.
     */
    public TickCounter() {
        // Number of ticks starts from 0 and keeps increasing, so it will never
        // be negative
        this(-1);
    }

    /**
     * Constructs a new {@code TickCounter} with a timer which fires a
     * {@link TickCounterTimeUpEvent} when specified number of seconds goes by.
     *
     * @param seconds the number of seconds before the event is fired
     * @see TickCounterTimeUpEvent
     */
    public TickCounter(double seconds) {
        this((int) Math.round(seconds * TICKS_PER_SECOND));
    }

    /**
     * Constructs a new {@code TickCounter} with a timer which fires a
     * {@link TickCounterTimeUpEvent} when specified number of ticks goes by.
     *
     * @param ticks the number of ticks before the event is fired
     * @see TickCounterTimeUpEvent
     */
    public TickCounter(int ticks) {
        timer = ticks;
        start();
    }

    /**
     * If the counter is stopped, starts it. Otherwise, nothing is done.
     */
    public void start() {
        if (!running) {
            MinecraftForge.EVENT_BUS.register(this);
            running = true;
        }
    }

    /**
     * If the counter is running, stops it. Otherwise, nothing is done.
     */
    public void stop() {
        if (running) {
            MinecraftForge.EVENT_BUS.unregister(this);
            running = false;
        }
    }

    /**
     * Resets the counter. The counter will keep running or being stopped.
     */
    public void reset() {
        tick = 0;
    }

    /**
     * Advances one tick. If the timer expires, fires a
     * {@link TickCounterTimeUpEvent} on this mod's event bus.
     *
     * @param event the event fired when client ticks
     * @see TickCounterTimeUpEvent
     * @see EventManager#EVENT_BUS
     */
    @SubscribeEvent
    public void tick(ClientTickEvent event) {
        tick++;
        if (tick == timer) {
            EventManager.EVENT_BUS.post(new TickCounterTimeUpEvent(this));
        }
    }

    /**
     * Returns ticks since creation of this {@code TickCounter}.
     *
     * @return ticks since creation of this {@code TickCounter}
     */
    public int getTick() {
        return tick;
    }

    /**
     * Returns seconds since creation of this {@code TickCounter}.
     * <p>
     * If it has been 39 ticks, this method returns <b>0</b>.<br>
     * If it has been 40 ticks, this method returns <b>1</b>.<br>
     * If it has been 41 ticks, this method returns <b>1</b>.<br>
     *
     * @return seconds since creation of this {@code TickCounter}
     */
    public int getSeconds() {
        return tick / TICKS_PER_SECOND;
    }

    /**
     * Returns exact value of seconds since creation of this {@code TickCounter}.
     * <p>
     * If it has been 39 ticks, this method returns <b>0.975</b>.<br>
     * If it has been 40 ticks, this method returns <b>1.0</b>.<br>
     * If it has been 41 ticks, this method returns <b>1.025</b>.<br>
     *
     * @return seconds since creation of this {@code TickCounter}
     */
    public double getExactSeconds() {
        return 1.0 * tick / TICKS_PER_SECOND;
    }
}
