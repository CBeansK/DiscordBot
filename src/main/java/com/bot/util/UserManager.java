package com.bot.util;

import com.bot.Listener;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;

public class UserManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserManager.class);

    // keep list of user data
    private static HashMap<Long, UserData> userData;
    public UserManager(){
        userData = new HashMap<>();
    }

    public static UserData getUser(long id){
        return userData.get(id);
    }

    public boolean knowsUser(long id) {
        return userData.containsKey(id);
    }

    // get user data when needed
    public static UserData loadUserData(long id){
        //  if we don't have the user in memory then try to get from the file
        // if the user doesn't exist in the file then
        // we want to create new data and add them to the file
        if (userData.containsKey(id)){
            return userData.get(id);

        } else {
            UserData data = UserData.getFromFileOrDefault(id);
            userData.put(id, data);
            JSONUtils.writeToFile(data);
            return data;
        }
    }

}
