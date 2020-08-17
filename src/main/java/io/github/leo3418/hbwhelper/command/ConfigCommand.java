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

package io.github.leo3418.hbwhelper.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.github.leo3418.hbwhelper.ConfigManager;
import io.github.leo3418.hbwhelper.HbwHelper;
import io.github.leo3418.hbwhelper.game.DreamMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Objects;
import java.util.Optional;
import java.util.function.IntConsumer;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

/**
 * The command for viewing and changing this mod's configuration. This command
 * is a temporary solution for the absent of a mod configuration GUI framework
 * in Minecraft Forge.
 *
 * @author Leo
 */
public class ConfigCommand {
    /**
     * The literal of this command
     */
    public static final String LITERAL = "hbwhs";

    /**
     * The literal for the child command that shows description of this mod's
     * configuration options
     */
    public static final String LITERAL_DESC = "desc";

    /**
     * The literal for the child command that changes an option's value
     */
    public static final String LITERAL_SET = "set";

    /**
     * The argument name of the option name parameter
     */
    private static final String ARG_NAME_OPTION = "option";

    /**
     * The argument name of the parameter for an option's new value
     */
    private static final String ARG_NAME_VALUE = "value";

    /**
     * The {@link ConfigManager} instance
     */
    private static final ConfigManager CMI = ConfigManager.getInstance();

    /**
     * Prevents instantiation of this class.
     */
    private ConfigCommand() {
    }

    /**
     * Registers this command in a {@link CommandDispatcher}.
     *
     * @param dispatcher the command dispatcher for command registration
     * @throws NullPointerException if {@code dispatcher == null}
     */
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal(LITERAL)
                .then(Desc.register())
                .then(Set.register())
                .requires(commandSource -> true)
                .executes(context -> {
                    printConfig();
                    return SINGLE_SUCCESS;
                })
        );
    }

    /**
     * Prints the current configuration of this mod in chat message.
     */
    private static void printConfig() {
        printString();
        printTranslation("hbwhelper.configGui.title", HbwHelper.NAME);
        printString();
        printTranslation("commands.hbwhelper.keyValueList");
        printString("showGenerationTimes - " + CMI.showGenerationTimes());
        printString("showTeamUpgrades - " + CMI.showTeamUpgrades());
        printString("showArmorInfo - " + CMI.showArmorInfo());
        printString("showEffectsInfo - " + CMI.showEffectsInfo());
        printString("alwaysShowEffects - " + CMI.alwaysShowEffects());
        printString("hudX - " + CMI.hudX());
        printString("hudY - " + CMI.hudY());
        printString("currentDreamMode - " + CMI.currentDreamMode().toDisplayName());
        printString();
        printTranslation("commands.hbwhelper.descHelp", LITERAL, LITERAL_DESC);
        printTranslation("commands.hbwhelper.entryDescHelp", LITERAL, LITERAL_DESC);
        printTranslation("commands.hbwhelper.changeConfigGeneralHelp", LITERAL, LITERAL_SET);
    }

    /**
     * Prints an empty line in chat message.
     */
    private static void printString() {
        printString("");
    }

    /**
     * Prints a string in chat message.
     *
     * @param string the string to be printed
     * @throws NullPointerException if any argument is {@code null}
     */
    private static void printString(String string) {
        Objects.requireNonNull(string, "string");
        Objects.requireNonNull(Minecraft.getInstance().player).sendMessage(
                new StringTextComponent(string));
    }

    /**
     * Prints a translated string in chat message.
     *
     * @param translationKey the translation key of the string
     * @param translationArgs any translation arguments of the string
     * @throws NullPointerException if any argument is {@code null}
     */
    private static void printTranslation(String translationKey,
                                         Object... translationArgs) {
        Objects.requireNonNull(translationKey, "translationKey");
        Objects.requireNonNull(translationArgs, "translationArgs");
        Objects.requireNonNull(Minecraft.getInstance().player).sendMessage(
                new TranslationTextComponent(translationKey, translationArgs));
    }

    /**
     * Returns a new {@link CommandSyntaxException} instance with the specified
     * error message.
     *
     * @param message the error message of the exception
     * @return a new {@code CommandSyntaxException} instance with the specified
     *         error message
     * @throws NullPointerException if any argument is {@code null}
     */
    private static CommandSyntaxException newSyntaxException(Message message) {
        Objects.requireNonNull(message, "message");
        return new CommandSyntaxException(
                new SimpleCommandExceptionType(message), message);
    }

    /**
     * The child command that shows description of this mod's configuration
     * options.
     */
    private static class Desc {
        /**
         * Returns an {@link ArgumentBuilder} instance that registers this
         * command to a parent command.
         *
         * @return an {@code ArgumentBuilder} instance that registers this
         *         command to a parent command
         */
        private static ArgumentBuilder<CommandSource, ?> register() {
            return Commands.literal(LITERAL_DESC)
                    .then(Commands.argument(ARG_NAME_OPTION, StringArgumentType.word())
                            .requires(commandSource -> true)
                            .executes(context -> {
                                String option = StringArgumentType
                                        .getString(context, ARG_NAME_OPTION);
                                printDescOf(option);
                                return SINGLE_SUCCESS;
                            })
                    )
                    .requires(commandSource -> true)
                    .executes(context -> {
                        printAllDesc();
                        return SINGLE_SUCCESS;
                    });
        }

        /**
         * Prints the description of every option of this mod's configuration.
         */
        private static void printAllDesc() {
            printString();
            printTranslation("hbwhelper.configGui.title", HbwHelper.NAME);
            printString();
            printTranslation("commands.hbwhelper.keyDescList");
            printString("showGenerationTimes - " + I18n.format("hbwhelper.configGui.showGenerationTimes.title"));
            printString("showTeamUpgrades - " + I18n.format("hbwhelper.configGui.showTeamUpgrades.title"));
            printString("showArmorInfo - " + I18n.format("hbwhelper.configGui.showArmorInfo.title"));
            printString("showEffectsInfo - " + I18n.format("hbwhelper.configGui.showEffectsInfo.title"));
            printString("alwaysShowEffects - " + I18n.format("hbwhelper.configGui.alwaysShowEffects.title"));
            printString("hudX - " + I18n.format("hbwhelper.configGui.hudX.title"));
            printString("hudY - " + I18n.format("hbwhelper.configGui.hudY.title"));
            printString("currentDreamMode - " + I18n.format("hbwhelper.configGui.currentDreamMode.title"));
            printString();
            printTranslation("commands.hbwhelper.entryDescHelp", LITERAL, LITERAL_DESC);
            printTranslation("commands.hbwhelper.changeConfigGeneralHelp", LITERAL, LITERAL_SET);
        }

        /**
         * Prints the description of a single option of this mod's
         * configuration.
         *
         * @param option the name of the option
         * @throws NullPointerException if any argument is {@code null}
         * @throws CommandSyntaxException if the specified option is invalid
         */
        private static void printDescOf(String option)
                throws CommandSyntaxException {
            Objects.requireNonNull(option, "option");
            Optional<Class<?>> type = ConfigManager.getClassOf(option);
            if (!type.isPresent()) {
                throw newSyntaxException(new TranslationTextComponent(
                        "commands.hbwhelper.invalidOption", option));
            }
            printString();
            printString(option + " - " +
                    I18n.format("hbwhelper.configGui." + option + ".title"));
            printTranslation("hbwhelper.configGui." + option + ".description");
            if (type.get() == Boolean.TYPE) {
                boolean value;
                switch (option) {
                    case "showGenerationTimes":
                        value = CMI.showGenerationTimes();
                        break;
                    case "showTeamUpgrades":
                        value = CMI.showTeamUpgrades();
                        break;
                    case "showArmorInfo":
                        value = CMI.showArmorInfo();
                        break;
                    case "showEffectsInfo":
                        value = CMI.showEffectsInfo();
                        break;
                    case "alwaysShowEffects":
                        value = CMI.alwaysShowEffects();
                        break;
                    default:
                        throw new AssertionError(
                                "New configuration option with boolean value added: " + option);
                }
                printTranslation("commands.hbwhelper.currentValue", value);
                printTranslation("commands.hbwhelper.allowedValues",
                        true + ", " + false);
            } else if (type.get() == Integer.TYPE) {
                int value;
                int min;
                int max;
                switch (option) {
                    case "hudX":
                        value = CMI.hudX();
                        min = 0;
                        max = CMI.maxHudX();
                        break;
                    case "hudY":
                        value = CMI.hudY();
                        min = 0;
                        max = CMI.maxHudY();
                        break;
                    default:
                        throw new AssertionError(
                                "New configuration option with int value added: " + option);
                }
                printTranslation("commands.hbwhelper.currentValue", value);
                printTranslation("commands.hbwhelper.allowedRanges", min, max);
            } else if (type.get() == DreamMode.class) {
                printTranslation("commands.hbwhelper.currentValue",
                        CMI.currentDreamMode().toDisplayName());
                StringBuilder builder = new StringBuilder();
                for (String displayName : DreamMode.displayNames()) {
                    if (builder.length() > 0) {
                        builder.append(", ");
                    }
                    builder.append(displayName);
                }
                printTranslation("commands.hbwhelper.allowedValues",
                        builder.toString());
            } else {
                throw new AssertionError("New configuration option with type "
                        + type.get() + " added");
            }
            printTranslation("commands.hbwhelper.changeConfigSpecificHelp",
                    LITERAL, LITERAL_SET, option);
        }
    }

    /**
     * The child command that changes an option's value.
     */
    private static class Set {
        /**
         * Returns an {@link ArgumentBuilder} instance that registers this
         * command to a parent command.
         *
         * @return an {@code ArgumentBuilder} instance that registers this
         *         command to a parent command
         */
        private static ArgumentBuilder<CommandSource, ?> register() {
            return Commands.literal(LITERAL_SET).then(Commands.argument(
                    ARG_NAME_OPTION, StringArgumentType.word())
                    .then(Commands.argument(ARG_NAME_VALUE,
                            StringArgumentType.greedyString())
                            .requires(commandSource -> true)
                            .executes(context -> {
                                String option = StringArgumentType
                                        .getString(context, ARG_NAME_OPTION);
                                String newValue = StringArgumentType
                                        .getString(context, ARG_NAME_VALUE);
                                changeValueOf(option, newValue);
                                return SINGLE_SUCCESS;
                            })));
        }

        /**
         * Changes the value of an option.
         *
         * @param option the name of the option
         * @param newValue the new value for the option
         * @throws NullPointerException if any argument is {@code null}
         * @throws CommandSyntaxException if the specified option or new value
         *         is invalid
         */
        private static void changeValueOf(String option, String newValue)
                throws CommandSyntaxException {
            Objects.requireNonNull(option, "option");
            Objects.requireNonNull(newValue, "newValue");
            Optional<Class<?>> type = ConfigManager.getClassOf(option);
            if (!type.isPresent()) {
                throw newSyntaxException(new TranslationTextComponent(
                        "commands.hbwhelper.invalidOption", option));
            }
            if (type.get() == Boolean.TYPE) {
                if (!(newValue.equals(Boolean.toString(true))
                        || newValue.equals(Boolean.toString(false)))) {
                    throw newSyntaxException(new TranslationTextComponent(
                            "commands.hbwhelper.invalidValue", newValue));
                }
                CMI.changeBoolean(option, Boolean.parseBoolean(newValue));
            } else if (type.get() == Integer.TYPE) {
                int newInt;
                try {
                    newInt = Integer.parseInt(newValue);
                } catch (NumberFormatException e) {
                    throw newSyntaxException(new TranslationTextComponent(
                            "commands.hbwhelper.invalidValue", newValue));
                }
                int min;
                int max;
                IntConsumer valueConsumer;
                switch (option) {
                    case "hudX":
                        min = 0;
                        max = CMI.maxHudX();
                        valueConsumer = CMI::changeHudX;
                        break;
                    case "hudY":
                        min = 0;
                        max = CMI.maxHudY();
                        valueConsumer = CMI::changeHudY;
                        break;
                    default:
                        throw new AssertionError(
                                "New configuration option with int value added: " + option);
                }
                if (newInt < min || newInt > max) {
                    throw newSyntaxException(new TranslationTextComponent(
                            "commands.hbwhelper.invalidValue", newValue));
                }
                valueConsumer.accept(newInt);
            } else if (type.get() == DreamMode.class) {
                DreamMode newMode = DreamMode.valueOfDisplayName(newValue)
                        .orElseThrow(() -> newSyntaxException(
                                new TranslationTextComponent(
                                        "commands.hbwhelper.invalidValue", newValue)));
                CMI.changeCurrentDreamMode(newMode);
            } else {
                throw new AssertionError("New configuration option with type "
                        + type.get() + " added");
            }
            printTranslation("commands.hbwhelper.changeSuccess",
                    option, newValue);
        }
    }
}
