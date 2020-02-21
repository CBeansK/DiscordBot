package com.bot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Bot {

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

