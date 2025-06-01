package com.dipo33.chatcalc.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;

public class Commands {
    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context)
    {
        dispatcher.register(CalcCommand.create());
    }
}
