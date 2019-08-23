/*
 * HBW Helper: Hypixel Bed Wars Helper Minecraft Forge Mod
 * Copyright (C) 2019 Leo3418
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
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;

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
     * When client joins a world, tests if the world is on a server; if yes,
     * checks and records if it is Hypixel.
     * <p>
     * This method should be called whenever an {@link EntityJoinWorldEvent} is
     * fired.
     *
     * @param event the event fired when client spawns
     */
    public void update(EntityJoinWorldEvent event) {
        if (event.getEntity() == Minecraft.getInstance().player) {
            ServerData currentServer = Minecraft.getInstance()
                    .getCurrentServerData();
            if (currentServer != null) {
                inHypixel = currentServer.serverIP.toLowerCase()
                        .contains(HYPIXEL_DOMAIN);
            } else {
                // Client is playing in single player mode
                inHypixel = false;
            }
        }
    }

    /**
     * When a world unloads, remembers that the player cannot be in Hypixel.
     * <p>
     * This method should be called whenever a {@link WorldEvent.Unload} is
     * fired.
     *
     * @param event the event fired when a world unloads
     */
    public void update(@SuppressWarnings("unused") WorldEvent.Unload event) {
        inHypixel = false;
    }
}
