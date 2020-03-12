package com.bot.command.commands.music;

import com.bot.command.CommandContext;
import com.bot.command.commands.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class LeaveCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        AudioManager audioManager = ctx.getGuild().getAudioManager();

        GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();

        VoiceChannel voiceChannel = audioManager.getConnectedChannel();

        if (!audioManager.isConnected()) {
            channel.sendMessage("I am not in a channel.").queue();
            return;
        }

        if (!voiceChannel.getMembers().contains(ctx.getMember())) {
            channel.sendMessage("You must be in the same voice channel as me to disconnect me.").queue();
            return;
        }

        audioManager.closeAudioConnection();
        channel.sendMessageFormat("Disconnected from %s", voiceChannel.getName()).queue();
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return "Makes the bot leave the current channel.";
    }
}
