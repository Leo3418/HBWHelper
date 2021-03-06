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

package io.github.leo3418.hbwhelper;

import io.github.leo3418.hbwhelper.event.*;
import io.github.leo3418.hbwhelper.game.GameManager;
import io.github.leo3418.hbwhelper.game.GameTypeDetector;
import io.github.leo3418.hbwhelper.gui.HudGui;
import io.github.leo3418.hbwhelper.gui.QuickJoinMenuScreen;
import io.github.leo3418.hbwhelper.util.GameDetector;
import io.github.leo3418.hbwhelper.util.HypixelDetector;
import io.github.leo3418.hbwhelper.util.InProgressGameDetector;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.BusBuilder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Objects;

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
    public static final IEventBus EVENT_BUS = BusBuilder.builder().build();

    /**
     * {@link ITextComponent} object storing prompt being shown when client
     * rejoins a Bed Wars game it was in before after Minecraft restarts
     */
    private static final ITextComponent CLIENT_RESTART_PROMPT =
            new TranslationTextComponent("hbwhelper.messages.clientRestart",
                    HbwHelper.NAME);

    /**
     * {@link ITextComponent} object storing prompt being shown when client
     * rejoins a Bed Wars game it was in before, but Minecraft has not been
     * restarted
     */
    private static final ITextComponent CLIENT_REJOIN_PROMPT =
            new TranslationTextComponent("hbwhelper.messages.clientRejoin",
                    HbwHelper.NAME);

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
     * The {@link HudGui} instance
     */
    private final HudGui hudGui;

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
        hudGui = HudGui.getInstance();
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
    public void onClientPlayerNetworkEvent(ClientPlayerNetworkEvent event) {
        hypixelDetector.update(event);
        gameDetector.update(event);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onGuiOpen(GuiOpenEvent event) {
        gameDetector.update(event);
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
        hudGui.render(event);
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
        PlayerEntity player =
                Objects.requireNonNull(Minecraft.getInstance().player);
        if (GameManager.getInstance() == null) {
            // Client is rejoining a Bed Wars game after restart of Minecraft
            player.sendMessage(CLIENT_RESTART_PROMPT, Util.NIL_UUID);
            gameTypeDetector.startDetection();
        } else {
            // Client is rejoining a Bed Wars game, but Minecraft is not closed
            player.sendMessage(CLIENT_REJOIN_PROMPT, Util.NIL_UUID);
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
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (hypixelDetector.isIn() && KeyBindings.QUICK_JOIN.consumeClick()) {
            Minecraft.getInstance().setScreen(new QuickJoinMenuScreen());
        }
    }
}
