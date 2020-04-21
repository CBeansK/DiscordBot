package com.bot.command;

import com.bot.Config;
import com.bot.command.commands.*;
import com.bot.command.commands.fun.MoleCommand;
import com.bot.command.commands.fun.RouletteCommand;
import com.bot.command.commands.fun.TbowCommand;
import com.bot.command.commands.music.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/*
    @class CommandManager
    Manages list of commands with functions to get, add, and handle commands.
 */
public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    /*
    *   Constructor builds list of all commands.
    *   Any new commands should be added here so the bot knows they exist
    *   and can handle them.
     */
    public CommandManager() {

        // Basic commands
        addCommand(new PingCommand());
        addCommand(new HelpCommand(this));
        addCommand(new PasteCommand());

        // Admin
        addCommand(new KickCommand());
        addCommand(new MuteCommand());

        // Games
        addCommand(new RouletteCommand());
        addCommand(new MoleCommand());
        addCommand(new TbowCommand());

        // VC Controls
        addCommand(new JoinCommand());
        addCommand(new LeaveCommand());

        // Music
        addCommand(new PlayCommand());
        addCommand(new PauseCommand());
        addCommand(new ResumeCommand());
        addCommand(new SkipCommand());
        addCommand(new VolumeCommand());
    }

    // adds a new command to the list
    private void addCommand(ICommand cmd) {
        // Check if command already exists
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        // just checking for duplicate commands
        if (nameFound){
            throw new IllegalArgumentException("A command with this name is already present");

        }

        commands.add(cmd);

    }

    // Return all commands
    public List<ICommand> getCommands(){
        return commands;
    }

    // Gets a command from the list
    // We also check for any aliases the command may have
    @Nullable
    public ICommand getCommand(String search){
        String searchLower = search.toLowerCase();

        for (ICommand cmd: this.commands){
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)){
                return cmd;
            }
        }

        return null;
    }

    /*
    *   Handler function for events.
     */
    public void handle(GuildMessageReceivedEvent event){

        // First, we break the message into the command and arguments
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Config.get("prefix")), "")
                .split("\\s+");

        // Then, we get the command from the list if it exists
        String invoke = split[0].toLowerCase();
        ICommand cmd = getCommand(invoke);

        // Now, we build a CommandContext from the command message, passing the arguments
        if (cmd != null){
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
        }
    }


}
