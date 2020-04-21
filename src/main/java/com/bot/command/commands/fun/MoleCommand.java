package com.bot.command.commands.fun;

import com.bot.command.CommandContext;
import com.bot.command.commands.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Random;
/*
*   @class MoleCommand
*   Provides a ratio of flower to tobacco for your sweet moles
 */
public class MoleCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();

        Random random = new Random();
        int baccus = (random.nextInt(5) + 5) * 5;
        int weed = 100 - baccus;

        channel.sendMessage(String.format("Baccus: %1$s     Bud: %2$s", baccus, weed)).queue();

    }

    @Override
    public String getName() {
        return "mole";
    }

    @Override
    public String getHelp() {
        return "Provides a ratio of tobacco to flower to use in your moles\n"+
                "Usage: !!mole";
    }
}
