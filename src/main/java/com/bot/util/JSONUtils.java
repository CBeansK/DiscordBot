package com.bot.util;

import com.bot.Listener;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JSONUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);

    public synchronized static JSONObject readFromFile(long userId){

        JSONParser parser = new JSONParser();

        try {
            FileReader reader = new FileReader("userdata.json");

            Object obj = parser.parse(reader);

            reader.close();
            JSONArray data = (JSONArray) obj;

            for(int i = 0; i < data.size(); i++) {
                JSONObject jsonObject = (JSONObject) data.get(i);
                if (jsonObject.get("id").equals(userId)) {
                    LOGGER.info("Loaded user data id from file.");
                    return jsonObject;
                }
            }

        } catch (FileNotFoundException e) {
            LOGGER.error("Could not open userdata file");
            LOGGER.error(e.getMessage());
        } catch (IOException | ParseException e) {
            LOGGER.warn("Could not parse userdata file. File may be empty.");
        }
        return new JSONObject();
    }

    private static JSONArray readAllData(){
        JSONParser parser = new JSONParser();

        try {
            FileReader reader = new FileReader("userdata.json");

            Object obj = parser.parse(reader);

            reader.close();
            JSONArray data = (JSONArray) obj;

            return data;

        } catch (FileNotFoundException e) {
            LOGGER.error("Could not open userdata file");
            LOGGER.error(e.getMessage());
        } catch (IOException | ParseException e) {
            LOGGER.warn("Could not parse userdata file. File may be empty.");
        }
        return new JSONArray();
    }

    public synchronized static void writeToFile(UserData data) {

        // check if there is current data we can overwrite
        long id = data.getUserId();
        JSONObject existingData = readFromFile(id);

        // convert user data to json
        JSONObject json = fromUserData(data);

        // get data array from json file
        JSONArray currentData = readAllData();


        if (existingData.isEmpty()){
            currentData.add(json);
        } else {
            currentData.set(currentData.indexOf(existingData), json);
        }

        // write to file
        try {
            FileWriter writer = new FileWriter("userdata.json");
            writer.write(currentData.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject fromUserData(UserData data){
        JSONObject json = new JSONObject();
        json.put("id", data.getUserId());
        json.put("tokenData", data.getTokenData());
        json.put("level", data.getLevel());
        json.put("xp", data.getXp());

        int tbowAttempts = data.getTbowAttempts();
        int tbowsAttained = data.getTbowsAttained();

        JSONObject tbowData = new JSONObject();
        tbowData.put("attempts", tbowAttempts);
        tbowData.put("attained", tbowsAttained);

        json.put("tbowData", tbowData);

        return json;
    }
}
