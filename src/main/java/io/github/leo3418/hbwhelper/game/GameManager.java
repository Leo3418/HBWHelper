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

package io.github.leo3418.hbwhelper.game;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Queue;

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
 * This class is designed <b>to be used only when the client is in a Minecraft
 * world</b>. Calling some methods when the client is not in a Minecraft world
 * (e.g. in the main menu) might produce {@link NullPointerException}.
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
     * Part of the prompt shown when the player's team unlocks "DeadShot"
     * upgrade
     */
    private static final String DEADSHOT_PROMPT =
            "\u00A7r\u00A76DeadShot ";

    /**
     * Reference to the last created instance of this class
     */
    private static GameManager instance;

    /**
     * Type of the current Bed Wars game
     */
    private final GameType gameType;

    /**
     * Trap queue
     */
    private final Queue<CountedTrap> trapQueue;

    /**
     * Cache of an unmodifiable copy of the trap queue
     */
    private final Collection<CountedTrap> readOnlyTraps;

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
     * Level of the "DeadShot" upgrade unlocked by the player's team
     */
    private int deadShotLevel;

    /**
     * Constructs a new {@code GameManager} instance.
     *
     * @param gameType the type of the current Bed Wars game
     */
    private GameManager(GameType gameType) {
        this.gameType = gameType;
        this.forgeLevel = gameType.initialForge;
        this.trapQueue = new ArrayDeque<>(MAX_TRAPS);
        for (CountedTrap countedTrap : gameType.initialTrapQueue) {
            this.trapQueue.add(countedTrap.getCopy());
        }
        this.readOnlyTraps =
                Collections.unmodifiableCollection(trapQueue);
    }

    /**
     * Creates a new {@code GameManager} instance. This method does not return
     * the instance created; rather, the new instance should be accessed by
     * calling the {@link #getInstance()} method.
     *
     * @param gameType the type of the current Bed Wars game
     */
    public static void createInstance(GameType gameType) {
        instance = new GameManager(gameType);
    }

    /**
     * Returns a reference to the last created instance of this class, or
     * {@code null} if an instance has never been created.
     * <p>
     * The returned reference <b>should not</b> be stored into a variable. The
     * reference is only useful for the current game the client is playing. When
     * the client joins another game, this method will return reference to
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
     * Clears the reference to the last created instance of this class, or does
     * nothing when an instance has never been created.
     * <p>
     * This method is useful when you wish to recover the state of this program
     * to the original state where no instance of this class has been created,
     * and any of your program's behavior relies on whether the
     * {@link #getInstance()} method returns {@code null}.
     */
    public static void clearInstance() {
        instance = null;
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
     * Returns level of the "DeadShot" upgrade unlocked by the player's team.
     * If the upgrade has not been unlocked at any level yet, then {@code 0} is
     * returned.
     *
     * @return level of the "DeadShot" upgrade unlocked by the player's team
     */
    public int getDeadShotLevel() {
        return deadShotLevel;
    }

    /**
     * Returns an <b>unmodifiable</b> {@link Collection} storing the trap queue.
     *
     * @return an <b>unmodifiable</b> {@code Collection} storing the trap queue
     */
    public Collection<CountedTrap> getTraps() {
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
        String message = event.getMessage().getFormattedText();
        if (message.contains(HEAL_POOL_PROMPT)) {
            healPool = true;
        } else if (message.contains(DRAGON_BUFF_PROMPT)) {
            dragonBuff = true;
        } else if (message.contains(DEADSHOT_PROMPT)) {
            // Parses the message to get the upgrade's level
            int levelStart = message.indexOf(DEADSHOT_PROMPT) +
                    DEADSHOT_PROMPT.length();
            int levelEnd = message.indexOf("\u00A7r", levelStart);
            String level = message.substring(levelStart, levelEnd);
            switch (level) {
                case "I":
                    deadShotLevel = 1;
                    break;
                case "II":
                    deadShotLevel = 2;
                    break;
                case "III":
                    deadShotLevel = 3;
                    break;
                case "IV":
                    deadShotLevel = 4;
                    break;
            }
        } else {
            for (ForgeLevel level : ForgeLevel.values()) {
                if (message.contains(level.prompt)) {
                    forgeLevel = level;
                    return;
                }
            }
            for (TrapType trapType : TrapType.values()) {
                /*
                If client temporarily leaves the current game, and a trap is
                set off before the client rejoins, the local trap queue will
                not be updated. Therefore, some while loops are used here to
                update the local trap queue correctly after client rejoins a
                game.
                 */
                if (message.contains(trapType.purchasePrompt)) {
                    /*
                    If the local trap queue is full but new trap is purchased,
                    some traps must have been set off since client leaves
                     */
                    while (trapQueue.size() >= MAX_TRAPS) {
                        trapQueue.remove();
                    }
                    trapQueue.add(new CountedTrap(trapType, gameType.trapUses));
                    return;
                } else if (message.contains(trapType.setOffPrompt)) {
                    /*
                    Removes all traps at the front of the trap queue that have
                    already been set off since client leaves
                     */
                    boolean consumed = false;
                    while (!consumed && !trapQueue.isEmpty()) {
                        CountedTrap firstInQueue = trapQueue.peek();
                        TrapType firstTrapType = firstInQueue.getTrapType();
                        if (firstTrapType == trapType) {
                            firstInQueue.setOff();
                            if (firstInQueue.hasUsedUp()) {
                                trapQueue.remove();
                            }
                            consumed = true;
                        } else {
                            trapQueue.remove();
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
        Iterable<Entity> entities = Minecraft.getMinecraft().world
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
        Iterable<EntityArmorStand> genEntities = Minecraft.getMinecraft().world
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
}
