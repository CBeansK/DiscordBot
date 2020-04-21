package com.bot;

import io.github.cdimascio.dotenv.Dotenv;

/*
*   @class Config
*   Basic config class for retrieving environment variables.
*   There are two different ways, one retrieves from a .env file in project directory
*   The other uses system variables for when we build into a docker container.
 */
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
