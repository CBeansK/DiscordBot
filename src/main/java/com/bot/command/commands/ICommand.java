package com.bot.command.commands;

import com.bot.command.CommandContext;

import java.util.List;

// Basic command interface with handler methods
public interface ICommand {
    void handle(CommandContext ctx);

    String getName();

    String getHelp();

    default List<String> getAliases(){
        return List.of();
    }
}
