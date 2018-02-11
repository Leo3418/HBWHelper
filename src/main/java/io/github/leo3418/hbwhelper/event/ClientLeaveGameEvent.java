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

package io.github.leo3418.hbwhelper.event;

import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * The event fired when client leaves a running Bed Wars game.
 * <p>
 * There are two circumstances where this event is fired:
 * <ol>
 * <li>Client leaves a Bed Wars server and returns to lobby;</li>
 * <li>Client disconnects from Hypixel.</li>
 * </ol>
 *
 * @author Leo
 */
public class ClientLeaveGameEvent extends Event {
}
