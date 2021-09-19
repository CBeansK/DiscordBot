package com.bot.command.commands.fun;

import com.bot.command.CommandContext;
import com.bot.command.commands.ICommand;
import com.bot.util.UserData;
import com.bot.util.UserManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
/*
*   @class TbowCommand
*   Command to roll the odds of a twisted bow from Runescape.
*   Currently, user data such as total attempts and total successes are stored in memory
*   A database needs to be implemented with user data for persistence
 */
public class TbowCommand implements ICommand {

    // Hash maps to store data in memory
    private HashMap<User, Integer> tbowAttempts = new HashMap<>();
    private HashMap<User, Integer> tbowsReceived = new HashMap<>();

    @Override
    public void handle(CommandContext ctx) {
        final User author = ctx.getAuthor();
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        UserData data = UserManager.getUser(author.getIdLong());

        if (!args.isEmpty()){
            switch (args.get(0)){
                case "attempts":
                    int attempts = data.getTbowAttempts();
                    channel.sendMessage("Tbow attempts: " + attempts).queue();
                    break;
                case "attained":
                    int attained = data.getTbowsAttained();
                    channel.sendMessage("Tbows gotten: " + attained).queue();
                    break;
            }
            return;
        }

        Random random = new Random();
        int rand = random.nextInt(1800);

        if (rand < 1){
            channel.sendMessage("You got tbow! Congratulations!").queue();
            data.setTbowsAttained(data.getTbowsAttained() + 1);
        }else{
            channel.sendMessage("You didn't get tbow, sucks to suck").queue();
        }
        data.setTbowAttempts(data.getTbowAttempts() + 1);
    }


    @Override
    public String getName() {
        return "tbow";
    }

    @Override
    public String getHelp() {
        return "Makes a roll to see if you get a twisted bow.\n"
                +"Usage: !!tbow [args]\n"
                +"Arguments: 'attempts' 'got'";
    }
}
