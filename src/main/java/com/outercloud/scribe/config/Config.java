package com.outercloud.scribe.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.outercloud.scribe.Scribe;

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

            Scribe.LOGGER.info("Got To File Exists!");

            Reader reader = Files.newBufferedReader(Paths.get("./config/" + relativePath));

            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

            reader.close();

            Scribe.LOGGER.info("Read JSON!");

            filePath = relativePath;

            masterGroup = new ConfigGroup(json, this);

            Scribe.LOGGER.info("Created master group! " + filePath);
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

    public void UpdateEmptyGroup(String key){
        masterGroup.UpdateEmtpyGroup(key);
    }

    public ConfigValue Get(String key){
        return masterGroup.Get(key);
    }

    public Number GetNumber(String key){
        return masterGroup.GetNumber(key);
    }

    public ConfigGroup GetGroup(String key){
        return masterGroup.GetGroup(key);
    }

    public void Remove(String key){
        masterGroup.Remove(key);
    }

    public void Default(String key, int value){
        masterGroup.Default(key, value);
    }

    public void Default(String key, ConfigGroup value){
        masterGroup.Default(key, value);
    }

    public void DefaultEmptyGroup(String key){
        masterGroup.DefaultEmptyGroup(key);
    }
}
