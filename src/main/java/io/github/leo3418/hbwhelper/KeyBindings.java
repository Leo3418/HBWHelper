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

package io.github.leo3418.hbwhelper;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

/**
 * Stores all custom key bindings of this mod, and provides a method for
 * registering those key bindings.
 *
 * @author Leo
 */
public class KeyBindings {
    /**
     * Key binding for toggling Bed Wars quick join menu
     */
    public static final KeyBinding QUICK_JOIN =
            new KeyBinding("key.hbwhelper.quickJoin", Keyboard.KEY_B,
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
