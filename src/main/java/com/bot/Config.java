package com.bot;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {


    // For Testing
    private static final Dotenv dotenv = Dotenv.configure().load();

    // retrieve elements from environment
    public static String get(String key){
        return dotenv.get(key.toUpperCase());
    }
    /*

     */



    // For Production

    /*
public static String get(String key){
        return System.getenv(key.toUpperCase());
    }
    */
}
