/*
 * Copyright (C) 2018-2020 Leo3418 <https://github.com/Leo3418>
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

package io.github.leo3418.hbwhelper.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.leo3418.hbwhelper.ConfigManager;
import io.github.leo3418.hbwhelper.HbwHelper;
import io.github.leo3418.hbwhelper.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.github.leo3418.hbwhelper.gui.ButtonParameters.*;

/**
 * The GUI of this mod for the Bed Wars quick join feature.
 *
 * @author Leo
 */
public final class QuickJoinMenuScreen extends Screen {
    /**
     * Distance between this GUI's title and the top of the screen
     */
    private static final int TITLE_HEIGHT = 40;

    /**
     * Width of long buttons on this GUI
     */
    private static final int LONG_BUTTON_WIDTH = 200;

    /**
     * Width of short buttons on this GUI
     */
    private static final int SHORT_BUTTON_WIDTH =
            (LONG_BUTTON_WIDTH - BUTTONS_INTERVAL) / 2;

    /**
     * Constructs a new {@link QuickJoinMenuScreen} instance.
     */
    public QuickJoinMenuScreen() {
        super(new TranslationTextComponent("hbwhelper.quickJoinGui.title"));
    }

    /**
     * Returns a {@link List} of buttons related to Dream mode games. These
     * buttons will either allow the user to join a Dream mode game or prompt
     * the user to select the current Dream mode on Hypixel in mod settings.
     * These buttons occupy a single row on this GUI.
     *
     * @param x width from the left edge of the Minecraft window to the left
     *         edge of the button if the {@code List} will only have one button,
     *         or the left edge of the first button on the left if the
     *         {@code List} will have multiple buttons
     * @param y height from the top edge of the Minecraft window to the top
     *         edges of the buttons
     * @return the {@code List} of buttons related to Dream mode games
     */
    private List<QuickJoinMenuButton> getDreamButtons(int x, int y) {
        List<QuickJoinMenuButton> buttons = new ArrayList<>(2);
        switch (ConfigManager.getInstance().currentDreamMode()) {
            case RUSH:
                buttons.add(new QuickJoinMenuButton(
                        QuickJoinMenuButton.Variant.SHORT,
                        x, y,
                        I18n.format("hbwhelper.dream.rush") + " " +
                                I18n.format("hbwhelper.quickJoinGui.doubles"),
                        this,
                        CommandAction.PLAY_RUSH_DOUBLES));
                buttons.add(new QuickJoinMenuButton(
                        QuickJoinMenuButton.Variant.SHORT,
                        x + SHORT_BUTTON_WIDTH + BUTTONS_INTERVAL, y,
                        I18n.format("hbwhelper.dream.rush") + " " +
                                I18n.format("hbwhelper.quickJoinGui.4v4v4v4"),
                        this,
                        CommandAction.PLAY_RUSH_4V4V4V4));
                return buttons;
            case ULTIMATE:
                buttons.add(new QuickJoinMenuButton(
                        QuickJoinMenuButton.Variant.SHORT,
                        x, y,
                        I18n.format("hbwhelper.dream.ultimate") + " " +
                                I18n.format("hbwhelper.quickJoinGui.doubles"),
                        this,
                        CommandAction.PLAY_ULTIMATE_DOUBLES));
                buttons.add(new QuickJoinMenuButton(
                        QuickJoinMenuButton.Variant.SHORT,
                        x + SHORT_BUTTON_WIDTH + BUTTONS_INTERVAL, y,
                        I18n.format("hbwhelper.dream.ultimate") + " " +
                                I18n.format("hbwhelper.quickJoinGui.4v4v4v4"),
                        this,
                        CommandAction.PLAY_ULTIMATE_4V4V4V4));
                return buttons;
            case CASTLE:
                buttons.add(new QuickJoinMenuButton(
                        QuickJoinMenuButton.Variant.LONG, x, y,
                        I18n.format("hbwhelper.dream.castle"),
                        this,
                        CommandAction.PLAY_CASTLE));
                return buttons;
            case LUCKY_BLOCKS:
                buttons.add(new QuickJoinMenuButton(
                        QuickJoinMenuButton.Variant.SHORT,
                        x, y,
                        // Translation key `hbwhelper.quickJoinGui.luckyBlocks`
                        // is used instead of `hbwhelper.dream.luckyBlocks` to
                        // avoid long text not fitting into a short button
                        I18n.format("hbwhelper.quickJoinGui.luckyBlocks") + " " +
                                I18n.format("hbwhelper.quickJoinGui.doubles"),
                        this,
                        CommandAction.PLAY_LUCKY_BLOCKS_DOUBLES));
                buttons.add(new QuickJoinMenuButton(
                        QuickJoinMenuButton.Variant.SHORT,
                        x + SHORT_BUTTON_WIDTH + BUTTONS_INTERVAL, y,
                        I18n.format("hbwhelper.quickJoinGui.luckyBlocks") + " " +
                                I18n.format("hbwhelper.quickJoinGui.4v4v4v4"),
                        this,
                        CommandAction.PLAY_LUCKY_BLOCKS_4V4V4V4));
                return buttons;
            case VOIDLESS:
                buttons.add(new QuickJoinMenuButton(
                        QuickJoinMenuButton.Variant.SHORT,
                        x, y,
                        I18n.format("hbwhelper.dream.voidless") + " " +
                                I18n.format("hbwhelper.quickJoinGui.doubles"),
                        this,
                        CommandAction.PLAY_VOIDLESS_DOUBLES));
                buttons.add(new QuickJoinMenuButton(
                        QuickJoinMenuButton.Variant.SHORT,
                        x + SHORT_BUTTON_WIDTH + BUTTONS_INTERVAL, y,
                        I18n.format("hbwhelper.dream.voidless") + " " +
                                I18n.format("hbwhelper.quickJoinGui.4v4v4v4"),
                        this,
                        CommandAction.PLAY_VOIDLESS_4V4V4V4));
                return buttons;
            case ARMED:
                buttons.add(new QuickJoinMenuButton(
                        QuickJoinMenuButton.Variant.SHORT,
                        x, y,
                        I18n.format("hbwhelper.dream.armed") + " " +
                                I18n.format("hbwhelper.quickJoinGui.doubles"),
                        this,
                        CommandAction.PLAY_ARMED_DOUBLES));
                buttons.add(new QuickJoinMenuButton(
                        QuickJoinMenuButton.Variant.SHORT,
                        x + SHORT_BUTTON_WIDTH + BUTTONS_INTERVAL, y,
                        I18n.format("hbwhelper.dream.armed") + " " +
                                I18n.format("hbwhelper.quickJoinGui.4v4v4v4"),
                        this,
                        CommandAction.PLAY_ARMED_4V4V4V4));
                return buttons;
            default:
                buttons.add(new QuickJoinMenuButton(
                        QuickJoinMenuButton.Variant.LONG, x, y,
                        I18n.format("hbwhelper.quickJoinGui.dream"),
                        this,
                        PromptAction.SET_DREAM_MODE));
                return buttons;
        }
    }

    /**
     * Initializes this GUI with buttons.
     */
    @Override
    @SuppressWarnings("ConstantConditions")
    protected void init() {
        int buttonX = (this.width - LONG_BUTTON_WIDTH) / 2;
        int firstButtonY = this.height / 4 + BUTTONS_INTERVAL;
        // Number of lines of buttons added so far, used for calculating height
        // of the next button
        int line = 0;
        addButton(new QuickJoinMenuButton(
                QuickJoinMenuButton.Variant.LONG,
                buttonX, firstButtonY + (line++ * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.solo"), this,
                CommandAction.PLAY_SOLO
        ));
        addButton(new QuickJoinMenuButton(
                QuickJoinMenuButton.Variant.LONG,
                buttonX, firstButtonY + (line++ * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.doubles"), this,
                CommandAction.PLAY_DOUBLES
        ));
        addButton(new QuickJoinMenuButton(
                QuickJoinMenuButton.Variant.LONG,
                buttonX, firstButtonY + (line++ * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.3v3v3v3"), this,
                CommandAction.PLAY_3V3V3V3
        ));
        addButton(new QuickJoinMenuButton(
                QuickJoinMenuButton.Variant.LONG,
                buttonX, firstButtonY + (line++ * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.4v4v4v4"), this,
                CommandAction.PLAY_4V4V4V4
        ));
        addButton(new QuickJoinMenuButton(
                QuickJoinMenuButton.Variant.LONG,
                buttonX, firstButtonY + (line++ * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.4v4"), this,
                CommandAction.PLAY_4V4
        ));
        getDreamButtons(buttonX,
                firstButtonY + (line++ * BUTTONS_TRANSLATION_INTERVAL))
                .forEach(this::addButton);
        // The following should always be the last button
        addButton(new QuickJoinMenuButton(
                QuickJoinMenuButton.Variant.LONG,
                buttonX, firstButtonY + (line * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.backToGame"), this
        ));
    }

    /**
     * Draws this GUI on the screen.
     *
     * @param matrixStack the matrix stack
     * @param mouseX horizontal location of the mouse
     * @param mouseY vertical location of the mouse
     * @param partialTicks number of partial ticks
     */
    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void render(@Nonnull MatrixStack matrixStack,
                       int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        drawCenteredString(matrixStack, this.font, this.title.getString(),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    /**
     * When a key is typed when this GUI is shown, checks which key is pressed,
     * and closes this GUI if client presses {@code ESC} or the hot key for Bed
     * Wars quick join.
     *
     * @param keyCode1 the key code of the first key being pressed
     * @param keyCode2 the key code of the second key being pressed
     * @param keyCode3 the key code of the third key being pressed
     * @return whether the key press is handled by this GUI
     */
    @Override
    public boolean keyPressed(int keyCode1, int keyCode2, int keyCode3) {
        int quickJoinKeyCode = KeyBindings.QUICK_JOIN.getKey().getKeyCode();
        if (quickJoinKeyCode == keyCode1
                || quickJoinKeyCode == keyCode2
                || quickJoinKeyCode == keyCode3) {
            Objects.requireNonNull(minecraft).displayGuiScreen(null);
            return true;
        } else {
            return super.keyPressed(keyCode1, keyCode2, keyCode3);
        }
    }

    /**
     * Enumeration of {@link Button.IPressable} implementations that runs a
     * Minecraft command when a {@link QuickJoinMenuButton} is pressed.
     */
    private enum CommandAction implements Button.IPressable {
        PLAY_SOLO("/play bedwars_eight_one"),
        PLAY_DOUBLES("/play bedwars_eight_two"),
        PLAY_3V3V3V3("/play bedwars_four_three"),
        PLAY_4V4V4V4("/play bedwars_four_four"),
        PLAY_4V4("/play bedwars_two_four"),
        PLAY_RUSH_DOUBLES("/play bedwars_eight_two_rush"),
        PLAY_RUSH_4V4V4V4("/play bedwars_four_four_rush"),
        PLAY_ULTIMATE_DOUBLES("/play bedwars_eight_two_ultimate"),
        PLAY_ULTIMATE_4V4V4V4("/play bedwars_four_four_ultimate"),
        PLAY_CASTLE("/play bedwars_castle"),
        PLAY_LUCKY_BLOCKS_DOUBLES("/play bedwars_eight_two_lucky"),
        PLAY_LUCKY_BLOCKS_4V4V4V4("/play bedwars_four_four_lucky"),
        PLAY_VOIDLESS_DOUBLES("/play bedwars_eight_two_voidless"),
        PLAY_VOIDLESS_4V4V4V4("/play bedwars_four_four_voidless"),
        PLAY_ARMED_DOUBLES("/play bedwars_eight_two_armed"),
        PLAY_ARMED_4V4V4V4("/play bedwars_four_four_armed");

        /**
         * The command being run when this {@code CommandAction} is performed
         */
        private final String command;

        /**
         * Constructs a new constant of {@link Button.IPressable}
         * implementation that runs a Minecraft command.
         *
         * @param command the command to be run when this {@code CommandAction}
         *         is performed
         */
        CommandAction(String command) {
            this.command = command;
        }

        /**
         * Runs the command defined for this {@link Button.IPressable}
         * implementation.
         *
         * @param button the {@link Button} being pressed
         */
        @Override
        public void onPress(@Nonnull Button button) {
            Objects.requireNonNull(Minecraft.getInstance().player)
                    .sendChatMessage(command);
        }
    }

    /**
     * Enumeration of {@link Button.IPressable} implementations that
     * shows a prompt in chat messages when a {@link QuickJoinMenuButton} is
     * pressed.
     */
    private enum PromptAction implements Button.IPressable {
        /**
         * The prompt that reminds the user to select the current game variant
         * for the Bed Wars Dream mode
         */
        SET_DREAM_MODE(new TranslationTextComponent(
                "hbwhelper.messages.setDreamMode", HbwHelper.NAME));

        /**
         * The prompt being shown when this {@code PromptAction} is performed
         */
        private final ITextComponent prompt;

        /**
         * Constructs a new constant of {@link Button.IPressable}
         * implementation that shows a prompt in chat messages.
         *
         * @param prompt the prompt being shown when this {@code PromptAction}
         *         is performed
         */
        PromptAction(ITextComponent prompt) {
            this.prompt = prompt;
        }

        /**
         * Shows the prompt defined for this {@link Button.IPressable}
         * implementation.
         *
         * @param button the {@link Button} being pressed
         */
        @Override
        public void onPress(@Nonnull Button button) {
            ClientPlayerEntity player =
                    Objects.requireNonNull(Minecraft.getInstance().player);
            player.sendMessage(prompt,
                    PlayerEntity.getUUID(player.getGameProfile()));
        }
    }

    /**
     * A button shown on this GUI screen.
     */
    private static class QuickJoinMenuButton extends Button {
        /**
         * Cache of a {@link Button.IPressable} object which defines no action
         * for a button
         */
        private static final IPressable NO_ACTION = button -> {
        };

        /**
         * The {@link QuickJoinMenuScreen} in which this button is
         */
        private final QuickJoinMenuScreen parent;

        /**
         * Constructs a new button for this GUI that does not perform any
         * action when pressed.
         *
         * @param variant the {@link Variant Variant} of the button's width
         * @param x the horizontal location of this button on the GUI
         * @param y the vertical location of this button on the GUI
         * @param buttonText the text shown on this button
         * @param parent the {@link QuickJoinMenuScreen} in which this button is
         */
        private QuickJoinMenuButton(Variant variant, int x, int y,
                                    String buttonText,
                                    QuickJoinMenuScreen parent) {
            this(variant, x, y, buttonText, parent, NO_ACTION);
        }

        /**
         * Constructs a new button for this GUI that performs an action when
         * pressed.
         *
         * @param variant the {@link Variant Variant} of the button's width
         * @param x the horizontal location of this button on the GUI
         * @param y the vertical location of this button on the GUI
         * @param buttonText the text shown on this button
         * @param parent the {@link QuickJoinMenuScreen} in which this button is
         * @param action the {@linkplain Button.IPressable action} performed
         *         when this button is pressed
         */
        private QuickJoinMenuButton(Variant variant, int x, int y,
                                    String buttonText,
                                    QuickJoinMenuScreen parent,
                                    IPressable action) {
            super(x, y, variant.width, BUTTON_HEIGHT,
                    new StringTextComponent(buttonText), action);
            this.parent = parent;
        }

        /**
         * Runs any action defined for this button. This method is called when
         * this button is clicked.
         */
        @Override
        public void onPress() {
            super.onPress();
            Objects.requireNonNull(parent.minecraft).displayGuiScreen(null);
        }

        /**
         * Enumeration of variants of the button.
         */
        private enum Variant {
            /**
             * Long button, whose width is the same as a default Minecraft
             * button
             */
            LONG(LONG_BUTTON_WIDTH),
            /**
             * Short button, whose width times 2 plus the default interval
             * between two Minecraft buttons would be the same as a long button
             */
            SHORT(SHORT_BUTTON_WIDTH);

            /**
             * Width of a button of this variant
             */
            private final int width;

            /**
             * Constructs a new constant of variants of the button.
             *
             * @param width the width of a button of this variant
             */
            Variant(int width) {
                this.width = width;
            }
        }
    }
}
