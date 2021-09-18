package com.bot.command.util;

import com.bot.command.CommandContext;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Utilities {
    public static void tempMute(Guild guild, TextChannel channel, Member target, int time){
        Timer timer = new Timer();
        // Get amount of time

        // Storing the amounts of time that people are muted
        final HashMap<Member, Integer> counter = new HashMap<>();
        counter.put(target, 0);

        // ensure target is able to be muted
        if (!target.getVoiceState().inVoiceChannel()){
            channel.sendMessage(
                    String.format("%s is not in a voice channel. I can't mute them.", target.getEffectiveName())
            ).queue();
        }

        // Mute the target
        channel.sendMessage(String.format("Muting %s for %d seconds", target.getEffectiveName(), time)).queue();
        guild.mute(target, true).queue();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                counter.put(target, counter.get(target) + 1);
                guild.mute(target, false).queue();

                if (counter.get(target) == 2){
                    this.cancel();
                }
            }
        };
        timer.schedule(task, 0, time * 1000L);

    }
}
