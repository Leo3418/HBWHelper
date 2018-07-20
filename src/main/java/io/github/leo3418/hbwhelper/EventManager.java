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

package io.github.leo3418.hbwhelper;

import io.github.leo3418.hbwhelper.event.*;
import io.github.leo3418.hbwhelper.game.GameManager;
import io.github.leo3418.hbwhelper.game.GameTypeDetector;
import io.github.leo3418.hbwhelper.gui.GuiHud;
import io.github.leo3418.hbwhelper.gui.GuiQuickJoinMenu;
import io.github.leo3418.hbwhelper.util.GameDetector;
import io.github.leo3418.hbwhelper.util.HypixelDetector;
import io.github.leo3418.hbwhelper.util.InProgressGameDetector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

/**
 * Event manager of this mod, which responds to events fired on
 * Minecraft Forge's event bus and this mod's {@link EventManager#EVENT_BUS
 * proprietary event bus}, and also holds that proprietary event bus.
 * <p>
 * This class is the place where most core behaviors of this mod is defined. It
 * decides what actions to take upon each kind of event, whereas other classes
 * define the actions' details and complete the actions.
 * <p>
 * This is a Singleton class. Only one instance of this class may be created
 * per runtime.
 *
 * @author Leo
 */
public class EventManager {
    /**
     * The proprietary event bus of this mod
     */
    public static final EventBus EVENT_BUS = new EventBus();

    /**
     * Prefix of prompt sent from this mod to the client
     */
    private static final String PROMPT_PREFIX = "[" + HbwHelper.NAME + "] ";

    /**
     * {@link ChatComponentText} object storing prompt being shown when client
     * rejoins a Bed Wars game it was in before after Minecraft restarts
     */
    private static final ChatComponentText CLIENT_RESTART_PROMPT =
            new ChatComponentText(PROMPT_PREFIX
                    + I18n.format("hbwhelper.messages.clientRestart"));

    /**
     * {@link ChatComponentText} object storing prompt being shown when client
     * rejoins a Bed Wars game it was in before, but Minecraft has not been
     * restarted
     */
    private static final ChatComponentText CLIENT_REJOIN_PROMPT =
            new ChatComponentText(PROMPT_PREFIX
                    + I18n.format("hbwhelper.messages.clientRejoin"));

    /**
     * The only instance of this class
     */
    private static final EventManager INSTANCE = new EventManager();

    /**
     * The {@link HypixelDetector} instance
     */
    private final HypixelDetector hypixelDetector;

    /**
     * The {@link GameDetector} instance
     */
    private final GameDetector gameDetector;

    /**
     * The {@link InProgressGameDetector} instance
     */
    private final InProgressGameDetector ipGameDetector;

    /**
     * The {@link GameTypeDetector} instance
     */
    private final GameTypeDetector gameTypeDetector;

    /**
     * The {@link ConfigManager} instance
     */
    private final ConfigManager configManager;

    /**
     * The {@link GuiHud} instance
     */
    private final GuiHud guiHud;

    /**
     * Whether the current {@link GameManager} instance returned by
     * {@link GameManager#getInstance()} should be cleared when client switches
     * to the next Bed Wars game
     * <p>
     * If this boolean's value is set to {@code true}, it should be changed to
     * {@code false} when one of the following conditions is satisfied:
     * <ul>
     * <li>Client leaves the current Bed Wars game and joins the next game</li>
     * <li>Client was being transferred to another in-progress Bed Wars game,
     * but the teleport is cancelled for whatever reason</li>
     * </ul>
     */
    private boolean shouldClearGMInstance;

    /**
     * Implementation of Singleton design pattern, which allows only one
     * instance of this class to be created.
     */
    private EventManager() {
        hypixelDetector = HypixelDetector.getInstance();
        gameDetector = GameDetector.getInstance();
        ipGameDetector = InProgressGameDetector.getInstance();
        gameTypeDetector = GameTypeDetector.getInstance();
        configManager = ConfigManager.getInstance();
        guiHud = GuiHud.getInstance();
    }

    /**
     * Returns the instance of this class.
     *
     * @return the instance of this class
     */
    static EventManager getInstance() {
        return INSTANCE;
    }

    /**
     * Registers this class as listener of Minecraft Forge's event bus and this
     * mod's {@link #EVENT_BUS proprietary event bus}.
     */
    void registerOnEventBus() {
        MinecraftForge.EVENT_BUS.register(this);
        EventManager.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onFMLNetworkEvent(FMLNetworkEvent event) {
        hypixelDetector.update(event);
        gameDetector.update(event);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onGuiOpen(GuiOpenEvent event) {
        gameDetector.update(event);
        configManager.update(event);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        gameDetector.update(event);
        ipGameDetector.detect(event);
        if (gameDetector.isIn() && GameManager.getInstance() != null) {
            GameManager.getInstance().update(event);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        guiHud.render(event);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onClientTick(TickEvent.ClientTickEvent event) {
        gameTypeDetector.detect();
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onGameStart(GameStartEvent event) {
        GameManager.clearInstance();
        gameTypeDetector.startDetection();
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onClientJoinIPGame(ClientJoinInProgressGameEvent event) {
        if (gameDetector.isIn()) {
            shouldClearGMInstance = true;
        } else {
            GameManager.clearInstance();
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onClientRejoinGame(ClientRejoinGameEvent event) {
        if (shouldClearGMInstance) {
            GameManager.clearInstance();
            shouldClearGMInstance = false;
        }
        if (GameManager.getInstance() == null) {
            // Client is rejoining a Bed Wars game after restart of Minecraft
            Minecraft.getMinecraft().thePlayer
                    .addChatMessage(CLIENT_RESTART_PROMPT);
            gameTypeDetector.startDetection();
        } else {
            // Client is rejoining a Bed Wars game, but Minecraft is not closed
            Minecraft.getMinecraft().thePlayer
                    .addChatMessage(CLIENT_REJOIN_PROMPT);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onClientLeaveGame(ClientLeaveGameEvent event) {
        gameTypeDetector.stopDetection();
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onGameTypeDetected(GameTypeDetectedEvent event) {
        GameManager.createInstance(event.getGameType());
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onTeleportCancelled(TeleportCancelledEvent event) {
        shouldClearGMInstance = false;
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        configManager.save(event);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (hypixelDetector.isIn() && KeyBindings.QUICK_JOIN.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiQuickJoinMenu());
        }
    }
}
