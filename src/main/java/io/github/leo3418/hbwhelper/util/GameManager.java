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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.List;

/**
 * Stores information and progress of a Bed Wars game session, and allows other
 * classes to read the information, such as time until next diamond generation
 * and upgrades the player's team has unlocked.
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
    private static final String GENERATOR_TEXT = "\u00A7eSpawns in \u00A7c";

    /**
     * Text that only appears in a diamond generator's display name
     */
    private static final String DIAMOND_GEN_TEXT = "\u00A7b\u00A7lDiamond\u00A7r";

    /**
     * Text that only appears in an emerald generator's display name
     */
    private static final String EMERALD_GEN_TEXT = "\u00A72\u00A7lEmerald\u00A7r";

    /**
     * Part of the prompt shown when the player's team unlocks "Heal Pool"
     * upgrade
     */
    private static final String HEAL_POOL_PROMPT =
            "\u00A7r\u00A76Heal Pool\u00A7r";

    /**
     * Part of the prompt shown when the player's team unlocks "Dragon Buff"
     * upgrade
     */
    private static final String DRAGON_BUFF_PROMPT =
            "\u00A7r\u00A76Dragon Buff\u00A7r";

    /**
     * Prompt client receives when all beds are going to destruct themselves
     */
    private static final String BED_SELF_DESTRUCTION_PROMPT =
            "\u00A7cAll beds will be destroyed in 5 minutes!\u00A7r";

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
     * Whether bed self-destruction occurs in 5 minutes
     */
    private boolean bedSelfDestructing;

    /**
     * Level of resource generation speed on the player's base island
     */
    private ForgeLevel forgeLevel;

    /**
     * Whether the player's team has unlocked "Heal Pool" upgrade
     */
    private boolean healPool;

    /**
     * Whether the player's team has unlocked "Dragon Buff" upgrade
     */
    private boolean dragonBuff;

    /**
     * Constructs a new {@code GameManager} instance.
     */
    public GameManager() {
        instance = this;
        forgeLevel = ForgeLevel.ORDINARY_FORGE;
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
     * Returns level of resource generation speed on the player's base island.
     *
     * @return level of resource generation speed on the player's base island
     */
    public ForgeLevel getForgeLevel() {
        return forgeLevel;
    }

    /**
     * Returns whether or not the player's team has unlocked "Heal Pool"
     * upgrade.
     *
     * @return whether or not the player's team has unlocked "Heal Pool"
     *         upgrade
     */
    public boolean hasHealPool() {
        return healPool;
    }

    /**
     * Returns whether or not the player's team has unlocked "Dragon Buff"
     * upgrade.
     *
     * @return whether or not the player's team has unlocked "Dragon Buff"
     *         upgrade
     */
    public boolean hasDragonBuff() {
        return dragonBuff;
    }

    /**
     * Returns whether bed self-destruction occurs in 5 minutes.
     *
     * @return whether bed self-destruction occurs in 5 minutes
     */
    public boolean isBedSelfDestructing() {
        return bedSelfDestructing;
    }

    /**
     * Updates upgrades the player's team has unlocked by analyzing chat
     * message client receives.
     *
     * @param event the event fired when client receives a chat message
     */
    public void update(ClientChatReceivedEvent event) {
        String message = event.message.getFormattedText();
        for (ForgeLevel level : ForgeLevel.values()) {
            if (message.contains(level.prompt)) {
                forgeLevel = level;
                return;
            }
        }
        if (message.contains(HEAL_POOL_PROMPT)) {
            healPool = true;
        } else if (message.contains(DRAGON_BUFF_PROMPT)) {
            dragonBuff = true;
        } else if (message.contains(BED_SELF_DESTRUCTION_PROMPT)) {
            bedSelfDestructing = true;
        }
    }

    /**
     * Returns position of a generator (an armor stand) whose display name
     * contains a specified string, or {@code null} if such armor stand cannot
     * be found.
     *
     * @param generatorText the text that would appear above the generator
     * @return position of a generator (an armor stand) whose display name
     *         contains a specified string, or {@code null} if such armor stand
     *         cannot be found
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
     * Returns spawn time of the generator at given position, or {@code -1} if
     * a generator cannot be found or read at that position.
     *
     * @param generatorPos the position of the generator
     * @return spawn time of the generator at given position, or {@code -1} if
     *         a generator cannot be found or read at that position
     */
    private int getSpawnTime(BlockPos generatorPos) {
        List<EntityArmorStand> genEntities = Minecraft.getMinecraft().theWorld
                .getEntitiesWithinAABB(EntityArmorStand.class,
                        new AxisAlignedBB(generatorPos, generatorPos.add(1, 1, 1)));
        for (EntityArmorStand genEntity : genEntities) {
            IChatComponent floatTextComponent = genEntity.getDisplayName();
            if (floatTextComponent.getFormattedText().contains(GENERATOR_TEXT)) {
                return Integer.parseInt(floatTextComponent.getUnformattedText()
                        .replaceAll("[^0-9]", ""));
            }
        }
        return -1;
    }

    /**
     * Enumeration of all resource generation speed levels on the player's base
     * island in Hypixel Bed Wars.
     */
    private enum ForgeLevel {
        /**
         * The initial resource generation speed level without any upgrade
         */
        ORDINARY_FORGE("Not upgraded"),
        /**
         * Resource generation speed level with "Iron Forge" upgrade
         */
        IRON_FORGE("\u00A7r\u00A76Iron Forge\u00A7r"),
        /**
         * Resource generation speed level with "Golden Forge" upgrade
         */
        GOLDEN_FORGE("\u00A7r\u00A76Golden Forge\u00A7r"),
        /**
         * Resource generation speed level with "Emerald Forge" upgrade
         */
        EMERALD_FORGE("\u00A7r\u00A76Emerald Forge\u00A7r"),
        /**
         * Resource generation speed level with "Molten Forge" upgrade
         */
        MOLTEN_FORGE("\u00A7r\u00A76Molten Forge\u00A7r");

        /**
         * Part of the prompt shown when the player's team unlocks this level
         * of resource generation speed
         */
        private final String prompt;

        /**
         * Constructs a new constant of resource generation speed levels.
         *
         * @param prompt part of the prompt shown when the player's team unlocks
         *         this level of resource generation speed
         */
        ForgeLevel(String prompt) {
            this.prompt = prompt;
        }

        /**
         * Returns a string representation of this constant's name without
         * formatting codes.
         *
         * @return a string representation of this constant's name without
         *         formatting codes
         * @see <a href="https://minecraft.gamepedia.com/Formatting_codes"
         *         target="_top">Formatting codes in Minecraft</a>
         */
        @Override
        public String toString() {
            return TextFormatRemover.removeAllFormats(prompt);
        }
    }
}
