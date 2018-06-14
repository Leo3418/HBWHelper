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

import io.github.leo3418.hbwhelper.event.TickCounterTimeUpEvent;
import io.github.leo3418.hbwhelper.util.ScoreboardReader;
import io.github.leo3418.hbwhelper.util.TickCounter;

/**
 * Detects the {@link GameType} of the current Bed Wars game.
 * <p>
 * Like some other classes under this package, this class is designed <b>to be
 * used only when the client is in a Minecraft world</b>. Calling some methods
 * when the client is not in a Minecraft world (e.g. in the main menu) might
 * produce {@link NullPointerException}.
 *
 * @author Leo
 */
public class GameTypeDetector {
    /**
     * Length of postponement to read scoreboard after client joins a game,
     * whose unit is second
     */
    private static final double READ_POSTPONEMENT = 2.0;

    /**
     * {@link TickCounter} which defers reading of scoreboard
     */
    private static final TickCounter TICK_COUNTER =
            new TickCounter(READ_POSTPONEMENT);

    /**
     * Text that appears on scoreboard only when client is in Bed Wars Castle
     * mode
     */
    private static final String CASTLE_SCOREBOARD_TEXT = "Streak Points:";

    /**
     * Prevents instantiation of this class.
     */
    private GameTypeDetector() {
    }

    /**
     * Prepares to read scoreboard.
     * <p>
     * Because scoreboard might not be available for reading immediately after
     * a game starts, client rejoins a game, or client joins an in-progress
     * game, this method was implemented to postpone reading of scoreboard.
     */
    public static void prepareToReadScoreboard() {
        TICK_COUNTER.reset();
        TICK_COUNTER.start();
    }

    /**
     * Returns whether the {@link TickCounter} expiring in a
     * {@link TickCounterTimeUpEvent} is the {@code TickCounter} initialized by
     * this class.
     *
     * @param event the event fired when a tick counter times up
     * @return whether the {@code TickCounter} expiring in the
     *         {@code TickCounterTimeUpEvent} is the {@code TickCounter}
     *         initialized by this class
     */
    public static boolean isTickCounterTimingUp(TickCounterTimeUpEvent event) {
        return event.getExpiringCounter() == TICK_COUNTER;
    }

    /**
     * Stops the {@link TickCounter} created by this class.
     */
    public static void stopTickCounter() {
        TICK_COUNTER.stop();
    }

    /**
     * Returns the current {@link GameType}.
     *
     * @return the current {@link GameType}
     */
    public static GameType getGameType() {
        if (ScoreboardReader.contains(CASTLE_SCOREBOARD_TEXT)) {
            return GameType.CASTLE;
        } else {
            return GameType.NORMAL;
        }
    }
}
