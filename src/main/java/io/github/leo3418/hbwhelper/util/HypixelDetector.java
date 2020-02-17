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

package io.github.leo3418.hbwhelper.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

/**
 * Detects and tracks if client is connected to Hypixel.
 * <p>
 * This is a Singleton class. Only one instance of this class may be created
 * per runtime.
 *
 * @author Leo
 */
public class HypixelDetector {
    /**
     * Domain of Hypixel's servers
     */
    private static final String HYPIXEL_DOMAIN = "hypixel.net";

    /**
     * The only instance of this class
     */
    private static final HypixelDetector INSTANCE = new HypixelDetector();

    /**
     * Whether client is currently in Hypixel
     */
    private boolean inHypixel;

    /**
     * Implementation of Singleton design pattern, which allows only one
     * instance of this class to be created.
     */
    private HypixelDetector() {
    }

    /**
     * Returns the instance of this class.
     *
     * @return the instance of this class
     */
    public static HypixelDetector getInstance() {
        return INSTANCE;
    }

    /**
     * Returns whether client is currently in Hypixel.
     *
     * @return whether client is currently in Hypixel
     */
    public boolean isIn() {
        return inHypixel;
    }

    /**
     * When client connects to a server, checks and tracks if the server is
     * Hypixel. Or, when client disconnects from a server, remembers that
     * the player is no longer in Hypixel.
     * <p>
     * This method should be called whenever a {@link FMLNetworkEvent} is fired.
     *
     * @param event the event fired when client joins or leaves a server
     */
    public void update(FMLNetworkEvent event) {
        if (event instanceof ClientConnectedToServerEvent) {
            ServerData currentServer = Minecraft.getMinecraft()
                    .getCurrentServerData();
            if (currentServer != null) {
                String serverAddress = currentServer.serverIP.toLowerCase();
                inHypixel = serverAddress.contains(HYPIXEL_DOMAIN);
            }
        } else if (event instanceof ClientDisconnectionFromServerEvent) {
            inHypixel = false;
        }
    }
}
