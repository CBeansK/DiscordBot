package com.bot.command.commands.music;

import com.bot.command.CommandContext;
import com.bot.command.commands.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

public class ResumeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();
        TextChannel channel = ctx.getChannel();

        if (!manager.getGuildMusicManager(ctx.getGuild()).player.isPaused()) {
            channel.sendMessage("I am already playing music.").queue();
            return;
        }

        channel.sendMessage("Resuming music.").queue();

        manager.resume(manager.getGuildMusicManager(ctx.getGuild()));
    }

    @Override
    public String getName() {
        return "resume";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
