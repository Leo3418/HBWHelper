/*
 * Copyright (C) 2018-2020 Leo3418 <https://github.com/Leo3418>
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

package io.github.leo3418.hbwhelper;

import io.github.leo3418.hbwhelper.gui.QuickJoinMenuScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * Stores all custom key bindings of this mod, and provides a method for
 * registering those key bindings.
 *
 * @author Leo
 */
public class KeyBindings {
    /**
     * Key code for the default key toggling the
     * {@linkplain QuickJoinMenuScreen Bed Wars Quick Join menu}
     */
    private static final int DEFAULT_KEY_CODE_QUICK_JOIN = 66; // The "B" key

    /**
     * Key binding for toggling Bed Wars quick join menu
     */
    public static final KeyBinding QUICK_JOIN =
            new KeyBinding("key.hbwhelper.quickJoin",
                    DEFAULT_KEY_CODE_QUICK_JOIN,
                    "key.categories.misc");

    /**
     * Prevents instantiation of this class.
     */
    private KeyBindings() {
    }

    /**
     * Registers this mod's key bindings.
     */
    static void registerBindings() {
        ClientRegistry.registerKeyBinding(QUICK_JOIN);
    }
}
