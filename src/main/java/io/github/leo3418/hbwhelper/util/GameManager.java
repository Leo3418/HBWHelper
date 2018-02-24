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
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.*;

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
     * Maximum number of traps a team can have in the trap queue
     */
    private static final int MAX_TRAPS = 3;

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
     * Trap queue of the player's team
     */
    private Queue<Trap> traps;

    /**
     * Cache of an unmodifiable copy of the trap queue
     */
    private Collection readOnlyTraps;

    /**
     * Constructs a new {@code GameManager} instance.
     */
    public GameManager() {
        instance = this;
        forgeLevel = ForgeLevel.ORDINARY_FORGE;
        traps = new ArrayDeque<Trap>(MAX_TRAPS);
        readOnlyTraps = Collections.unmodifiableCollection(traps);
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
     * Returns an <b>unmodifiable</b> copy of the trap queue of the player's
     * team.
     *
     * @return an <b>unmodifiable</b> copy of the trap queue of the player's
     *         team
     */
    public Collection getTraps() {
        return readOnlyTraps;
    }

    /**
     * Updates upgrades the player's team has unlocked by analyzing chat
     * message client receives.
     *
     * @param event the event fired when client receives a chat message
     */
    public void update(ClientChatReceivedEvent event) {
        String message = event.getMessage().getFormattedText();
        if (message.contains(HEAL_POOL_PROMPT)) {
            healPool = true;
        } else if (message.contains(DRAGON_BUFF_PROMPT)) {
            dragonBuff = true;
        } else if (message.contains(BED_SELF_DESTRUCTION_PROMPT)) {
            bedSelfDestructing = true;
        } else {
            for (ForgeLevel level : ForgeLevel.values()) {
                if (message.contains(level.prompt)) {
                    forgeLevel = level;
                    return;
                }
            }
            for (Trap trap : Trap.values()) {
                if (message.contains(trap.purchasePrompt)) {
                    while (traps.size() >= MAX_TRAPS) {
                        traps.remove();
                    }
                    traps.add(trap);
                    return;
                }
                if (message.contains(trap.setOffPrompt)) {
                    boolean removed = false;
                    while (!removed && !traps.isEmpty()) {
                        if (traps.remove() == trap) {
                            removed = true;
                        }
                    }
                    return;
                }
            }
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
                        new AxisAlignedBB(generatorPos));
        for (EntityArmorStand genEntity : genEntities) {
            ITextComponent floatTextComponent = genEntity.getDisplayName();
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
        private final String prompt;

        /**
         * Text displayed for this level on this mod's GUI
         */
        private final String displayText;

        /**
         * Constructs a new constant of resource generation speed levels.
         *
         * @param name the name of this trap shown in any prompt in Hypixel
         *         without any formatting code
         * @see <a href="https://minecraft.gamepedia.com/Formatting_codes"
         *         target="_top">Formatting codes in Minecraft</a>
         */
        ForgeLevel(String name) {
            this.prompt = "\u00A7r\u00A76" + name + "\u00A7r";
            this.displayText = name;
        }

        /**
         * Returns the text displayed for this trap on this mod's GUI.
         *
         * @return the text displayed for this trap on this mod's GUI
         */
        @Override
        public String toString() {
            return displayText;
        }
    }

    /**
     * Enumeration of all traps in Hypixel Bed Wars.
     */
    private enum Trap {
        /**
         * The ordinary "It's a trap!"
         */
        ORDINARY("It's a trap!", "Ordinary"),
        /**
         * The "Counter-Offensive Trap"
         */
        COUNTER("Counter-Offensive Trap", "Counter-Offensive"),
        /**
         * The "Alarm Trap"
         * <p>
         * This value has three arguments because Hypixel used "Alarm Trap" and
         * "Alarm trap" at the same time.
         */
        ALARM("Alarm Trap", "Alarm trap", "Alarm"),
        /**
         * The "Miner Fatigue Trap"
         */
        MINER_FATIGUE("Miner Fatigue Trap", "Miner Fatigue");

        /**
         * Part of the prompt shown when the player's team purchases this trap
         */
        private final String purchasePrompt;

        /**
         * Part of the prompt shown when this trap is set off
         */
        private final String setOffPrompt;

        /**
         * Text displayed for this trap on this mod's GUI
         */
        private final String displayText;

        /**
         * Constructs a new constant of traps whose name is <b>consistent</b>
         * in Hypixel Bed Wars.
         *
         * @param name the name of this trap shown in any prompt in Hypixel
         *         Bed Wars without any formatting code
         * @param displayText the text displayed for this trap on this mod's GUI
         * @see <a href="https://minecraft.gamepedia.com/Formatting_codes"
         *         target="_top">Formatting codes in Minecraft</a>
         */
        Trap(String name, String displayText) {
            this(name, name, displayText);
        }

        /**
         * Constructs a new constant of traps whose name is <b>inconsistent</b>
         * in Hypixel Bed Wars.
         *
         * @param purchaseName the name of this trap in the prompt shown when
         *         the player's team purchases this trap
         * @param setOffName the name of this trap in the prompt shown when
         *         it sets off
         * @param displayText the text displayed for this trap on this mod's GUI
         * @see <a href="https://minecraft.gamepedia.com/Formatting_codes"
         *         target="_top">Formatting codes in Minecraft</a>
         */
        Trap(String purchaseName, String setOffName, String displayText) {
            this.purchasePrompt = "\u00A7r\u00A76" + purchaseName + "\u00A7r";
            this.setOffPrompt = "\u00A7c\u00A7l" + setOffName;
            this.displayText = displayText;
        }

        /**
         * Returns the text displayed for this trap on this mod's GUI
         *
         * @return the text displayed for this trap on this mod's GUI
         */
        @Override
        public String toString() {
            return displayText;
        }
    }
}
