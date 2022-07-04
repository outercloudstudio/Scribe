package com.outercloud.scribe.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Config {
    public ConfigGroup masterGroup;
    public String filePath;

    public void Load(String relativePath){
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            if(!Files.exists(Paths.get("./config/" + relativePath))){
                masterGroup = new ConfigGroup(null, this);

                filePath = relativePath;

                return;
            }

            Reader reader = Files.newBufferedReader(Paths.get("./config/" + relativePath));

            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

            reader.close();

            filePath = relativePath;

            masterGroup = new ConfigGroup(json, this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void Save(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject jsonObject = masterGroup.ToJson();

        try {
            if(!Files.exists(Paths.get("./config/" + filePath))){
                new File("./config/" + filePath).createNewFile();
            }

            FileWriter fileWriter = new FileWriter("./config/" + filePath);

            fileWriter.write(gson.toJson(jsonObject));

            fileWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean HasKey(String key){
        return masterGroup.HasKey(key);
    }

    public void Update(String key, int value){
        masterGroup.Update(key, value);
    }

    public void Update(String key, ConfigGroup value){
        masterGroup.Update(key, value);
    }

    public void Remove(String key){
        masterGroup.Remove(key);
    }
}
