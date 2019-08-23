/*
 * HBW Helper: Hypixel Bed Wars Helper Minecraft Forge Mod
 * Copyright (C) 2019 Leo3418
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

import io.github.leo3418.hbwhelper.EventManager;
import io.github.leo3418.hbwhelper.event.GameTypeDetectedEvent;
import io.github.leo3418.hbwhelper.util.ScoreboardReader;

/**
 * Detects the {@link GameType} of the current Bed Wars game.
 * <p>
 * This is a Singleton class. Only one instance of this class may be created
 * per runtime.
 *
 * @author Leo
 */
public class GameTypeDetector {
    /**
     * Text that appears on scoreboard only when client is in a Bed Wars game
     * (except in Capture)
     */
    private static final String GAME_SCOREBOARD_TEXT = "R Red:";

    /**
     * Text that appears on scoreboard only when client is in a Bed Wars Capture
     * game
     */
    private static final String CAPTURE_SCOREBOARD_TEXT = "Draw in";

    /**
     * Text that appears on scoreboard only when client is in Bed Wars Rush
     * Mode at the beginning of the game
     */
    private static final String RUSH_SCOREBOARD_TEXT = "Bed gone in";

    /**
     * Text that appears on scoreboard only when client is in Bed Wars Castle
     * Mode at the beginning of the game
     */
    private static final String CASTLE_SCOREBOARD_TEXT = "Streak Points:";

    /**
     * The only instance of this class
     */
    private static final GameTypeDetector INSTANCE = new GameTypeDetector();

    /**
     * Whether this object should detect the current game type
     */
    private boolean shouldDetect;

    /**
     * Implementation of Singleton design pattern, which allows only one
     * instance of this class to be created.
     */
    private GameTypeDetector() {
    }

    /**
     * Returns the instance of this class.
     *
     * @return the instance of this class
     */
    public static GameTypeDetector getInstance() {
        return INSTANCE;
    }

    /**
     * Starts detection of game type.
     * <p>
     * This method should be called whenever a
     * {@link io.github.leo3418.hbwhelper.event.GameStartEvent} or
     * {@link io.github.leo3418.hbwhelper.event.ClientLeaveGameEvent} is fired.
     */
    public void startDetection() {
        shouldDetect = true;
    }

    /**
     * If a detection has been started, attempts to find the current game type.
     * Otherwise, does nothing.
     * <p>
     * If the type is confirmed, fires a {@link GameTypeDetectedEvent} on this
     * mod's {@link EventManager#EVENT_BUS proprietary event bus}.
     * <p>
     * Calling this method when the client is not in a Minecraft world
     * (e.g. in the main menu) after invocation of {@link #startDetection()}
     * method can produce {@link NullPointerException}. When the client leaves
     * the Minecraft world, immediately call the {@link #stopDetection()}
     * method, so execution of this method will no longer produce
     * {@code NullPointerException} until {@link #startDetection()} is called
     * again.
     * <p>
     * This method should be called whenever a
     * {@link net.minecraftforge.event.TickEvent.ClientTickEvent
     * ClientTickEvent} is fired.
     */
    public void detect() {
        if (shouldDetect) {
            // An extra check runs here to ensure that the scoreboard is fully
            // loaded with the information needed to determine the game type
            if (ScoreboardReader.contains(GAME_SCOREBOARD_TEXT) ||
                    ScoreboardReader.contains(CAPTURE_SCOREBOARD_TEXT)) {
                EventManager.EVENT_BUS.post(
                        new GameTypeDetectedEvent(getGameType()));
                stopDetection();
            }
        }
    }

    /**
     * Stops detection of game type.
     */
    public void stopDetection() {
        shouldDetect = false;
    }

    /**
     * Returns the current {@link GameType} inferred from scoreboard.
     *
     * @return the current {@code GameType} inferred from scoreboard
     */
    private GameType getGameType() {
        if (ScoreboardReader.contains(RUSH_SCOREBOARD_TEXT)) {
            return GameType.RUSH;
        } else if (ScoreboardReader.contains(CASTLE_SCOREBOARD_TEXT)) {
            return GameType.CASTLE;
        } else {
            return GameType.NORMAL;
        }
    }
}
