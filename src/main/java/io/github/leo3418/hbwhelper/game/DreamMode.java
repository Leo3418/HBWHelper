/*
 * Copyright (C) 2018-2020 Leo3418 <https://github.com/Leo3418>
 *
 * This file is part of Hypixel Bed Wars Helper (HBW Helper).
 *
 * HBW Helper is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * HBW Helper is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.leo3418.hbwhelper.game;

import net.minecraft.client.resources.I18n;

import java.util.*;

/**
 * Enumeration of Bed Wars Dream mode games on Hypixel.
 *
 * @author Leo
 */
public enum DreamMode {
    /**
     * Dummy constant which represents that the user has not selected the
     * current Dream mode on Hypixel, or the previous selection is no longer
     * valid
     */
    UNSELECTED(I18n.format("hbwhelper.configGui.unselected")),
    /**
     * Rush mode
     */
    RUSH(I18n.format("hbwhelper.dream.rush")),
    /**
     * Ultimate mode
     */
    ULTIMATE(I18n.format("hbwhelper.dream.ultimate")),
    /**
     * Castle mode
     */
    CASTLE(I18n.format("hbwhelper.dream.castle")),
    /**
     * Lucky Blocks mode
     */
    LUCKY_BLOCKS(I18n.format("hbwhelper.dream.luckyBlocks")),
    /**
     * Voidless mode
     */
    VOIDLESS(I18n.format("hbwhelper.dream.voidless")),
    /**
     * Armed mode
     */
    ARMED(I18n.format("hbwhelper.dream.armed"));

    /**
     * Cache for better performance when querying a constant of Dream mode
     * games using its display name and when getting the set of display names of
     * every constant
     */
    private static final Map<String, DreamMode> DISPLAY_NAMES_MAP;

    /**
     * Unmodifiable {@link Collection} containing display names of every
     * constants of this enumeration, in the order their corresponding constants
     * are declared
     */
    private static final Collection<String> VALUE_NAMES;

    static {
        DISPLAY_NAMES_MAP = new LinkedHashMap<>();
        for (DreamMode dreamMode : DreamMode.values()) {
            DISPLAY_NAMES_MAP.put(dreamMode.displayName, dreamMode);
        }
        VALUE_NAMES = Collections.unmodifiableCollection(
                DISPLAY_NAMES_MAP.keySet());
    }

    /**
     * Name of this Dream mode game displayed in the client
     */
    private final String displayName;

    /**
     * Constructs a new constant of Bed Wars Dream mode games.
     *
     * @param displayName the name of this Dream mode game displayed in the
     *         client
     */
    DreamMode(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns an unmodifiable {@link Collection} containing display names of
     * every constants of this enumeration whose iteration order is the same as
     * the order their corresponding constants are declared.
     *
     * @return an unmodifiable {@code Collection} containing display names of
     *         every constants of this enumeration whose iteration order is the
     *         same as the order their corresponding constants are declared
     */
    public static Collection<String> displayNames() {
        return VALUE_NAMES;
    }

    /**
     * Returns an {@link Optional} wrapping the constant of this enumeration
     * with the specified display name, or an empty {@code Optional} if there is
     * no such constant with the specified name.
     * <p>
     * An empty {@code Optional} will also be returned if the
     * {@code displayName} argument is {@code null}.
     *
     * @param displayName the display name of the constant to return
     * @return an {@code Optional} wrapping the constant of this enumeration
     *         with the specified display name, or an empty {@code Optional} if
     *         there is no such constant with the specified name or
     *         {@code displayName == null}
     */
    public static Optional<DreamMode> valueOfDisplayName(String displayName) {
        if (displayName == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(DISPLAY_NAMES_MAP.get(displayName));
    }
}
