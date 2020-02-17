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

package io.github.leo3418.hbwhelper.event;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * The event fired when client is being teleported to an in-progress Bed Wars
 * game server, but the teleport is cancelled because client has already
 * connected to the server or the server does not accept more new player.
 *
 * @author Leo
 */
public final class TeleportCancelledEvent extends Event {
    /**
     * Constructs a new {@code TeleportCancelledEvent}.
     */
    public TeleportCancelledEvent() {
    }
}
