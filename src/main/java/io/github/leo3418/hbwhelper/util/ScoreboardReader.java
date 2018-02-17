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
     * Prevents instantiation of this class.
     */
    private ScoreboardReader() {
    }

    /**
     * Returns a scoreboard's title if the scoreboard exists, or an empty
     * string otherwise.
     *
     * @param removeFormat whether or not to remove format from the title
     * @return a scoreboard's title if the scoreboard exists, or an empty
     *         string otherwise
     */
    public static String getTitle(boolean removeFormat) {
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld
                .getScoreboard();
        ScoreObjective scoreboardTitle = scoreboard
                .getObjectiveInDisplaySlot(1);
        if (scoreboardTitle != null) {
            String title = scoreboardTitle.getDisplayName();
            if (removeFormat) {
                title = TextFormatRemover.removeAllFormats(title);
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
        Collection<Score> scores = Minecraft.getMinecraft().theWorld
                .getScoreboard().getScores();
        return scores.size();
    }

    /**
     * Returns whether any line on the scoreboard matches a piece of text.
     *
     * @param text the text to be matched
     * @param removeFormat whether or not to remove format from lines on the
     *         scoreboard
     * @return whether any line on the scoreboard matches a piece of text.
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
     * Returns whether any line on the scoreboard matches a regular expression.
     *
     * @param regex the regular expression to be matched
     * @param removeFormat whether or not to remove format from lines on the
     *         scoreboard
     * @return whether any line on the scoreboard matches a regular expression
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
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld
                .getScoreboard();
        Collection<Score> scores = scoreboard.getScores();
        Collection<String> lines = new ArrayList<String>(getSize());
        for (Score score : scores) {
            ScorePlayerTeam line = scoreboard
                    .getPlayersTeam(score.getPlayerName());
            String lineText = ScorePlayerTeam.formatPlayerName(line,
                    score.getPlayerName());
            if (removeFormat) {
                lineText = TextFormatRemover.removeAllFormats(lineText);
            }
            lines.add(lineText);
        }
        return lines;
    }
}
