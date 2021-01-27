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
import io.github.leo3418.hbwhelper.event.TeleportCancelledEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

/**
 * Detects if client is joining an in-progress Hypixel Bed Wars game and if
 * joining is cancelled because of any reason.
 * <p>
 * Implementation of this class was fostered by Hypixel's introduction of the
 * Bed Wars Castle mode, in which a player might be directed to a game that has
 * already started after they choose to join a game.
 * <p>
 * This is a Singleton class. Only one instance of this class may be created
 * per runtime.
 *
 * @author Leo
 */
public class InProgressGameDetector {
    /**
     * Prompt client received in chat when it joins an in-progress game for the
     * first time
     */
    private static final String IN_PROGRESS_GAME_JOIN_TEXT =
            "\u00A7aFound an in-progress Bed Wars game! Teleporting you to ";

    /**
     * Prompt client received in chat when Hypixel attempts to put the player
     * into the server they are already in
     */
    private static final String ALREADY_CONNECTED_TEXT =
            "\u00A7cYou are already connected to this server";

    /**
     * Prompt client received in chat when it joins an in-progress game that
     * no longer accepts new players anymore
     */
    private static final String GAME_ALREADY_STARTED_TEXT =
            "\u00A7cThis game has already started! Please try again!";

    /**
     * The only instance of this class
     */
    private static final InProgressGameDetector INSTANCE =
            new InProgressGameDetector();

    /**
     * The {@link HypixelDetector} instance
     */
    private final HypixelDetector hypixelDetector;

    /**
     * The {@link GameDetector} instance
     */
    private final GameDetector gameDetector;

    /**
     * Implementation of Singleton design pattern, which allows only one
     * instance of this class to be created.
     */
    private InProgressGameDetector() {
        hypixelDetector = HypixelDetector.getInstance();
        gameDetector = GameDetector.getInstance();
    }

    /**
     * Returns the instance of this class.
     *
     * @return the instance of this class
     */
    public static InProgressGameDetector getInstance() {
        return INSTANCE;
    }

    /**
     * Detects if client is joining an in-progress Hypixel Bed Wars game or if
     * joining is cancelled because of any reason by analyzing chat message
     * client receives, and fires corresponding events.
     * <p>
     * If client is joining an in-progress game it has never connected before,
     * fires a {@link ClientJoinInProgressGameEvent} on this mod's
     * {@link EventManager#EVENT_BUS proprietary event bus}.
     * <p>
     * If a game join has been initiated but is cancelled, either because client
     * has already connected to the server it is joining, or the game on the
     * server it is joining has already started, <b>and</b> client is in a Bed
     * Wars game, fires a {@link TeleportCancelledEvent} on this mod's
     * {@link EventManager#EVENT_BUS proprietary event bus}.
     * <p>
     * This method should be called whenever a {@link ClientChatReceivedEvent}
     * is fired.
     *
     * @param event the event fired when client receives a chat message
     */
    public void detect(ClientChatReceivedEvent event) {
        if (hypixelDetector.isIn()) {
            String message = TextComponents.toFormattedText(
                    event.getMessage());
            if (message.contains(IN_PROGRESS_GAME_JOIN_TEXT)) {
                EventManager.EVENT_BUS
                        .post(new ClientJoinInProgressGameEvent());
            } else if (gameDetector.isIn()
                    && (message.contains(ALREADY_CONNECTED_TEXT)
                    || message.contains(GAME_ALREADY_STARTED_TEXT))) {
                /*
                Client can also receive these messages when not in Bed Wars,
                but we only care about them when client is in Bed Wars
                 */
                EventManager.EVENT_BUS.post(new TeleportCancelledEvent());
            }
        }
    }
}
