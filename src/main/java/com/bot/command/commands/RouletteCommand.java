package com.bot.command.commands;

import com.bot.command.CommandContext;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;

public class RouletteCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        final Member member = ctx.getMember();
        final Member selfMember = ctx.getSelfMember();
        JDA jda = ctx.getJDA();


        if (args.isEmpty()){
            channel.sendMessage("Missing arguments").queue();
            return;
        }
        final String difficulty = args.get(0);

        if (!selfMember.hasPermission(Permission.KICK_MEMBERS) ||
            !selfMember.hasPermission(Permission.BAN_MEMBERS) ||
            !selfMember.hasPermission(Permission.MANAGE_CHANNEL)){
                channel.sendMessage("Missing permissions").queue();
                return;
        }

        List<Member> participants = new ArrayList<>();
        participants.add(member);
        channel.sendMessage("Roulette starting! You have 30 seconds to enter by clicking the emote below").queue(message ->
                message.addReaction("\uD83D\uDD2B").queue());


        Timer timer = new Timer();
        timer.schedule(new Countdown(participants,difficulty), 30 * 1000);
        //while the delay is occurring, listen for reactions on the message
    }

    class Countdown extends TimerTask{

        private List<Member> participants = new ArrayList<>();
        private String difficulty = new String();

        public Countdown(List<Member> participants, String difficulty) {
            this.participants = participants;
            this.difficulty = difficulty;
        }

        @Override
        public void run() {
            doRoulette(participants, difficulty);
        }
    }
    class ReactionListener extends ListenerAdapter{
        private String messageId = new String();
        private List<Member> participants = new ArrayList<>();

        public ReactionListener(String messageId, ArrayList<Member> participants){
            this.messageId = messageId;
            this.participants = participants;
        }
        public void onReactionReceived(GuildMessageReactionAddEvent event) {
            if (event.getReactionEmote().getName().equals("\uD83D\uDD2B") && !event.getMember().getUser().equals(event.getJDA())){
                participants.add(event.getMember());
            }

        }
    }

    private void doRoulette(List<Member> participants, String difficulty) {

        int index = (int)(Math.random() * (participants.size()));
        final Member target = participants.get(index);
        target.modifyNickname("big fat loser lmao");

        switch (difficulty){
            case "easy":
                if (target.getVoiceState().getChannel() != null){
                    target.mute(true);
                }
                break;
            case "medium":
                target.kick("You lost!");
                break;
            case "hard":
                target.ban(1, "You lost!");
                break;
        }
    }

    @Override
    public String getName() {
        return "roulette";
    }

    @Override
    public String getHelp() {
        return "Starts a roulette for a 'prize'\n" +
                "Usage: `!!roulette [difficulty]`";
    }
}
