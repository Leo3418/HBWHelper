/*
 * Copyright (C) 2018-2021 Leo3418 <https://github.com/Leo3418>
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

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;

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
     * This method should be called whenever a {@link ClientPlayerNetworkEvent}
     * is fired.
     *
     * @param event the event fired when client joins or leaves a server
     */
    public void update(ClientPlayerNetworkEvent event) {
        if (event instanceof ClientPlayerNetworkEvent.LoggedInEvent) {
            ServerData currentServer = Minecraft.getInstance()
                    .getCurrentServer();
            if (currentServer != null) {
                String serverAddress = currentServer.ip.toLowerCase();
                inHypixel = serverAddress.contains(HYPIXEL_DOMAIN);
            }
        } else if (event instanceof ClientPlayerNetworkEvent.LoggedOutEvent) {
            inHypixel = false;
        }
    }
}
