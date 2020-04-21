package com.bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

// Main class holder
public class Bot {

    // Builds a new bot object with authorization and listener
    // You can set the bot's current activity here
    private Bot() throws LoginException {

        new JDABuilder()
            .setToken(Config.get("token"))
            .addEventListeners(new Listener())
            .setActivity(Activity.listening("silence"))
            .build();

    }

    public static void main(String[] args) throws LoginException {
        new Bot();
    }
}

