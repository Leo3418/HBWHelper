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
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.regex.Pattern;

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
     * Pattern of regular expression of formatting codes in Minecraft
     * <p>
     * I have to use the Unicode encoding for the section sign ({@code §})
     * because Minecraft is so mean that it does not want the player to use
     * this sign in game to produce formatted text, even if the player is
     * merely using it in a mod's code.
     *
     * @see <a href="https://minecraft.gamepedia.com/Formatting_codes#Use_in_server.properties_and_pack.mcmeta"
     *         target="_top">Relevant information on Minecraft Wiki</a>
     */
    private static final Pattern FORMATTING_PATTERN =
            Pattern.compile("\u00A7[0-9a-fk-or]");

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
     * Returns the size of the scoreboard.
     *
     * @return the size of the scoreboard
     */
    private static int getSize() {
        return Minecraft.getMinecraft().world.getScoreboard().getScores()
                .size();
    }

    /**
     * Returns a {@link Collection} of all lines on the scoreboard without
     * formatting codes.
     *
     * @return a {@code Collection} of all lines on the scoreboard without
     *         formatting codes
     * @see #FORMATTING_PATTERN Pattern of regular expression of formatting
     *         codes in Minecraft
     */
    private static Collection<String> getLines() {
        Scoreboard scoreboard =
                Objects.requireNonNull(Minecraft.getMinecraft().world)
                        .getScoreboard();
        Collection<Score> scores = scoreboard.getScores();
        Collection<String> lines = new ArrayList<>(getSize());
        for (Score score : scores) {
            ScorePlayerTeam line = scoreboard
                    .getPlayersTeam(score.getPlayerName());
            String lineText = FORMATTING_PATTERN
                    .matcher(ScorePlayerTeam.formatPlayerName(line,
                            score.getPlayerName()))
                    .replaceAll("");
            lines.add(lineText);
        }
        return lines;
    }
}
