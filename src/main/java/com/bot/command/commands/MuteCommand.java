package com.bot.command.commands;

import com.bot.command.CommandContext;
import com.bot.command.util.Utilities;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/*
*   @class MuteCommand
*   Command to temporarily mute a member. Can only be called if the author and bot have permissions
 */
public class MuteCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        Timer timer = new Timer();

        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();
        final Guild guild = ctx.getGuild();

        // Check if arguments are valid
        if (args.size() < 1 || message.getMentionedMembers().isEmpty()){
            channel.sendMessage("Missing arguments.\n" +
                    "Usage: !!mute @target [seconds]").queue();
            return;
        }

        // Check if user has permissions to mute
        if (!member.hasPermission(Permission.VOICE_MUTE_OTHERS)){
            channel.sendMessage("You dont have permission to mute others.").queue();
            return;
        }

        final Member target = message.getMentionedMembers().get(0);

        // Check if bot has permissions to mute
        if (!ctx.getSelfMember().hasPermission(Permission.VOICE_MUTE_OTHERS) || !ctx.getSelfMember().canInteract(target)){
            channel.sendMessage("I don't have permission to mute you.").queue();
            return;
        }

        // Check if member is in a voice channel
        if (!target.getVoiceState().inVoiceChannel()){
            channel.sendMessage("The target needs to be in a voice channel to be muted.").queue();
            return;
        }
        // Check if member is already muted
        if (target.getVoiceState().isMuted()){
            channel.sendMessage("This person is already muted.").queue();
            return;
        }

        // default time
        int time = 60;
        try {
            time = Integer.parseInt(args.get(1));
        } catch (NumberFormatException e){
            channel.sendMessage("Please enter a valid amount of time").queue();
            return;
        }

        Utilities.tempMute(guild, channel, target, time);
    }

    @Override
    public String getName() {
        return "mute";
    }

    @Override
    public String getHelp() {
        return "Mutes a user for a specific amount of time.\n"
                + "If no time is specified, the default time will be 1 minute.\n"
                + "Usage: !!mute @target [seconds]";
    }
}
