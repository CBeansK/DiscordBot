package com.bot.command.commands;

import com.bot.command.CommandContext;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Random;

public class TbowCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        Random random = new Random();

        int rand = random.nextInt(1800);
        if (rand < 1){
            channel.sendMessage("You got tbow! Congratulations!").queue();
        }else{
            channel.sendMessage("You didn't get tbow, sucks to suck").queue();
        }
    }

    @Override
    public String getName() {
        return "tbow";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
