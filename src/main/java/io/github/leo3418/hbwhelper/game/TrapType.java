/*
 * Copyright (C) 2018-2021 Leo3418 <https://github.com/Leo3418>
 *
 * This file is part of Hypixel Bed Wars Helper (HBW Helper).
 *
 * HBW Helper is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPL) as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * HBW Helper is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * Under section 7 of GPL version 3, you are granted additional
 * permissions described in the HBW Helper MC Exception.
 *
 * You should have received a copy of the GNU GPL and a copy of the
 * HBW Helper MC Exception along with this program's source code; see
 * the files LICENSE.txt and LICENSE-MCE.txt respectively.  If not, see
 * <http://www.gnu.org/licenses/> and
 * <https://github.com/Leo3418/HBWHelper>.
 */

package io.github.leo3418.hbwhelper.game;

/**
 * Enumeration of all types of trap in Hypixel Bed Wars.
 *
 * @author Leo
 */
public enum TrapType {
    /**
     * The ordinary "It's a trap!"
     */
    ORDINARY("It's a trap!"),
    /**
     * The "Counter-Offensive Trap"
     */
    COUNTER("Counter-Offensive Trap"),
    /**
     * The "Alarm Trap"
     * <p>
     * This value has an extra argument because Hypixel uses "Alarm Trap"
     * and "Alarm trap" at the same time.
     */
    ALARM("Alarm Trap", "Alarm trap"),
    /**
     * The "Miner Fatigue Trap"
     */
    MINER_FATIGUE("Miner Fatigue Trap");

    /**
     * Part of the prompt shown when the player's team purchases this trap
     */
    final String purchasePrompt;

    /**
     * Part of the prompt shown when this trap is set off
     */
    final String setOffPrompt;

    /**
     * Constructs a new constant of traps whose name is <b>consistent</b>
     * in Hypixel Bed Wars.
     *
     * @param name the name of this trap shown in any prompt in Hypixel
     *         Bed Wars without any formatting code
     */
    TrapType(String name) {
        this(name, name);
    }

    /**
     * Constructs a new constant of traps whose name is <b>inconsistent</b>
     * in Hypixel Bed Wars.
     *
     * @param purchaseName the name of this trap in the prompt shown when
     *         the player's team purchases this trap
     * @param setOffName the name of this trap in the prompt shown when
     *         it sets off
     */
    TrapType(String purchaseName, String setOffName) {
        this.purchasePrompt = "\u00A7r\u00A76" + purchaseName + "\u00A7r";
        this.setOffPrompt = "\u00A7c\u00A7l" + setOffName;
    }
}
