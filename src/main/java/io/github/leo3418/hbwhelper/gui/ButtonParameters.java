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

package io.github.leo3418.hbwhelper.gui;

/**
 * This class consists exclusively of constants that control the size, spacing
 * and other dimensions of the buttons on this mod's GUI components.
 *
 * @author Leo
 */
class ButtonParameters {
    /**
     * Vertical distance between borders of two adjacent buttons
     */
    static final int BUTTONS_INTERVAL = 4;

    /**
     * Height of buttons on this mod's GUI
     */
    static final int BUTTON_HEIGHT = 20;

    /**
     * Vertical distance between the top of a button and the top of the button
     * right below it
     */
    static final int BUTTONS_TRANSLATION_INTERVAL =
            BUTTON_HEIGHT + BUTTONS_INTERVAL;

    /**
     * Prevents instantiation of this class.
     */
    private ButtonParameters() {
    }
}
