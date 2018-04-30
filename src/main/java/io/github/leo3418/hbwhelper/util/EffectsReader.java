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
 * <p>
 * Like some other classes under this package, this class is designed <b>to be
 * used only when the client is in a Minecraft world</b>. Calling some methods
 * when the client is not in a Minecraft world (e.g. in the main menu) might
 * produce {@link NullPointerException}.
 *
 * @author Leo
 */
public class EffectsReader {
    /**
     * Number of game ticks per second in Minecraft
     */
    private static final int TICKS_PER_SECOND = 20;

    /**
     * Prevents instantiation of this class.
     */
    private EffectsReader() {
    }

    /**
     * Returns a {@link Collection} of all potion effects on the player.
     *
     * @return a {@code Collection} of all potion effects on the player
     */
    public static Collection<PotionEffect> getEffects() {
        return Minecraft.getMinecraft().thePlayer.getActivePotionEffects();
    }

    /**
     * Returns the displayed amplifier of a potion effect.
     * <p>
     * The amplifier returned is equivalent to the Roman numeral displayed in
     * game (e.g. 1 represents I, 2 represents II, 3 represents III).
     *
     * @param potionEffect the potion effect whose amplifier is queried
     * @return the displayed amplifier of a potion effect
     */
    public static int getDisplayedAmplifier(PotionEffect potionEffect) {
        // Internal amplifier is 0 for level I, 1 for level II, etc.
        int internalAmplifier = potionEffect.getAmplifier();
        return internalAmplifier + 1;
    }

    /**
     * Returns duration of a potion effect as how they are shown in the client.
     *
     * @param potionEffect the potion effect whose duration is queried
     * @return duration of the potion effect as how they are shown in the client
     */
    public static String getDisplayedDuration(PotionEffect potionEffect) {
        return Potion.getPotionDurationString(potionEffect, 1.0F);
    }

    /**
     * Returns duration of a potion effect in seconds, or {@code -1} if it has
     * maximum duration.
     *
     * @param potionEffect the potion effect whose duration is queried
     * @return duration of the potion effect in seconds, or {@code -1} if it has
     *         maximum duration
     */
    public static int getDuration(PotionEffect potionEffect) {
        if (potionEffect.getIsPotionDurationMax()) {
            return -1;
        }
        return potionEffect.getDuration() / TICKS_PER_SECOND;
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
