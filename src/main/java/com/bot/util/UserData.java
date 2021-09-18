package com.bot.util;
import org.json.simple.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserData {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);

    public long getUserId() {
        return userId;
    }

    // user id
    // tbow attempts
    // tbows attained
    // amount of xp
    // level
    // tokens
    private long userId;
    private JSONObject tokenData;
    private int tbowAttempts;
    private int tbowsAttained;
    private int xp;
    private int level;

    public JSONObject getTokenData() {
        return tokenData;
    }

    public int getTbowAttempts() {
        return tbowAttempts;
    }

    public void setTbowAttempts(int tbowAttempts) {
        this.tbowAttempts = tbowAttempts;
    }

    public int getTbowsAttained() {
        return tbowsAttained;
    }

    public void setTbowsAttained(int tbowsAttained) {
        this.tbowsAttained = tbowsAttained;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTokenCount(String type){
        return (int) this.tokenData.get(type);
    }

    public void setTokenCount(String type, int num) {
        this.tokenData.put(type, num);
    }

    // should initialize new user with base stats
    public UserData(long id){
        this.userId = id;
        this.tokenData = initializeTokenData();
        this.tbowAttempts = 0;
        this.tbowsAttained = 0;
        this.xp = 0;
        this.level = 1;
    }

    public static UserData getFromFileOrDefault(long id){
        JSONObject data = JSONUtils.readFromFile(id);
        UserData userData = new UserData(id);
        if (data.isEmpty()){
           return userData;
        }

        userData.setXp(Integer.parseInt(data.get("xp").toString()));
        userData.setLevel(Integer.parseInt(data.get("level").toString()));


        userData.tokenData = (JSONObject) data.get("tokens");

        JSONObject tbowData = (JSONObject) data.get("tbowData");
        userData.setTbowAttempts(Integer.parseInt(tbowData.get("attempts").toString()));
        userData.setTbowsAttained(Integer.parseInt(tbowData.get("attained").toString()));

        return userData;
    }

    private JSONObject initializeTokenData(){
        JSONObject data = new JSONObject();
        data.put("mute", 0);
        data.put("disconnect", 0);

        return data;
    }


    /*
        {
            "userid": "11111111111111111",
            "tbow": {
                "attempts": 100,
                "attained": 3
                },
            "xp": 1000,
            "level": 5,
            "tokens": {
                "mute": 2,
                "disconnect": 3
                }
        }
     */
}
