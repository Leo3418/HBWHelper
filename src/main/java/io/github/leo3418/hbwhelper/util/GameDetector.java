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

package io.github.leo3418.hbwhelper.util;

import io.github.leo3418.hbwhelper.EventManager;
import io.github.leo3418.hbwhelper.event.ClientJoinInProgressGameEvent;
import io.github.leo3418.hbwhelper.event.ClientLeaveGameEvent;
import io.github.leo3418.hbwhelper.event.ClientRejoinGameEvent;
import io.github.leo3418.hbwhelper.event.GameStartEvent;
import net.minecraft.client.gui.screen.DownloadTerrainScreen;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.GuiOpenEvent;

/**
 * Detects and tracks if client is in a Hypixel Bed Wars game.
 * <p>
 * This is a Singleton class. Only one instance of this class may be created
 * per runtime.
 *
 * @author Leo
 */
public class GameDetector {
    /*
     * Note: Some of the prompts below in 1.14 and above differ from their
     * counterpart in older Minecraft client versions.
     */

    /**
     * Prompt client received in chat when a new ordinary game starts
     */
    private static final String ORDINARY_START_TEXT =
            "\u00A7f\u00A7lBed Wars\u00A7r";

    /**
     * Prompt client received in chat when a Bed Wars game in Rush Mode starts
     */
    private static final String RUSH_START_TEXT =
            "\u00A7f\u00A7lBed Wars Rush\u00A7r";

    /**
     * Prompt client received in chat when a Bed Wars game in Ultimate Mode
     * starts
     */
    private static final String ULTIMATE_START_TEXT =
            "\u00A7f\u00A7lBed Wars Ultimate\u00A7r";

    /**
     * Prompt client received in chat when a Bed Wars game in Lucky Blocks Mode
     * starts
     */
    private static final String LUCKY_BLOCKS_START_TEXT =
            "\u00A7f\u00A7lBed Wars Lucky Blocks\u00A7r";

    /**
     * Prompt client received in chat when it rejoins a game
     */
    private static final String REJOIN_TEXT =
            "\u00A7e\u00A7lTo leave Bed Wars, type /lobby\u00A7r";

    /**
     * The only instance of this class
     */
    private static final GameDetector INSTANCE = new GameDetector();

    /**
     * The {@link HypixelDetector} instance
     */
    private final HypixelDetector hypixelDetector;

    /**
     * Whether client is currently in a Bed Wars game
     */
    private boolean inBedWars;

    /**
     * Implementation of Singleton design pattern, which allows only one
     * instance of this class to be created.
     */
    private GameDetector() {
        hypixelDetector = HypixelDetector.getInstance();
    }

    /**
     * Returns the instance of this class.
     *
     * @return the instance of this class
     */
    public static GameDetector getInstance() {
        return INSTANCE;
    }

    /**
     * Returns whether client is currently in a Bed Wars game.
     *
     * @return whether client is currently in a Bed Wars game
     */
    public boolean isIn() {
        return inBedWars;
    }

    /**
     * Updates whether client is in a Bed Wars game when it transfers from one
     * server to another.
     * <p>
     * The screen with dirt background, which might be accompanied by the text
     * "Downloading terrain", will show up when client transfers.
     * <p>
     * If client is leaving a Bed Wars game, fires a
     * {@link ClientLeaveGameEvent} on this mod's {@link EventManager#EVENT_BUS
     * proprietary event bus}.
     * <p>
     * This method should be called whenever a {@link GuiOpenEvent} is fired.
     *
     * @param event the event fired when the screen with dirt background shows
     *         up
     */
    public void update(GuiOpenEvent event) {
        if (inBedWars && event.getGui() instanceof DownloadTerrainScreen) {
            inBedWars = false;
            EventManager.EVENT_BUS.post(new ClientLeaveGameEvent());
        }
    }

    /**
     * Updates whether client is in a Bed Wars game upon connection change.
     * <p>
     * Because {@link GuiOpenEvent} will not be called when client disconnects,
     * this method is implemented to detect
     * {@link ClientPlayerNetworkEvent.LoggedOutEvent}, which is fired when
     * client disconnects.
     * <p>
     * If client is leaving a Bed Wars game, fires a
     * {@link ClientLeaveGameEvent} on this mod's {@link EventManager#EVENT_BUS
     * proprietary event bus}.
     * <p>
     * This method should be called whenever a {@link ClientPlayerNetworkEvent}
     * is fired.
     *
     * @param event the event fired when client joins or leaves a server
     */
    public void update(ClientPlayerNetworkEvent event) {
        if (inBedWars
                && event instanceof ClientPlayerNetworkEvent.LoggedOutEvent) {
            /*
            GuiOpenEvent will not be called when client disconnects, so we need
            to detect ClientPlayerNetworkEvent.LoggedOutEvent
             */
            inBedWars = false;
            EventManager.EVENT_BUS.post(new ClientLeaveGameEvent());
        }
    }

    /**
     * Updates whether client is in a Bed Wars game by analyzing chat message
     * client receives, and fires corresponding events.
     * <p>
     * If a Bed Wars game starts, fires a {@link GameStartEvent} on this mod's
     * {@link EventManager#EVENT_BUS proprietary event bus}.
     * <p>
     * If client is rejoining a Bed Wars game, fires a
     * {@link ClientRejoinGameEvent} on this mod's
     * {@link EventManager#EVENT_BUS proprietary event bus}.
     * <p>
     * If client is joining an in-progress game which it never played before
     * (which is different from rejoining a game), fires a
     * {@link ClientJoinInProgressGameEvent} on this mod's
     * {@link EventManager#EVENT_BUS proprietary event bus}.
     * <p>
     * This method should be called whenever a {@link ClientChatReceivedEvent}
     * is fired.
     *
     * @param event the event fired when client receives a chat message
     */
    public void update(ClientChatReceivedEvent event) {
        if (hypixelDetector.isIn() && !inBedWars) {
            String message = event.getMessage().getFormattedText();
            if (message.contains(ORDINARY_START_TEXT)
                    || message.contains(RUSH_START_TEXT)
                    || message.contains(ULTIMATE_START_TEXT)
                    || message.contains(LUCKY_BLOCKS_START_TEXT)) {
                // A Bed Wars game starts
                inBedWars = true;
                EventManager.EVENT_BUS.post(new GameStartEvent());
            } else if (message.contains(REJOIN_TEXT)) {
                // Client rejoins a Bed Wars game
                inBedWars = true;
                EventManager.EVENT_BUS.post(new ClientRejoinGameEvent());
            }
        }
    }
}
