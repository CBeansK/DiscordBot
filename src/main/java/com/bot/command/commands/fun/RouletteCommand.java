package com.bot.command.commands.fun;

import com.bot.command.CommandContext;
import com.bot.command.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/*
*   @class RouletteCommand
*   Russian Roulette game with multiple levels of difficulty
*   Easy: Loser is muted
*   Medium: Loser is kicked from the voice channel
*   Hard: Loser is kicked from the server
*
*   Potential improvements:
*   Balance
*   Temp mute on easy
*   multiple players at once in an elimination mode?
 */
public class RouletteCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        final User authorAsUser = ctx.getAuthor();
        final Member authorAsMember = ctx.getGuild().getMember(authorAsUser);
        final Member selfMember = ctx.getSelfMember();
        final Guild guild = ctx.getGuild();

        // Check arguments
        if (args.isEmpty()){
            channel.sendMessage(getHelp()).queue();
            return;
        }

        final String difficulty = args.get(0).toLowerCase();

        // Check permissions
        if (!selfMember.hasPermission(Permission.KICK_MEMBERS) ||
            !selfMember.hasPermission(Permission.BAN_MEMBERS) ||
            !selfMember.hasPermission(Permission.MANAGE_CHANNEL)){
                channel.sendMessage("Missing permissions").queue();
                return;
        }

        // Send start message
        channel.sendMessage("Roulette starting! You have 10 seconds say your last goodbyes").queue();

        // Start timer
        Timer timer = new Timer();
        timer.schedule(new Countdown(authorAsMember, difficulty, channel, guild), 10 * 1000);

    }

    // Counts down from the time the command was received to the time of punishment
    class Countdown extends TimerTask{

        private String difficulty;
        private Member author;
        private TextChannel channel;
        private Guild guild;

        public Countdown(Member author, String difficulty, TextChannel channel, Guild guild) {
            this.difficulty = difficulty;
            this.author = author;
            this.channel = channel;
            this.guild = guild;
        }

        @Override
        public void run() {


            // Author needs to be in a voice channel to get muted if they lose on easy
            if (difficulty.equals("easy") && author.getVoiceState().getChannel() == null){
                this.channel.sendMessage("You must be in a voice channel to use this difficulty").queue();
                return;
            }

            this.channel.sendMessage(doRoulette(this.author, this.difficulty, this.guild)).queue();
        }
    }

    // Handles the game logic i.e. the roll and difficulty, as well as punishment
    private String doRoulette(Member author, String difficulty, Guild guild) {

        // Change this if you want roulette to be more less likely to succeed
        final int ODDS = 10;

        double roll = Math.floor(Math.random() * ODDS);

        // Outcomes for each difficulty
        if (roll < 1){
            switch (difficulty){
                case "easy":
                    // TODO: add timed mute so people aren't muted forever
                    guild.mute(author, true).queue();
                    break;
                case "medium":
                    guild.moveVoiceMember(author, null).queue();
                    break;
                case "hard":
                    guild.kick(author, "You lost!").queue();
                    break;
            }
            return String.format("%s lost the roulette and has suffered the punishment!", author.getEffectiveName());
        } else{
            return "You survived this time. Might wanna try again";
        }
    }

    @Override
    public String getName() {
        return "roulette";
    }

    @Override
    public String getHelp() {
        return "Starts a roulette for a 'prize'\n" +
                "Usage: ```!!roulette [difficulty]\n{ easy, medium, hard }```";
    }
}
