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
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Stores information and progress of a Bed Wars game session, and allows other
 * classes to read the information, such as time until next diamond generation.
 *
 * @author Leo
 */
public class GameManager {
    /**
     * Text returned by {@link GameManager#getNextDiamond()} and
     * {@link GameManager#getNextEmerald()} when there is no available
     * generator to read
     */
    private static final String FINDING_GEN_PROMPT = "Searching signal...";

    /**
     * Text that only appears in the line showing spawn time above a generator
     */
    private static final String GENERATOR_TEXT = "§eSpawns in §c";

    /**
     * Text that only appears in a diamond generator's display name
     */
    private static final String DIAMOND_GEN_TEXT = "§b§lDiamond§r";

    /**
     * Text that only appears in an emerald generator's display name
     */
    private static final String EMERALD_GEN_TEXT = "§2§lEmerald§r";

    /**
     * A reference to the last created instance of this class
     */
    private static GameManager instance;

    /**
     * Position of the diamond generator being read
     */
    private BlockPos diamondGenPos;

    /**
     * Position of the emerald generator being read
     */
    private BlockPos emeraldGenPos;

    /**
     * Constructs a new {@code GameManager} instance.
     */
    public GameManager() {
        instance = this;
    }

    /**
     * Returns a reference to the last created instance of this class.
     *
     * @return a reference to the last created instance of this class
     */
    public static GameManager getInstance() {
        return instance;
    }

    /**
     * Returns a string showing spawn time of next diamond, or
     * {@link GameManager#FINDING_GEN_PROMPT} if there is no diamond generator
     * that can be read.
     *
     * @return a string showing spawn time of next diamond, or
     *         {@link GameManager#FINDING_GEN_PROMPT}
     */
    public String getNextDiamond() {
        if (diamondGenPos != null) {
            int time = getSpawnTime(diamondGenPos);
            if (time != -1) {
                return time + "s";
            }
        }
        // When position of diamond generator not set or the current generator's
        // display name is no longer readable, find a new diamond generator
        diamondGenPos = findGenerator(DIAMOND_GEN_TEXT);
        return FINDING_GEN_PROMPT;
    }

    /**
     * Returns a string showing spawn time of next emerald, or
     * {@link GameManager#FINDING_GEN_PROMPT} if there is no emerald generator
     * that can be read.
     *
     * @return a string showing spawn time of next emerald, or
     *         {@link GameManager#FINDING_GEN_PROMPT}
     */
    public String getNextEmerald() {
        if (emeraldGenPos != null) {
            int time = getSpawnTime(emeraldGenPos);
            if (time != -1) {
                return time + "s";
            }
        }
        // When position of emerald generator not set or the current generator's
        // display name is no longer readable, find a new emerald generator
        emeraldGenPos = findGenerator(EMERALD_GEN_TEXT);
        return FINDING_GEN_PROMPT;
    }

    /**
     * Returns position of a generator (an armor stand) whose display name
     * contains a specified string.
     * <p>
     * If such armor stand cannot be found, returns {@code null}.
     *
     * @param generatorText the text that would appear above the generator
     * @return position of the generator, or {@code null} if the generator is
     *         not found
     */
    private BlockPos findGenerator(String generatorText) {
        List<Entity> entities = Minecraft.getMinecraft().theWorld
                .getLoadedEntityList();
        for (Entity entity : entities) {
            if (entity instanceof EntityArmorStand) {
                String name = entity.getDisplayName().getFormattedText();
                if (name.contains(generatorText)) {
                    return entity.getPosition();
                }
            }
        }
        return null;
    }

    /**
     * Returns spawn time of  the generator at given position.
     * <p>
     * If a generator cannot be found or read at that position, returns
     * {@code -1}.
     *
     * @param generatorPos the position of the generator
     * @return spawn time of the generator at given position, or {@code -1}
     */
    private int getSpawnTime(BlockPos generatorPos) {
        List<EntityArmorStand> genEntities = Minecraft.getMinecraft().theWorld
                .getEntitiesWithinAABB(EntityArmorStand.class,
                        new AxisAlignedBB(generatorPos));
        int time = -1;
        for (EntityArmorStand genEntity : genEntities) {
            String floatText = genEntity.getDisplayName().getFormattedText();
            if (floatText.contains(GENERATOR_TEXT)) {
                floatText = TextFormatRemover.removeAllFormats(floatText);
                time = Integer.parseInt(floatText.replaceAll("[^0-9]", ""));
            }
        }
        return time;
    }
}
