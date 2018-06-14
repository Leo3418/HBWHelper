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

package io.github.leo3418.hbwhelper.game;

/**
 * Wrapper of the {@link TrapType} enumeration which provides functionality for
 * counting remaining uses of a trap.
 * <p>
 * Implementation of this class was facilitated by Hypixel's introduction of the
 * Bed Wars Castle mode, in which a trap can be set off multiple times per
 * purchase. However, for other game modes in which a trap can only be used once
 * per purchase, this class also works well with {@code remainingUses} set to
 * {@code 1} when calling the {@link #CountedTrap(TrapType, int)} constructor.
 *
 * @author Leo
 */
public class CountedTrap {
    /**
     * Type of this trap
     */
    private final TrapType trapType;

    /**
     * Number of times this trap can be set off before it is cleared from the
     * trap queue
     */
    private int remainingUses;

    /**
     * Constructs a new {@code CountedTrap} instance.
     *
     * @param trapType the type of this trap
     * @param remainingUses number of times this trap can be set off
     */
    CountedTrap(TrapType trapType, int remainingUses) {
        this.trapType = trapType;
        this.remainingUses = remainingUses;
    }

    /**
     * Returns the type of this trap.
     *
     * @return the type of this trap
     */
    public TrapType getTrapType() {
        return trapType;
    }

    /**
     * Returns a copy of this object.
     *
     * @return a copy of this object
     */
    CountedTrap getCopy() {
        return new CountedTrap(this.trapType, this.remainingUses);
    }

    /**
     * Performs a set-off of this trap.
     */
    void setOff() {
        remainingUses--;
    }

    /**
     * Returns whether this trap has been used up and should be removed from the
     * trap queue.
     *
     * @return whether this trap has been used up and should be removed from the
     *         trap queue
     */
    boolean hasUsedUp() {
        return remainingUses <= 0;
    }
}
