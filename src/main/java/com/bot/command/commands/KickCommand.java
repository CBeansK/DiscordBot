package com.bot.command.commands;

import com.bot.command.CommandContext;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class KickCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        System.out.println("command received");
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();

        if (args.size() < 2 || message.getMentionedMembers().isEmpty()){
            channel.sendMessage("Missing arguments").queue();
            return;
        }

        final Member target = message.getMentionedMembers().get(0);

        if (!member.canInteract(target) || !member.hasPermission(Permission.KICK_MEMBERS)){
            channel.sendMessage("You don't have permission to kick this person").queue();
            return;
        }

        final Member selfMember = ctx.getSelfMember();

        if (!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.KICK_MEMBERS)){
            channel.sendMessage("I am missing the permissions to kick people :(").queue();
            return;
        }

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
