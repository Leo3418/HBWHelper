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

package io.github.leo3418.hbwhelper.game;

import java.util.Objects;

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
    UNSELECTED("hbwhelper.configGui.unselected"),
    /**
     * Rush mode
     */
    RUSH("hbwhelper.dream.rush"),
    /**
     * Ultimate mode
     */
    ULTIMATE("hbwhelper.dream.ultimate"),
    /**
     * Castle mode
     */
    CASTLE("hbwhelper.dream.castle"),
    /**
     * Lucky Blocks mode
     */
    LUCKY_BLOCKS("hbwhelper.dream.luckyBlocks"),
    /**
     * Voidless mode
     */
    VOIDLESS("hbwhelper.dream.voidless"),
    /**
     * Armed mode
     */
    ARMED("hbwhelper.dream.armed");

    /**
     * Translate key for the name of this Dream mode game
     */
    private final String translateKey;

    /**
     * Constructs a new constant of Bed Wars Dream mode games.
     *
     * @param translateKey translate key for the name of this Dream mode game
     * @throws NullPointerException if {@code translateKey == null}
     */
    DreamMode(String translateKey) {
        this.translateKey =
                Objects.requireNonNull(translateKey, "translateKey");
    }

    /**
     * Returns the translate key for the name of this Dream mode game.
     *
     * @return the translate key for the name of this Dream mode game
     */
    public String getTranslateKey() {
        return this.translateKey;
    }
}
