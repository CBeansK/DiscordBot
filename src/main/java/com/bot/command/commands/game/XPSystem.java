package com.bot.command.commands.game;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class XPSystem {

    // These will store xp and total times for now
    // TODO: add these to db
    private HashMap<Member, Integer> playerXp = new HashMap<>();
    private HashMap<Member, Integer> playerTimes = new HashMap<>();

    public int getPlayerXp(Member member) {
        return playerXp.get(member);
    }

    public void setPlayerXp(Member member, int value){
        playerXp.put(member, value);
    }


    // Not used right now, but can be used for testing
    public int getPlayerTimes(Member member) {
        return playerTimes.get(member);
    }

    public void setPlayerTimes(Member member, int value) {
        playerTimes.put(member, value);
    }

    // Generates a random amount of xp in a given range
    private int genRandomXp(int min, int max){
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }

    /*
    * This is where you can determine whether or not someone can earn xp
    * Useful for capping levels, punishing the unworthy, and moderation
     */
    public boolean canGetXp(Member member){
        if (!playerTimes.containsKey(member)){

            // Initializes base value for a member
            playerTimes.put(member, 0);
            playerXp.put(member, 0);
            return true;
        }
        // I think a string works here, not sure
        for (Role role : member.getRoles()){
            if (role.getName().equals("XP")){ return true; }
        }
        return false;
    }

    /*
     * This is called whenever a user enters a channel
     * Needs to run as long as they are connected to the channel
     */
    public void startTimer(Member member){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (member.getVoiceState().inVoiceChannel()){
                    setPlayerXp(member, getPlayerXp(member) + genRandomXp(5, 15));
                }
                else {
                    cancel();
                }
            }
        };
        // We don't want the xp to accrue immediately, but instead after the first tick
        timer.schedule(task, 1000, 1000);
    }
}
