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

package io.github.leo3418.hbwhelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.ClientChatEvent;

import java.util.Collections;
import java.util.Objects;

/**
 * The command manager of this mod, for processing this mod's commands.
 * <p>
 * With Minecraft 1.13, Mojang introduced
 * <a href="https://github.com/Mojang/brigadier">Brigadier</a>, a new command
 * library for the game. Minecraft Forge provides ways to register commands with
 * this library on the server side; however, the commands of this mod are
 * intended to be registered on the client side. Neither Minecraft nor Forge
 * offers any utility for client-side command registration.
 * <p>
 * This class enables registration and processing of this mod's client-side
 * commands.
 * <p>
 * This is a Singleton class. Only one instance of this class may be created
 * per runtime.
 *
 * @author Leo
 */
public class CommandManager {
    /**
     * The prefix of all messages that are intended to be parsed as a command
     */
    private static final String COMMAND_PREFIX = "/";

    /**
     * The only instance of this class
     */
    private static final CommandManager INSTANCE = new CommandManager();

    /**
     * The {@link CommandDispatcher} used by the instance
     */
    private final CommandDispatcher<CommandSource> dispatcher;

    /**
     * Implementation of Singleton design pattern, which allows only one
     * instance of this class to be created.
     */
    private CommandManager() {
        this.dispatcher = new CommandDispatcher<>();
    }

    /**
     * Returns the instance of this class.
     *
     * @return the instance of this class
     */
    public static CommandManager getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the {@link CommandDispatcher} used by this instance.
     *
     * @return the {@code CommandDispatcher} used by this instance
     */
    public CommandDispatcher<CommandSource> getDispatcher() {
        return dispatcher;
    }

    /**
     * Processes a {@link ClientChatEvent} to see if the client has entered a
     * command that this object accepts. If the client does so, then executes
     * the command and cancels the event. Otherwise, this method does nothing.
     * If the event has already been cancelled before this method is called,
     * then this method does nothing, either.
     * <p>
     * This method should be called whenever a {@link ClientChatEvent} is fired.
     *
     * @param event the message for this object to process
     * @throws NullPointerException if {@code event == null}
     */
    public void process(ClientChatEvent event) {
        Objects.requireNonNull(event, "event");
        if (event.isCanceled()) {
            return;
        }
        String message = event.getOriginalMessage();
        if (!message.startsWith(COMMAND_PREFIX)) {
            return;
        }
        String command = message.substring(COMMAND_PREFIX.length());
        if (!registered(command)) {
            return;
        }
        CommandSource source =
                Minecraft.getInstance().player.getCommandSource();
        ParseResults<CommandSource> parseResults =
                dispatcher.parse(command, source);
        try {
            dispatcher.execute(parseResults);
        } catch (CommandException e) {
            source.sendErrorMessage(e.getComponent());
        } catch (CommandSyntaxException e) {
            source.sendErrorMessage(new StringTextComponent(e.getMessage()));
        }
        event.setCanceled(true);
    }

    /**
     * Returns whether a command has been registered in the
     * {@linkplain #dispatcher command dispatcher} used by this instance.
     *
     * @param command the command to be checked, without
     *         {@linkplain #COMMAND_PREFIX the prefix}
     * @return whether the command has been registered in the command dispatcher
     * @throws NullPointerException if {@code command == null}
     */
    private boolean registered(String command) {
        Objects.requireNonNull(command, "command");
        String literal = command.split(" ")[0];
        return dispatcher.findNode(Collections.singleton(literal)) != null;
    }
}
