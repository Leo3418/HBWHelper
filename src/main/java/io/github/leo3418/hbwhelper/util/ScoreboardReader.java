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

package io.github.leo3418.hbwhelper.util;

import net.minecraft.client.Minecraft;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Provides methods for reading the scoreboard and getting information from it.
 * <p>
 * Like some other classes under this package, this class is designed <b>to be
 * used only when the client is in a Minecraft world</b>. Calling some methods
 * when the client is not in a Minecraft world (e.g. in the main menu) might
 * produce {@link NullPointerException}.
 *
 * @author Leo
 */
public class ScoreboardReader {
    /**
     * Prevents instantiation of this class.
     */
    private ScoreboardReader() {
    }

    /**
     * Returns whether any line on the scoreboard contains a piece of text.
     * <p>
     * The formatting codes of all lines on the scoreboard will be removed for
     * the sake of this method.
     *
     * @param text the text to be matched
     * @return whether any line on the scoreboard contains a piece of text.
     */
    public static boolean contains(String text) {
        Collection<String> lines = getLines();
        for (String line : lines) {
            if (line.contains(text)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a {@link Collection} of all lines on the scoreboard without
     * formatting codes.
     *
     * @return a {@code Collection} of all lines on the scoreboard without
     *         formatting codes
     */
    private static Collection<String> getLines() {
        return Objects.requireNonNull(Minecraft.getInstance().level)
                .getScoreboard().getPlayerTeams().stream()
                .map(team -> team.getPlayerPrefix().getString()
                        + team.getPlayerSuffix().getString())
                .collect(Collectors.toList());
    }
}
