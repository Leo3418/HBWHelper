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

import java.util.*;

/**
 * Stores information and progress of a Bed Wars game session, and allows other
 * classes to read information such as time until next diamond generation and
 * upgrades the player's team has unlocked.
 * <p>
 * When the client joins a new Bed Wars game, a new {@code GameManager} object
 * should be created for the game. The object can be reused if the client
 * disconnects from the game and rejoins it later unless Minecraft is restarted
 * in the meanwhile.
 * <p>
 * When a player leaves a Bed Wars game and joins another game on Hypixel, they
 * will no longer be able to rejoin the previous game they were playing, so the
 * previous {@code GameManager} object can and <b>should</b> be discarded, and
 * a new object should be created for the new game.
 * <p>
 * An object of this class can be created by one of these manners:
 * <ul>
 * <li>Merely call the constructor and not assign the new object to a variable,
 * and retrieve the last created object with {@link #getInstance()}</li>
 * <li>Call the constructor and assign the new object to a variable, then
 * assign all objects created later to that variable</li>
 * </ul>
 * Like some other classes under this package, this class is designed <b>to be
 * used only when the client is in a Minecraft world</b>. Calling some methods
 * when the client is not in a Minecraft world (e.g. in the main menu) might
 * produce {@link NullPointerException}.
 *
 * @author Leo
 */
public class GameManager {
    /**
     * Maximum number of traps a team can have in the trap queue
     */
    public static final int MAX_TRAPS = 3;

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
     * Reference to the last created instance of this class
     */
    private static GameManager instance;

    /**
     * Trap queue
     */
    private final Queue<Trap> traps;

    /**
     * Cache of an unmodifiable copy of the trap queue
     */
    private final Collection<Trap> readOnlyTraps;

    /**
     * Position of the diamond generator being read
     */
    private BlockPos diamondGenPos;

    /**
     * Position of the emerald generator being read
     */
    private BlockPos emeraldGenPos;

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
        forgeLevel = ForgeLevel.ORDINARY_FORGE;
        traps = new ArrayDeque<Trap>(MAX_TRAPS);
        readOnlyTraps =
                Collections.unmodifiableCollection(traps);
        instance = this;
    }

    /**
     * Returns a reference to the last created instance of this class, or
     * {@code null} if an instance has never been created.
     * <p>
     * The returned reference <b>should not</b> be stored into a variable
     * because it is only useful for the current game the client is playing.
     * When the client joins another game, this method will return reference to
     * another instance of this class, and the previous instance should be
     * discarded and recycled by the garbage collector. If a reference to the
     * previous instance were still stored in a variable, it would prevent the
     * garbage collection on the instance, causing memory leak.
     *
     * @return a reference to the last created instance of this class, or
     *         {@code null} if an instance has never been created
     */
    public static GameManager getInstance() {
        return instance;
    }

    /**
     * Returns spawn time of next diamond, or {@code -1} if there is no diamond
     * generator that can be read.
     *
     * @return spawn time of next diamond, or {@code -1} if there is no diamond
     *         generator that can be read
     */
    public int getNextDiamond() {
        int time;
        if (diamondGenPos != null) {
            time = getSpawnTime(diamondGenPos);
        } else {
            time = -1;
        }
        // When position of diamond generator not set or the current generator's
        // display name is no longer readable, find a new diamond generator
        if (time == -1) {
            diamondGenPos = findGenerator(DIAMOND_GEN_TEXT);
        }
        return time;
    }

    /**
     * Returns spawn time of next emerald, or {@code -1} if there is no emerald
     * generator that can be read.
     *
     * @return spawn time of next emerald, or {@code -1} if there is no emerald
     *         generator that can be read
     */
    public int getNextEmerald() {
        int time;
        if (emeraldGenPos != null) {
            time = getSpawnTime(emeraldGenPos);
        } else {
            time = -1;
        }
        // When position of emerald generator not set or the current generator's
        // display name is no longer readable, find a new emerald generator
        if (time == -1) {
            emeraldGenPos = findGenerator(EMERALD_GEN_TEXT);
        }
        return time;
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
     * Returns an <b>unmodifiable</b> {@link Collection} derived from the trap
     * queue.
     *
     * @return an <b>unmodifiable</b> {@code Collection} derived from the trap
     *         queue
     */
    public Collection<Trap> getTraps() {
        return readOnlyTraps;
    }

    /**
     * Updates upgrades the player's team has unlocked by analyzing chat
     * message client receives.
     * <p>
     * This method should be called when the client is in Bed Wars, and a
     * {@link ClientChatReceivedEvent} is fired.
     *
     * @param event the event fired when client receives a chat message
     */
    public void update(ClientChatReceivedEvent event) {
        String message = event.message.getFormattedText();
        if (message.contains(HEAL_POOL_PROMPT)) {
            healPool = true;
        } else if (message.contains(DRAGON_BUFF_PROMPT)) {
            dragonBuff = true;
        } else {
            for (ForgeLevel level : ForgeLevel.values()) {
                if (message.contains(level.prompt)) {
                    forgeLevel = level;
                    return;
                }
            }
            for (Trap trap : Trap.values()) {
                /*
                If client temporarily leaves the current game, and a trap is
                set off before the client rejoins, the local trap queue will
                not be updated. Therefore, some while loops are used here to
                update the local trap queue correctly after client rejoins a
                game.
                 */
                if (message.contains(trap.purchasePrompt)) {
                    /*
                    If the local trap queue is full but new trap is purchased,
                    some traps must have been set off since client leaves
                     */
                    while (traps.size() >= MAX_TRAPS) {
                        traps.remove();
                    }
                    traps.add(trap);
                    return;
                } else if (message.contains(trap.setOffPrompt)) {
                    /*
                    Removes all traps at the front of the trap queue that have
                    already been set off since client leaves
                     */
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
    public enum ForgeLevel {
        /**
         * The initial resource generation speed level without any upgrade
         */
        @SuppressWarnings("unused")
        ORDINARY_FORGE("Not upgraded"),
        /**
         * Resource generation speed level with "Iron Forge" upgrade
         */
        @SuppressWarnings("unused")
        IRON_FORGE("Iron Forge"),
        /**
         * Resource generation speed level with "Golden Forge" upgrade
         */
        @SuppressWarnings("unused")
        GOLDEN_FORGE("Golden Forge"),
        /**
         * Resource generation speed level with "Emerald Forge" upgrade
         */
        @SuppressWarnings("unused")
        EMERALD_FORGE("Emerald Forge"),
        /**
         * Resource generation speed level with "Molten Forge" upgrade
         */
        @SuppressWarnings("unused")
        MOLTEN_FORGE("Molten Forge");

        /**
         * Part of the prompt shown when the player's team unlocks this level
         * of resource generation speed
         */
        private final String prompt;

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

    /**
     * Enumeration of all traps in Hypixel Bed Wars.
     */
    public enum Trap {
        /**
         * The ordinary "It's a trap!"
         */
        @SuppressWarnings("unused")
        ORDINARY("It's a trap!"),
        /**
         * The "Counter-Offensive Trap"
         */
        @SuppressWarnings("unused")
        COUNTER("Counter-Offensive Trap"),
        /**
         * The "Alarm Trap"
         * <p>
         * This value has an extra argument because Hypixel uses "Alarm Trap"
         * and "Alarm trap" at the same time.
         */
        @SuppressWarnings("unused")
        ALARM("Alarm Trap", "Alarm trap"),
        /**
         * The "Miner Fatigue Trap"
         */
        @SuppressWarnings("unused")
        MINER_FATIGUE("Miner Fatigue Trap");

        /**
         * Part of the prompt shown when the player's team purchases this trap
         */
        private final String purchasePrompt;

        /**
         * Part of the prompt shown when this trap is set off
         */
        private final String setOffPrompt;

        /**
         * Constructs a new constant of traps whose name is <b>consistent</b>
         * in Hypixel Bed Wars.
         *
         * @param name the name of this trap shown in any prompt in Hypixel
         *         Bed Wars without any formatting code
         */
        Trap(String name) {
            this(name, name);
        }

        /**
         * Constructs a new constant of traps whose name is <b>inconsistent</b>
         * in Hypixel Bed Wars.
         *
         * @param purchaseName the name of this trap in the prompt shown when
         *         the player's team purchases this trap
         * @param setOffName the name of this trap in the prompt shown when
         *         it sets off
         */
        Trap(String purchaseName, String setOffName) {
            this.purchasePrompt = "\u00A7r\u00A76" + purchaseName + "\u00A7r";
            this.setOffPrompt = "\u00A7c\u00A7l" + setOffName;
        }
    }
}
