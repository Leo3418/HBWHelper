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

package io.github.leo3418.hbwhelper.event;

import io.github.leo3418.hbwhelper.util.TickCounter;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * The event fired when a {@link TickCounter} with timer expires.
 * <p>
 * Because there might be more than one {@code TickCounter} created, it is
 * important to know which {@code TickCounter} fired this event. The following
 * code demonstrates how to check if this event is fired by a specified
 * {@code TickCounter}. Assume that the {@code TickCounter} is called
 * {@code tickCounter}.
 * <blockquote><pre>{@code
 *      \u0040SubscribeEvent
 *      public void onTickCounterTimeUp(TickCounterTimeUpEvent event) {
 *          if (event.getExpiringCounter() == tickCounter) {
 *              // The event is fired by tickCounter
 *          }
 *      }
 * }</pre></blockquote>
 *
 * @author Leo
 * @see TickCounter
 */
public class TickCounterTimeUpEvent extends Event {
    /**
     * The {@code TickCounter} object which fires this event
     */
    private final TickCounter expiringCounter;

    /**
     * Constructs a new {@code TickCounterTimeUpEvent}.
     *
     * @param expiringCounter the {@code TickCounter} object which fires this event
     */
    public TickCounterTimeUpEvent(TickCounter expiringCounter) {
        this.expiringCounter = expiringCounter;
    }

    /**
     * Returns the {@code TickCounter} object which fires this event.
     *
     * @return the {@code TickCounter} object which fires this event
     */
    public TickCounter getExpiringCounter() {
        return expiringCounter;
    }
}
