package com.bot.command.commands.music;

import com.bot.command.CommandContext;
import com.bot.command.commands.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

public class SkipCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();
        TextChannel channel = ctx.getChannel();

        if (manager.getGuildMusicManager(ctx.getGuild()).player.getPlayingTrack() == null) {
            channel.sendMessage("I am not playing music right now.").queue();
            return;
        }

        channel.sendMessage("Skipping current song.").queue();
        manager.skip(manager.getGuildMusicManager(ctx.getGuild()));
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
