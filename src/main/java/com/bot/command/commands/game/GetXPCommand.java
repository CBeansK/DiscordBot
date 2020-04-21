package com.bot.command.commands.game;

import com.bot.command.CommandContext;
import com.bot.command.commands.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class GetXPCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member member = ctx.getMember();

        // Need to figure out a way to access the xp system and retrieve player data
        channel.sendMessage("still testing").queue();
    }

    @Override
    public String getName() {
        return "getxp";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
