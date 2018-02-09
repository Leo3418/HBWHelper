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
     * {@code true} when client is currently in a Bed Wars game
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
        if (inBedWars && event.getGui() instanceof GuiDownloadTerrain) {
            inBedWars = false;
            EventManager.EVENT_BUS.post(new ClientLeaveGameEvent());
        }
    }

    /**
     * Updates whether client is in a Bed Wars game upon connection change.
     * <p>
     * Because {@code GuiOpenEvent} will not be called when
     * client disconnects, this method is implemented to detect
     * {@code ClientDisconnectionFromServerEvent}, which is fired when client
     * disconnects.
     * <p>
     * If client leaves game, fires a {@link ClientLeaveGameEvent} on this
     * mod's event bus.
     *
     * @param event the event fired when client joins or leaves a server
     * @see ClientLeaveGameEvent
     * @see EventManager#EVENT_BUS
     */
    public void update(FMLNetworkEvent event) {
        if (inBedWars && event instanceof ClientDisconnectionFromServerEvent) {
            /*
            GuiOpenEvent will not be called when client disconnects, so we need
            to detect ClientDisconnectionFromServerEvent
             */
            inBedWars = false;
            EventManager.EVENT_BUS.post(new ClientLeaveGameEvent());
        }
    }

    /**
     * Updates whether client is in a Bed Wars game by analyzing chat message
     * client receives.
     * <p>
     * If a Bed Wars game starts, fires a {@link GameStartEvent} on this mod's
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
        if (hypixelDetector.isIn() && !inBedWars) {
            String message = event.getMessage().getFormattedText();
            if ((message.contains(ORDINARY_START_TEXT)
                    || message.contains(CAPTURE_START_TEXT))) {
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
