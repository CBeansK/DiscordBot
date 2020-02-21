package com.bot.command.commands;

import com.bot.command.CommandContext;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue(
                (ping) -> ctx.getChannel()
                .sendMessageFormat("Rest ping: %sms", ping).queue()
        );
    }

    @Override
    public String getHelp() {
        return "Shows the current ping from the bot to discord servers";
    }

    @Override
    public String getName() {
        return "ping";
    }
}
