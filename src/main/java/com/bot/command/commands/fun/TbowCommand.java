package com.bot.command.commands;

import com.bot.command.CommandContext;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TbowCommand implements ICommand {

    private HashMap<User, Integer> tbowAttempts = new HashMap<>();
    private HashMap<User, Integer> tbowsReceived = new HashMap<>();

    @Override
    public void handle(CommandContext ctx) {
        final User author = ctx.getAuthor();
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();

        if (!args.isEmpty()){
            switch (args.get(0)){
                case "attempts":
                    channel.sendMessage("Tbow attempts: " + tbowAttempts.getOrDefault(author, 0)).queue();
                    break;
                case "got":
                    channel.sendMessage("Tbows gotten: " + tbowsReceived.getOrDefault(author, 0)).queue();
                    break;
            }
            return;
        }

        Random random = new Random();
        int rand = random.nextInt(1800);

        if (rand < 1){
            channel.sendMessage("You got tbow! Congratulations!").queue();
            tbowsReceived.put(author, tbowAttempts.getOrDefault(author, 0) + 1);

        }else{
            channel.sendMessage("You didn't get tbow, sucks to suck").queue();
        }
        tbowAttempts.put(author, tbowAttempts.getOrDefault(author, 0) + 1);
    }



    public int getTbowAttempts(User user) {
        return tbowAttempts.get(user);
    }

    public int getTbowsReceived(User user) {
        return tbowsReceived.get(user);
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
