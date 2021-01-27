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

/**
 * Enumeration of all resource generation speed levels on the player's base
 * island in Hypixel Bed Wars.
 *
 * @author Leo
 */
public enum ForgeLevel {
    /**
     * The initial resource generation speed level without any upgrade
     */
    ORDINARY_FORGE("Not upgraded"),
    /**
     * Resource generation speed level with "Iron Forge" upgrade
     */
    IRON_FORGE("Iron Forge"),
    /**
     * Resource generation speed level with "Golden Forge" upgrade
     */
    GOLDEN_FORGE("Golden Forge"),
    /**
     * Resource generation speed level with "Emerald Forge" upgrade
     */
    EMERALD_FORGE("Emerald Forge"),
    /**
     * Resource generation speed level with "Molten Forge" upgrade
     */
    MOLTEN_FORGE("Molten Forge");

    /**
     * Part of the prompt shown when the player's team unlocks this level
     * of resource generation speed
     */
    final String prompt;

    /**
     * Constructs a new constant of resource generation speed levels.
     *
     * @param name the name of this trap shown in any prompt in Hypixel
     *         without any formatting code
     */
    ForgeLevel(String name) {
        this.prompt = "\u00A7r\u00A76" + name + "\u00A7r";
    }
}
