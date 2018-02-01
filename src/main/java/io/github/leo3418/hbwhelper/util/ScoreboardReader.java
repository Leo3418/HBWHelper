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
     * If a scoreboard exists, returns its title. Otherwise, returns an empty
     * string.
     *
     * @return the scoreboard's title or an empty string
     */
    public static String getTitle() {
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld
                .getScoreboard();
        ScoreObjective scoreboardTitle = scoreboard
                .getObjectiveInDisplaySlot(1);
        if (scoreboardTitle != null) {
            return scoreboardTitle.getDisplayName();
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
     * Checks if any line on the scoreboard matches a piece of text.
     *
     * @param text the text to be matched
     * @return {@code true} if any line matches the text, or {@code false}
     *         otherwise
     */
    public static boolean contains(String text) {
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld
                .getScoreboard();
        Collection<Score> scores = scoreboard.getScores();
        for (Score score : scores) {
            ScorePlayerTeam line = scoreboard
                    .getPlayersTeam(score.getPlayerName());
            String lineText = ScorePlayerTeam.formatPlayerName(line,
                    score.getPlayerName());
            if (lineText.contains(text)) {
                return true;
            }
        }
        return false;
    }
}
