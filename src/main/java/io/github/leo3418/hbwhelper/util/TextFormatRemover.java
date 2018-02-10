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

/**
 * Provides methods for removing formatting codes (e.g. {@code §4}, {@code §e},
 * and {@code §m}) in a piece of text.
 *
 * @author Leo
 */
public class TextFormatRemover {
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
    private TextFormatRemover() {
    }

    /**
     * Returns a copy of a piece of text whose formatting codes are removed.
     *
     * @param text the text whose formatting codes to be removed
     * @return a copy of a piece of text whose formatting codes are removed
     * @throws NullPointerException if the text is {@code null}
     */
    public static String removeAllFormats(String text) {
        return text.replaceAll(FORMATTING_REGEX, "");
    }
}
