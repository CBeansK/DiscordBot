package com.bot.command.commands.music;

import com.bot.Config;
import com.bot.command.CommandContext;
import com.bot.command.commands.ICommand;

public class PlayCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();

        manager.loadAndPlay(ctx.getChannel(), ctx.getArgs().get(0));

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
