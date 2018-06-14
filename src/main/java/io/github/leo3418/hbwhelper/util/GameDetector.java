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

import io.github.leo3418.hbwhelper.EventManager;
import io.github.leo3418.hbwhelper.event.ClientJoinInProgressGameEvent;
import io.github.leo3418.hbwhelper.event.ClientRejoinGameEvent;
import io.github.leo3418.hbwhelper.event.GameStartEvent;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

/**
 * Detects and tracks if client is in a Hypixel Bed Wars game.
 * <p>
 * This is a Singleton class. Only one instance of this class may be created
 * per runtime.
 *
 * @author Leo
 */
public class GameDetector {
    /**
     * Prompt client received in chat when a new ordinary game starts
     */
    private static final String ORDINARY_START_TEXT =
            "\u00A7r\u00A7f\u00A7lBed Wars\u00A7r";

    /**
     * Prompt client received in chat when a new game in Capture Mode starts
     */
    private static final String CAPTURE_START_TEXT =
            "\u00A7r\u00A7f\u00A7lTo win a game of Bed Wars Capture\u00A7r";

    /**
     * Prompt client received in chat when a game in Dream Mode starts
     */
    private static final String DREAM_START_TEXT =
            "\u00A7r\u00A7f\u00A7lBed Wars Ultimate\u00A7r";

    /**
     * Prompt client received in chat when it rejoins a game
     */
    private static final String REJOIN_TEXT =
            "\u00A7e\u00A7lTo leave Bed Wars, type /lobby\u00A7r";

    /**
     * Prompt client received in chat when it joins an in-progress game for the
     * first time
     */
    private static final String IN_PROGRESS_GAME_JOIN_TEXT =
            "\u00A7aFound an in-progress Bed Wars game! Teleporting you to ";

    /**
     * The only instance of this class
     */
    private static final GameDetector INSTANCE = new GameDetector();

    /**
     * The {@code HypixelDetector} instance
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
     * The screen with dirt background will show up. when client transfers.
     * <p>
     * This method should be called when a {@link GuiOpenEvent} is fired.
     *
     * @param event the event fired when the screen with dirt background shows
     *         up
     */
    public void update(GuiOpenEvent event) {
        if (inBedWars && event.getGui() instanceof GuiDownloadTerrain) {
            inBedWars = false;
        }
    }

    /**
     * Updates whether client is in a Bed Wars game upon connection change.
     * <p>
     * Because {@link GuiOpenEvent} will not be called when client disconnects,
     * this method is implemented to detect
     * {@link ClientDisconnectionFromServerEvent
     * ClientDisconnectionFromServerEvent}, which is fired when client
     * disconnects.
     * <p>
     * This method should be called whenever a {@link FMLNetworkEvent} is fired.
     *
     * @param event the event fired when client joins or leaves a server
     */
    public void update(FMLNetworkEvent event) {
        if (inBedWars && event instanceof ClientDisconnectionFromServerEvent) {
            /*
            GuiOpenEvent will not be called when client disconnects, so we need
            to detect ClientDisconnectionFromServerEvent
             */
            inBedWars = false;
        }
    }

    /**
     * Updates whether client is in a Bed Wars game by analyzing chat message
     * client receives, and fires corresponding events.
     * <p>
     * If a Bed Wars game starts, fires a {@link GameStartEvent} on this mod's
     * {@link EventManager#EVENT_BUS proprietary event bus.}
     * <p>
     * If client is rejoining a Bed Wars game, fires a
     * {@link ClientRejoinGameEvent} on this mod's
     * {@link EventManager#EVENT_BUS proprietary event bus.}
     * <p>
     * If client is joining an in-progress game which it never played before
     * (which is different from rejoining a game), fires a
     * {@link ClientJoinInProgressGameEvent} on this mod's
     * {@link EventManager#EVENT_BUS proprietary event bus.}
     * <p>
     * This method should be called whenever a {@link ClientChatReceivedEvent}
     * is fired.
     *
     * @param event the event fired when client receives a chat message
     */
    public void update(ClientChatReceivedEvent event) {
        String message = event.getMessage().getFormattedText();
        if (hypixelDetector.isIn() && !inBedWars) {
            if ((message.contains(ORDINARY_START_TEXT)
                    || message.contains(CAPTURE_START_TEXT))
                    || message.contains(DREAM_START_TEXT)) {
                // A Bed Wars game starts
                inBedWars = true;
                EventManager.EVENT_BUS.post(new GameStartEvent());
            } else if (message.contains(REJOIN_TEXT)) {
                // Client rejoins a Bed Wars game
                inBedWars = true;
                EventManager.EVENT_BUS.post(new ClientRejoinGameEvent());
            }
        } else if (message.contains(IN_PROGRESS_GAME_JOIN_TEXT)) {
            // Client joins a Bed Wars game that is in progress
            /*
            There is no need to change `inBedWars` either from `true` to `false`
            or from `false` to `true` in this branch. Both jobs are done by
            other code in this class.

            No matter whether the client is in Bed Wars or not, immediately
            after client receives the text in this else-if branch's condition,
            the "Downloading terrain" screen shows up, changing `inBedWars` to
            `false`.

            Because even in this case, Hypixel makes things happen like if
            the client is rejoining a game it was in before, this else-if
            branch should only fire event; changing `inBedWars` to `true`
            should be done after the rejoin prompt text is sent to client.
             */
            EventManager.EVENT_BUS.post(
                    new ClientJoinInProgressGameEvent());
        }
    }
}
