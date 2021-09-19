package com.bot.command.commands.music;

import com.bot.Config;
import com.bot.command.CommandContext;
import com.bot.command.commands.ICommand;

public class PlayCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();

        if (!ctx.getSelfMember().getVoiceState().inVoiceChannel()){
            ctx.getChannel().sendMessage("Attempting to join channel...").queue();
            new JoinCommand().handle(ctx);
        }

        manager.loadAndPlay(ctx.getChannel(), ctx.getArgs());

        manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(Integer.parseInt(Config.get("volume")));
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
