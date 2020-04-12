package com.bot.command.commands;

import com.bot.Config;
import com.bot.command.CommandContext;
import com.bot.command.CommandManager;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class HelpCommand implements ICommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();

        if (args.isEmpty()){
            StringBuilder builder = new StringBuilder();

            builder.append("List of commands:\n");

            manager.getCommands().stream().map(ICommand::getName).forEach(
                    (it) -> builder.append('`').append(Config.get("prefix")).append(it).append("`\n")
            );

            channel.sendMessage(builder.toString()).queue();
            return;
        }
        // Handle invalid commands
        else{
            ICommand cmd = manager.getCommand(args.get(0));

            if (cmd == null){
                channel.sendMessage("Not a valid command.").queue();
            } else {
                channel.sendMessage(cmd.getHelp()).queue();
            }

        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null){
            channel.sendMessageFormat("Nothing found for %s", search).queue();
            return;
        }
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Shows a list of commands the bot can use\n" +
                "Usage: '!!help [command]";
    }

    @Override
    public List<String> getAliases() {
        return List.of("commands", "cmds", "commandList");
    }
}
