package com.dipo33.chatcalc.command;

import com.dipo33.chatcalc.calc.NumberValue;
import com.dipo33.chatcalc.calc.Parser;
import com.dipo33.chatcalc.calc.ShuntingYard;
import com.dipo33.chatcalc.calc.element.IFormulaElement;
import com.dipo33.chatcalc.calc.exception.MissingOperandException;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import java.math.BigInteger;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CalcCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal("calc").requires(source -> source.hasPermission(2))
                .then(
                        Commands.argument("expression", StringArgumentType.greedyString())
                                .executes(CalcCommand::processCommand)
                );
    }

    public static int processCommand(CommandContext<CommandSourceStack> context) {
        var formula = context.getArgument("expression", String.class);
        CommandSourceStack source = context.getSource();
        if (source.getEntity() instanceof ServerPlayer player) {
            try {
                player.sendSystemMessage(Component.literal("§e§l---------------------------------------------"));
                player.sendSystemMessage(Component.literal("§b§lFormula: §f" + formula));

                List<IFormulaElement> elements = Parser.parse(formula);
                NumberValue result = ShuntingYard.evaluatePrefix(ShuntingYard.shuntingYard(elements)).displayRound();
                if (!result.isInteger()) {
                    player.sendSystemMessage(Component.literal("§a§lFraction: §f" + result.asFractionString()));
                }
                player.sendSystemMessage(Component.literal("§a§lDecimal: §f" + result.asDecimalString(44)));

                if (result.isInteger() && result.asInteger().compareTo(BigInteger.ZERO) > 0) {
                    player.sendSystemMessage(Component.literal("§a§lStacks: §f" + result.asStackString()));
                    player.sendSystemMessage(Component.literal("§a§lFluid: §f" + result.asFluidString()));
                    if (result.asInteger().compareTo(BigInteger.valueOf(630720000000L)) <= 0) {
                        player.sendSystemMessage(Component.literal("§a§lTime: §f" + result.asTimeString()));
                        player.sendSystemMessage(Component.literal("§a§lTick Time: §f" + result.asTickTimeString()));
                    }
                }
            } catch (MissingOperandException e) {
                player.sendSystemMessage(Component.literal("§c§lError: §fMissing operand"));
            } catch (Exception e) {
                player.sendSystemMessage(Component.literal("§c§lError: §f" + (e.getMessage() == null ? e.getClass().toString() : e.getMessage())));
            }
        } else {
            source.sendFailure(Component.literal("This command must be run by a player."));
        }

        return 0;
    }
}
