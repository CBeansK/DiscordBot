package com.bot.command.commands.music;

import com.bot.command.CommandContext;
import com.bot.command.commands.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

public class PauseCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();
        TextChannel channel = ctx.getChannel();

        if (manager.getGuildMusicManager(ctx.getGuild()).player.isPaused()){
            channel.sendMessage("I am not playing music right now.").queue();
            return;
        }

        channel.sendMessage("Pausing music.").queue();

        manager.pause(manager.getGuildMusicManager(ctx.getGuild()));
    }

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
