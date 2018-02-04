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
import io.github.leo3418.hbwhelper.event.ClientLeaveGameEvent;
import io.github.leo3418.hbwhelper.event.ClientRejoinGameEvent;
import io.github.leo3418.hbwhelper.event.GameStartEvent;
import io.github.leo3418.hbwhelper.event.TickCounterTimeUpEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
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
     * Length of postponement to read scoreboard when client spawns, whose unit
     * is second
     */
    private static final double READ_POSTPONEMENT = 1.0;

    /**
     * Title of scoreboard in a Bed Wars game
     */
    private static final String BW_SCOREBOARD_TITLE = "BED WARS";

    /**
     * Text that only appears on the scoreboard when client is waiting for a
     * game to start
     */
    private static final String WAITING_BOARD_TEXT = "Mode: ";

    /**
     * Prompt client received in chat when an ordinary game starts
     */
    private static final String ORDINARY_START_TEXT =
            "\u00A7r\u00A7f\u00A7lBed Wars\u00A7r";

    /**
     * Prompt client received in chat when a game in Capture Mode starts
     */
    private static final String CAPTURE_START_TEXT =
            "\u00A7r\u00A7f\u00A7lTo win a game of Bed Wars Capture\u00A7r";

    /**
     * Prompt client received in chat when it rejoins a game
     */
    private static final String REJOIN_TEXT =
            "\u00A7e\u00A7lTo leave Bed Wars, type /lobby\u00A7r";

    /**
     * The only instance of this class
     */
    private static volatile GameDetector instance;

    /**
     * The {@code HypixelDetector} instance
     */
    private final HypixelDetector hypixelDetector;

    /**
     * {@code TickCounter} which defers reading of scoreboard
     */
    private TickCounter tickCounter;

    /**
     * {@code true} when client is currently in a Bed Wars game
     */
    private boolean inBedWars;

    /**
     * If this is {@code true}, {@code GuiOpenEvent} where
     * {@code GuiDownloadTerrain} is loaded will be ignored once
     */
    private boolean ignoreGuiOpenEvent;

    /**
     * {@code true} when client is in a Bed Wars server where a game is about
     * to start
     */
    private boolean waitingToStart;

    /**
     * Implementation of Singleton design pattern, which allows only one
     * instance of this class to be created.
     */
    private GameDetector() {
        hypixelDetector = HypixelDetector.getInstance();
        tickCounter = new TickCounter(READ_POSTPONEMENT);
        tickCounter.stop();
    }

    /**
     * Returns the instance of this class.
     *
     * @return the instance of this class
     */
    public static GameDetector getInstance() {
        if (instance == null) {
            synchronized (GameDetector.class) {
                if (instance == null) {
                    instance = new GameDetector();
                }
            }
        }
        return instance;
    }

    /**
     * Returns {@code true} when the client is currently in a Bed Wars game, or
     * {@code false} otherwise.
     *
     * @return {@code true} when the client is currently in a Bed Wars game
     */
    public boolean isIn() {
        return inBedWars;
    }

    /**
     * When client spawns on a Hypixel server, prepares to read scoreboard.
     * <p>
     * Because {@code EntityJoinWorldEvent} is fired before client actually
     * spawns and gets the scoreboard, we need to postpone reading of
     * scoreboard for a few seconds.
     *
     * @param event the event fired when client spawns
     * @see #update(TickCounterTimeUpEvent)
     */
    public void prepareToReadScoreboard(EntityJoinWorldEvent event) {
        if (hypixelDetector.isIn() && event.getEntity()
                == Minecraft.getMinecraft().thePlayer) {
            tickCounter.reset();
            tickCounter.start();
        }
    }

    /**
     * Updates whether client is in a Bed Wars game when it transfers from one
     * server to another.
     * <p>
     * The screen with dirt background will show up. when client transfers.
     * <p>
     * If client leaves game, fires a {@link ClientLeaveGameEvent} on this
     * mod's event bus.
     *
     * @param event the event fired when the screen with dirt background shows up
     * @see ClientLeaveGameEvent
     * @see EventManager#EVENT_BUS
     */
    public void update(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiDownloadTerrain) {
            /*
            If client is currently in a Bed Wars game, this means it must
            be leaving the game.
            If client is not in a Bed Wars game, we will try to detect if
            it is joining a Bed Wars game using other code.
             */
            if (ignoreGuiOpenEvent) {
                    /*
                    When client makes initial connection to server,
                    GuiOpenEvent is fired after EntityJoinWorldEvent, which
                    unexpectedly stops the timer and prevents detection of game
                    session on first join. To fix this, the following boolean
                    is introduced so that on the first join, the timer is not
                    stopped.
                     */
                ignoreGuiOpenEvent = false;
            } else {
                inBedWars = false;
                tickCounter.stop();
                EventManager.EVENT_BUS.post(new ClientLeaveGameEvent());
            }
        }
    }

    /**
     * Updates whether client is in a Bed Wars game upon connection change.
     * <p>
     * When client leaves game, fires a {@link ClientLeaveGameEvent} on this
     * mod's event bus.
     *
     * @param event the event fired when client joins or leaves a server
     * @see ClientLeaveGameEvent
     * @see EventManager#EVENT_BUS
     */
    public void update(FMLNetworkEvent event) {
        if (event instanceof ClientConnectedToServerEvent) {
            /*
            Because of difference between orders of events fired when client
            connects to a server and when it transfers between servers, we
            would want the GuiOpenEvent fired straight after client's join to
            server to be ignored.
             */
            ignoreGuiOpenEvent = true;
        } else if (event instanceof ClientDisconnectionFromServerEvent) {
            /*
            GuiOpenEvent will not be called when client disconnects, so we need
            to detect ClientDisconnectionFromServerEvent
             */
            inBedWars = false;
            tickCounter.stop();
            EventManager.EVENT_BUS.post(new ClientLeaveGameEvent());
        }
    }

    /**
     * Updates whether client is in a Bed Wars game by reading the scoreboard
     * when it is created and ready to be read.
     *
     * @param event the event fired when it is long enough for scoreboard to be
     *         ready
     * @see #prepareToReadScoreboard(EntityJoinWorldEvent)
     * @see EventManager#EVENT_BUS
     */
    public void update(TickCounterTimeUpEvent event) {
        if (event.getExpiringCounter() == tickCounter) {
            // Timer created by this instance expires
            tickCounter.stop();
            if (hypixelDetector.isIn() && ScoreboardReader.getTitle(true)
                    .contains(BW_SCOREBOARD_TITLE)) {
                // Scoreboard's title shows client is in Bed Wars lobby, waiting
                // for a game to start, or in game
                if (ScoreboardReader.contains(WAITING_BOARD_TEXT, true)) {
                    // Client is waiting for a Bed Wars game to start
                    waitingToStart = true;
                }
                // The case when client rejoins a game is detected on receiving
                // chat message
                // Nothing needed to do when client is in lobby
            }
        }
    }

    /**
     * Updates whether client is in a Bed Wars game if client joins a game that
     * is about to start.
     * <p>
     * When the game starts, fires a {@link GameStartEvent} on this mod's
     * event bus.
     * <p>
     * If client is rejoining a Bed Wars game, fires a
     * {@link ClientRejoinGameEvent} on this mod's event bus.
     *
     * @param event the event fired when client receives a chat message
     * @see GameStartEvent
     * @see ClientRejoinGameEvent
     * @see EventManager#EVENT_BUS
     */
    public void update(ClientChatReceivedEvent event) {
        String message = event.getMessage().getFormattedText();
        if (waitingToStart && (message.contains(ORDINARY_START_TEXT)
                || message.contains(CAPTURE_START_TEXT))) {
            // A Bed Wars game starts
            waitingToStart = false;
            inBedWars = true;
            EventManager.EVENT_BUS.post(new GameStartEvent());
        } else if (message.contains(REJOIN_TEXT)) {
            // Client rejoins a Bed Wars game
            inBedWars = true;
            EventManager.EVENT_BUS.post(new ClientRejoinGameEvent());
        }
    }
}
