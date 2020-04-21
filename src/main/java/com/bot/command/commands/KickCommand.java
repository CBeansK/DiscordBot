package com.bot.command.commands;

import com.bot.command.CommandContext;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

/*
*   @class KickCommand
*   Kicks a member from the guild if the author and bot have permissions
 */
public class KickCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();

        // need to make sure we have someone to kick
        if (args.size() < 2 || message.getMentionedMembers().isEmpty()){
            channel.sendMessage("Missing arguments").queue();
            return;
        }

        final Member target = message.getMentionedMembers().get(0);

        // Checking permissions for author
        if (!member.canInteract(target) || !member.hasPermission(Permission.KICK_MEMBERS)){
            channel.sendMessage("You don't have permission to kick this person").queue();
            return;
        }

        final Member selfMember = ctx.getSelfMember();

        // Checking permissions for bot
        if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.KICK_MEMBERS)){
            channel.sendMessage("I am missing the permissions to kick people :(").queue();
            return;
        }

        // Optional reason for kick
        final String reason = String.join(" ", args.subList(1, args.size()));

        ctx.getGuild()
                .kick(target, reason)
                .reason(reason)
                .queue(
                        (__) -> channel.sendMessage("Member kicked").queue(),
                        (error) -> channel.sendMessageFormat("Could not kick %s",error.getMessage()).queue()
                );
    }


    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getHelp() {
        return "kick a member from the server.\n" +
                "Usage: `!!kick <@user> <reason>`";
    }
}
