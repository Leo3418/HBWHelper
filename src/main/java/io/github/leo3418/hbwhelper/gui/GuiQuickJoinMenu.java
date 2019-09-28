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

package io.github.leo3418.hbwhelper.gui;

import io.github.leo3418.hbwhelper.ConfigManager;
import io.github.leo3418.hbwhelper.HbwHelper;
import io.github.leo3418.hbwhelper.KeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * The GUI of this mod for the Bed Wars quick join feature.
 *
 * @author Leo
 */
public final class GuiQuickJoinMenu extends GuiScreen {
    /**
     * Height of the title of this GUI
     */
    private static final int TITLE_HEIGHT = 40;

    /**
     * Vertical distance between borders of two adjacent buttons
     */
    private static final int BUTTONS_INTERVAL = 4;

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
     * Height of buttons on this GUI
     */
    private static final int BUTTON_HEIGHT = 20;

    /**
     * Vertical distance between the top of a button and the top of the button
     * right below it
     */
    private static final int BUTTONS_TRANSLATION_INTERVAL =
            BUTTON_HEIGHT + BUTTONS_INTERVAL;

    /**
     * Constructs a new {@link GuiQuickJoinMenu} instance.
     */
    public GuiQuickJoinMenu() {
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
        List<QuickJoinMenuButton> buttons = new ArrayList<QuickJoinMenuButton>(2);
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
    public void initGui() {
        int buttonX = (this.width - LONG_BUTTON_WIDTH) / 2;
        int firstButtonY = this.height / 4 + BUTTONS_INTERVAL;
        // Number of lines of buttons added so far, used for calculating height
        // of the next button
        int line = 0;
        this.buttonList.add(new QuickJoinMenuButton(
                QuickJoinMenuButton.Variant.LONG,
                buttonX, firstButtonY + (line++ * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.solo"), this,
                CommandAction.PLAY_SOLO
        ));
        this.buttonList.add(new QuickJoinMenuButton(
                QuickJoinMenuButton.Variant.LONG,
                buttonX, firstButtonY + (line++ * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.doubles"), this,
                CommandAction.PLAY_DOUBLES
        ));
        this.buttonList.add(new QuickJoinMenuButton(
                QuickJoinMenuButton.Variant.LONG,
                buttonX, firstButtonY + (line++ * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.3v3v3v3"), this,
                CommandAction.PLAY_3V3V3V3
        ));
        this.buttonList.add(new QuickJoinMenuButton(
                QuickJoinMenuButton.Variant.LONG,
                buttonX, firstButtonY + (line++ * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.4v4v4v4"), this,
                CommandAction.PLAY_4V4V4V4
        ));
        this.buttonList.add(new QuickJoinMenuButton(
                QuickJoinMenuButton.Variant.LONG,
                buttonX, firstButtonY + (line++ * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.4v4"), this,
                CommandAction.PLAY_4V4
        ));
        this.buttonList.addAll(getDreamButtons(buttonX,
                firstButtonY + (line++ * BUTTONS_TRANSLATION_INTERVAL)));
        // The following should always be the last button
        this.buttonList.add(new QuickJoinMenuButton(
                QuickJoinMenuButton.Variant.LONG,
                buttonX, firstButtonY + (line * BUTTONS_TRANSLATION_INTERVAL),
                I18n.format("hbwhelper.quickJoinGui.backToGame"), this
        ));
    }

    /**
     * Draws this GUI on the screen.
     *
     * @param mouseX horizontal location of the mouse
     * @param mouseY vertical location of the mouse
     * @param partialTicks number of partial ticks
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj,
                I18n.format("hbwhelper.quickJoinGui.title"),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * When a button on this GUI is clicked, performs the action the button
     * defines.
     *
     * @param button the button being clicked
     */
    @Override
    protected void actionPerformed(GuiButton button) {
        if (button instanceof QuickJoinMenuButton) {
            ((QuickJoinMenuButton) button).performAction();
        }
    }

    /**
     * When a key is typed when this GUI is shown, checks which key is pressed,
     * and closes this GUI if client presses {@code ESC} or the hot key for Bed
     * Wars quick join.
     *
     * @param typedChar the character typed
     * @param keyCode the code of the key pressed
     */
    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE
                || keyCode == KeyBindings.QUICK_JOIN.getKeyCode()) {
            close();
        }
    }

    /**
     * Closes this GUI.
     */
    private void close() {
        mc.displayGuiScreen(null);
        mc.setIngameFocus();
    }

    /**
     * Enumeration of {@link QuickJoinMenuButton.Action} implementations that
     * runs a Minecraft command when a {@link QuickJoinMenuButton} is pressed.
     */
    private enum CommandAction implements QuickJoinMenuButton.Action {
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
         * Constructs a new constant of {@link QuickJoinMenuButton.Action}
         * implementation that runs a Minecraft command.
         *
         * @param command the command to be run when this {@code CommandAction}
         *         is performed
         */
        CommandAction(String command) {
            this.command = command;
        }

        /**
         * Runs the command defined for this {@link QuickJoinMenuButton.Action}
         * implementation.
         */
        @Override
        public void perform() {
            Minecraft.getMinecraft().thePlayer.sendChatMessage(command);
        }
    }

    /**
     * Enumeration of {@link QuickJoinMenuButton.Action} implementations that
     * shows a prompt in chat messages when a {@link QuickJoinMenuButton} is
     * pressed.
     */
    private enum PromptAction implements QuickJoinMenuButton.Action {
        /**
         * The prompt that reminds the user to select the current game variant
         * for the Bed Wars Dream mode
         */
        SET_DREAM_MODE(new TextComponentTranslation(
                "hbwhelper.messages.setDreamMode", HbwHelper.NAME));

        /**
         * The prompt being shown when this {@code PromptAction} is performed
         */
        private final ITextComponent prompt;

        /**
         * Constructs a new constant of {@link QuickJoinMenuButton.Action}
         * implementation that shows a prompt in chat messages.
         *
         * @param prompt the prompt being shown when this {@code PromptAction}
         *         is performed
         */
        PromptAction(ITextComponent prompt) {
            this.prompt = prompt;
        }

        /**
         * Shows the prompt defined for this {@link QuickJoinMenuButton.Action}
         * implementation.
         */
        @Override
        public void perform() {
            Minecraft.getMinecraft().thePlayer.addChatMessage(prompt);
        }
    }

    /**
     * A button shown on this GUI screen.
     * <p>
     * The original {@link GuiButton} class tracks an numeric ID for a button
     * created from the class. Client programs of the class should use the ID
     * to differentiate which button is pressed and perform corresponding
     * actions.
     * <p>
     * This class extends the {@code GuiButton} class by making the action a
     * property of a button, so its client program can define a button's action
     * in a more simple way.
     */
    private static class QuickJoinMenuButton extends GuiButton {
        /**
         * Cache of an {@link Action Action} object which defines no action for
         * a button
         */
        private static final Action NO_ACTION = new Action() {
            @Override
            public void perform() {
            }
        };

        /**
         * The {@link Action Action} performed when this button is pressed
         */
        private final Action action;

        /**
         * The {@link GuiQuickJoinMenu} in which this button is
         */
        private final GuiQuickJoinMenu parent;

        /**
         * Constructs a new button for this GUI that does not perform any
         * action when pressed.
         *
         * @param variant the {@link Variant Variant} of the button's width
         * @param x the horizontal location of this button on the GUI
         * @param y the vertical location of this button on the GUI
         * @param buttonText the text shown on this button
         * @param parent the {@link GuiQuickJoinMenu} in which this button is
         */
        private QuickJoinMenuButton(Variant variant, int x, int y,
                                    String buttonText,
                                    GuiQuickJoinMenu parent) {
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
         * @param parent the {@link GuiQuickJoinMenu} in which this button is
         * @param action the {@link Action Action} performed when this button is
         *         pressed
         */
        private QuickJoinMenuButton(Variant variant, int x, int y,
                                    String buttonText,
                                    GuiQuickJoinMenu parent,
                                    Action action) {
            super(0, x, y, variant.width, BUTTON_HEIGHT, buttonText);
            this.parent = parent;
            this.action = action;
        }

        /**
         * Performs the {@link Action} defined for this button, then closes
         * this GUI.
         */
        private void performAction() {
            action.perform();
            parent.close();
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

        /**
         * The interface for classes that define the action that will be
         * performed when the button is pressed.
         * <p>
         * Any class that implements this interface should put code that
         * performs the action into its implementation for the
         * {@link Action#perform()} method.
         */
        private interface Action {
            /**
             * Runs the action that should be performed when the button is
             * pressed.
             */
            void perform();
        }
    }
}
