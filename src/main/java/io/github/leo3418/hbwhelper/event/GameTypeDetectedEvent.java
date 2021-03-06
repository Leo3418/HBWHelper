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

package io.github.leo3418.hbwhelper.event;

import io.github.leo3418.hbwhelper.game.GameType;
import net.minecraftforge.eventbus.api.Event;

/**
 * The event fired when the {@link GameType} of the current Bed Wars game is
 * successfully detected.
 *
 * @author Leo
 */
public final class GameTypeDetectedEvent extends Event {
    /**
     * The {@link GameType} of the current Bed Wars game
     */
    private final GameType gameType;

    /**
     * Constructs a new {@code GameTypeDetectedEvent}.
     *
     * @param gameType the {@link GameType} of the current Bed Wars game
     */
    public GameTypeDetectedEvent(GameType gameType) {
        this.gameType = gameType;
    }

    /**
     * Returns the {@link GameType} of the current Bed Wars game.
     *
     * @return the {@link GameType} of the current Bed Wars game
     */
    public GameType getGameType() {
        return gameType;
    }
}
