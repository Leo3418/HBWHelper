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

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Provides methods for reading the scoreboard and getting information from it.
 *
 * @author Leo
 */
public class ScoreboardReader {
    /**
     * Regular expression of formatting codes in Minecraft
     * <p>
     * I have to use the Unicode encoding for the section sign ({@code §})
     * because Minecraft is so mean that it does not want the player to use
     * this sign in game to produce formatted text, even if the player is
     * merely using it in a mod's code.
     *
     * @see <a href="https://minecraft.gamepedia.com/Formatting_codes"
     *         target="_top">Formatting codes in Minecraft</a>
     * @see <a href="https://en.wikipedia.org/wiki/Section_sign">
     *         The section sign's information on Wikipedia</a>
     */
    private static final String FORMATTING_REGEX = "\u00A7[0-9a-fk-or]";

    /**
     * Prevents instantiation of this class.
     */
    private ScoreboardReader() {
    }

    /**
     * If a scoreboard exists, returns its title. Otherwise, returns an empty
     * string.
     *
     * @param removeFormat whether or not to remove format from the title
     * @return the scoreboard's title or an empty string
     */
    public static String getTitle(boolean removeFormat) {
        Scoreboard scoreboard = Minecraft.getMinecraft().world.getScoreboard();
        ScoreObjective scoreboardTitle = scoreboard
                .getObjectiveInDisplaySlot(1);
        if (scoreboardTitle != null) {
            String title = scoreboardTitle.getDisplayName();
            if (removeFormat) {
                title = title.replaceAll(FORMATTING_REGEX, "");
            }
            return title;
        }
        return "";
    }

    /**
     * Returns the size of the scoreboard.
     *
     * @return the size of the scoreboard
     */
    public static int getSize() {
        Collection<Score> scores = Minecraft.getMinecraft().world
                .getScoreboard().getScores();
        return scores.size();
    }

    /**
     * Checks if any line on the scoreboard matches a piece of text.
     *
     * @param text the text to be matched
     * @param removeFormat whether or not to remove format from lines on the
     *         scoreboard
     * @return {@code true} if any line matches the text, or {@code false}
     *         otherwise
     */
    public static boolean contains(String text, boolean removeFormat) {
        Collection<String> lines = getLines(removeFormat);
        for (String line : lines) {
            if (line.contains(text)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if any line on the scoreboard matches a regular expression.
     *
     * @param regex the regular expression to be matched
     * @param removeFormat whether or not to remove format from lines on the
     *         scoreboard
     * @return {@code true} if any line matches the regular expression, or
     *         {@code false} otherwise
     */
    public static boolean containsRegex(String regex, boolean removeFormat) {
        Collection<String> lines = getLines(removeFormat);
        for (String line : lines) {
            if (line.matches(regex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a {@link Collection} of all lines on the scoreboard.
     *
     * @param removeFormat whether or not to remove format from the lines
     * @return a {@link Collection} of all lines on the scoreboard.
     */
    private static Collection<String> getLines(boolean removeFormat) {
        Scoreboard scoreboard = Minecraft.getMinecraft().world.getScoreboard();
        Collection<Score> scores = scoreboard.getScores();
        Collection<String> lines = new ArrayList<>(getSize());
        for (Score score : scores) {
            ScorePlayerTeam line = scoreboard
                    .getPlayersTeam(score.getPlayerName());
            String lineText = ScorePlayerTeam.formatPlayerName(line,
                    score.getPlayerName());
            if (removeFormat) {
                lineText = lineText.replaceAll(FORMATTING_REGEX, "");
            }
            lines.add(lineText);
        }
        return lines;
    }
}
