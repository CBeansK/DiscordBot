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

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

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

    private void addCommand(ICommand cmd) {
        // Check if command already exists
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound){
            throw new IllegalArgumentException("A command with this name is already present");

        }

        commands.add(cmd);

    }

    public List<ICommand> getCommands(){
        return commands;
    }

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

    public void handle(GuildMessageReceivedEvent event){
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Config.get("prefix")), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = getCommand(invoke);

        if (cmd != null){
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
        }
    }


}
