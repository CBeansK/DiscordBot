package com.bot.command.commands;

import com.bot.command.CommandContext;

import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx);

    String getName();

    String getHelp();

    default List<String> getAliases(){
        return List.of();
    }
}
