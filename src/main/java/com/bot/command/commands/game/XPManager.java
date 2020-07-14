package com.bot.command.commands.game;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class XPManager {

    // These will store xp and total times for now
    // TODO: add these to db
    private HashMap<Member, Integer> playerXp = new HashMap<>();
    private HashMap<Member, Integer> playerTimes = new HashMap<>();
    private HashMap<Member, Integer> levels = new HashMap<>();

    // Frequency of ticks for XP in seconds and range of XP earned each tick
    private int frequency;

    // Creates a new XP Manager with a frequency of XP gains in seconds
    public XPManager(int frequency){
        this.frequency = frequency;
    }

    public int getPlayerXp(Member member) {
        return playerXp.get(member);
    }

    public void setPlayerXp(Member member, int value){
        playerXp.put(member, value);
    }

    public void setPlayerLevel(Member member, int level){
        levels.put(member, level);
    }

    public Integer getPlayerLevel(Member member){
        return levels.get(member);
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

    // Initializes base values for a member
    public void initializePlayer(Member member){
        playerTimes.put(member, 0);
        playerXp.put(member, 0);
        levels.put(member, 1);
    }

    // If the member isnt present, they are a new player
    public boolean newPlayer(Member member){
        return !playerTimes.containsKey(member);
    }

    /*
    * This is where you can determine whether or not someone can earn xp
    * Useful for capping levels, punishing the unworthy, and moderation
     */
    public boolean canGetXp(Member member){
        // I think a string works here, not sure
        for (Role role : member.getRoles()){
            if (role.getName().equals("XP")){ return true; }
        }
        return false;
    }

    /*
    * Conditions for leveling up
    * Contains a scaling function for calculating xp required
     */
    private boolean checkLevelUp(Integer currentLevel, Integer currentXP){
        // Linear XP function
        return currentXP >= xpForNextLevel(currentLevel);
    }

    private double xpForNextLevel(int level){
        if (level == 0){
            return 500;
        }
        return (500 * level + xpForNextLevel(level - 1));
    }

    // User earns xp by staying in a voice channel
    public void startTimer(Member member){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                if (member.getVoiceState().inVoiceChannel()){
                    setPlayerXp(member, getPlayerXp(member) + genRandomXp(5, 15));
                    if (checkLevelUp(getPlayerLevel(member), getPlayerXp(member))){
                        levelUp(member);
                    }
                }
                else {
                    cancel();
                }
            }
        };
        // We don't want the xp to accrue immediately, but instead after the first tick
        timer.schedule(task, frequency * 1000, frequency * 1000);
    }

    // Updates player level and sends message to player
    private void levelUp(Member member) {
        setPlayerLevel(member, getPlayerLevel(member) + 1);
        member.getUser()
                .openPrivateChannel().complete()
                .sendMessageFormat("You have leveled up! You are now level %d", getPlayerLevel(member))
                .queue();

    }

    
}
