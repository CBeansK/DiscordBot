package com.bot.command.commands.music;

import com.bot.command.CommandContext;
import com.bot.command.commands.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

public class VolumeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        PlayerManager manager = PlayerManager.getInstance();
        TextChannel channel = ctx.getChannel();

        // make sure args exist
        if (ctx.getArgs().size() < 1) {
            channel.sendMessage("You need to specify a volume from 0-100").queue();
        }

        // change volume
        int currentVolume = manager.getGuildMusicManager(ctx.getGuild()).player.getVolume();
        manager.getGuildMusicManager(ctx.getGuild()).player.setVolume(tryParseVolume(ctx.getArgs().get(0), currentVolume, channel));
    }

    // all the validation logic for the new volume is handled here
    private int tryParseVolume(String value, int currentVolume, TextChannel channel) {
        try {
            int ret = Integer.parseInt(value);

            // if the value is the same then don't need to do anything
            if (ret == currentVolume) {
                channel.sendMessage("This is the current volume.").queue();
                return currentVolume;
            }

            // argument must be between 0 and 100
            if (ret < 0 || ret > 100) {
                channel.sendMessage("You need to specify a volume from 0-100").queue();
                return currentVolume;
            }

            // return if everything checks out
            return ret;

        } catch (NumberFormatException e){
            // argument needs to be a number
            channel.sendMessage("You need to specify a volume from 0-100").queue();
            return currentVolume;
        }
    }

    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
