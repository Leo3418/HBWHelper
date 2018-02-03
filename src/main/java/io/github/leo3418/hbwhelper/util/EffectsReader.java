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
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
     * Returns a {@link Map} storing name (including amplifier) and duration
     * (in MM:SS format) of every effect on the player.
     *
     * @return a {@link Map} storing information of the player's effects
     */
    public static Map<String, String> getEffects() {
        Map<String, String> effects = new HashMap<String, String>();
        Collection<PotionEffect> potionEffects = Minecraft.getMinecraft()
                .thePlayer.getActivePotionEffects();
        for (PotionEffect potionEffect : potionEffects) {
            String effectName = I18n.format(potionEffect.getEffectName());
            int amplifier = potionEffect.getAmplifier();
            // Amplifier is 0 for level I, 1 for level II, etc.
            if (amplifier > 0) {
                effectName += " " + (amplifier + 1);
            }
            String duration = Potion.getDurationString(potionEffect);
            effects.put(effectName, duration);
        }
        return effects;
    }
}
