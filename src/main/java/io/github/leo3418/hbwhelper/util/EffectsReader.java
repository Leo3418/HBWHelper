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
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.Collection;

/**
 * Provides methods for getting the player's status effects.
 *
 * @author Leo
 */
public class EffectsReader {
    /**
     * Prevents instantiation of this class.
     */
    private EffectsReader() {
    }

    /**
     * Returns a {@code Collection} of all potion effects on the player.
     *
     * @return a {@code Collection} of all potion effects on the player
     */
    public static Collection<PotionEffect> getEffects() {
        return Minecraft.getMinecraft().player.getActivePotionEffects();
    }

    /**
     * Returns a string indicating amplifier of a potion effect.
     *
     * The amplifier returned is equivalent to the Roman numeral displayed in
     * game (e.g. 2 represents II, 3 represents III).
     *
     * If the amplifier is level I, an empty string is returned.
     *
     * @param potionEffect the potion effect whose amplifier is queried
     * @return a string indicating amplifier of the potion effect
     */
    public static String getAmplifier(PotionEffect potionEffect) {
        int amplifier = potionEffect.getAmplifier();
        // Amplifier is 0 for level I, 1 for level II, etc.
        if (amplifier > 0) {
            return (amplifier + 1) + "";
        }
        return "";
    }

    /**
     * Returns duration of a potion effect as how they are shown in the client.
     *
     * @param potionEffect the potion effect whose duration is queried
     * @return duration of the potion effect as how they are shown in the client
     */
    public static String getDuration(PotionEffect potionEffect) {
        return Potion.getPotionDurationString(potionEffect, 1.0F);
    }

    /**
     * Returns icon index of a potion effect.
     *
     * @param potionEffect the potion effect whose icon index is queried
     * @return icon index of the potion effect
     */
    public static int getIconIndex(PotionEffect potionEffect) {
        return potionEffect.getPotion().getStatusIconIndex();
    }
}
