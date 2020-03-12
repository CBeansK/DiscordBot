package com.bot;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private static final Dotenv dotenv = Dotenv.configure().directory("./src/main/resources/.env").load();

    // retrieve elements from environment
    public static String get(String key){
        return dotenv.get(key.toUpperCase());
    }
}
