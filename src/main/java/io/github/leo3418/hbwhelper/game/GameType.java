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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Enumeration of different types of Bed Wars game on Hypixel. These types are
 * differentiated by initial team upgrades and how team upgrades are specially
 * processed, e.g. how many times a trap can be used.
 *
 * @author Leo
 */
public enum GameType {
    /**
     * Normal game modes in which no team upgrades will be unlocked when the
     * game starts, including:
     * <ul>
     * <li>Solo, Doubles, 3v3v3v3, 4v4v4v4</li>
     * <li>4v4</li>
     * <li>Ultimate</li>
     * <li>Lucky Blocks</li>
     * <li>Voidless</li>
     * </ul>
     */
    NORMAL(ForgeLevel.ORDINARY_FORGE, 1, Constants.EMPTY_INITIAL_TRAP_QUEUE),
    /**
     * Bed Wars Rush mode, in which resource generation speed level on every
     * team's base is "Molten Forge"
     */
    RUSH(ForgeLevel.MOLTEN_FORGE, 1, Constants.EMPTY_INITIAL_TRAP_QUEUE),
    /**
     * Bed Wars Castle mode, in which three Alarm Traps will be unlocked
     * when the game starts, and each trap can be used for multiple times
     */
    CASTLE(ForgeLevel.ORDINARY_FORGE, Constants.CASTLE_TRAP_USES,
            Constants.CASTLE_INITIAL_TRAP_QUEUE);

    /**
     * Initial resource generation level
     */
    final ForgeLevel initialForge;

    /**
     * Number of times a trap can be triggered before it is removed from the
     * trap queue
     */
    final int trapUses;

    /**
     * {@link List} of traps in the initial trap queue of this game type
     * <p>
     * Traps in this {@code List} will be added to the local-cached trap queue
     * once the game starts.
     */
    final List<CountedTrap> initialTrapQueue;

    /**
     * Constructs a new constant of game type.
     *
     * @param initialForge the initial resource generation level of this
     *         game type
     * @param trapUses the number of types a trap can be triggered before it
     *         is removed from the trap queue in this game type
     * @param initialTrapQueue the {@link List} of traps in the initial trap
     *         queue
     */
    GameType(ForgeLevel initialForge, int trapUses,
             List<CountedTrap> initialTrapQueue) {
        this.initialForge = initialForge;
        this.trapUses = trapUses;
        this.initialTrapQueue = initialTrapQueue;
    }

    /**
     * Stores static final fields (constants) for this enum class.
     */
    private static class Constants {
        /**
         * Number of times a trap can be triggered in Bed Wars Castle mode
         * before it is removed from the trap queue
         */
        private static final int CASTLE_TRAP_USES = 5;

        /**
         * {@link List} of traps in an empty initial trap queue, which is an
         * empty {@code List}
         */
        private static final List<CountedTrap> EMPTY_INITIAL_TRAP_QUEUE =
                Collections.emptyList();

        /**
         * {@link List} of traps in the initial trap queue of Bed Wars Castle
         * mode
         */
        private static final List<CountedTrap> CASTLE_INITIAL_TRAP_QUEUE;

        static {
            List<CountedTrap> castleInitialTrapQueue =
                    new ArrayList<>(GameManager.MAX_TRAPS);
            for (int i = 0; i < GameManager.MAX_TRAPS; i++) {
                castleInitialTrapQueue.add(
                        new CountedTrap(TrapType.ALARM, CASTLE_TRAP_USES));
            }
            CASTLE_INITIAL_TRAP_QUEUE =
                    Collections.unmodifiableList(castleInitialTrapQueue);
        }
    }
}
