package com.outercloud.scribe.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Config {
    public ConfigGroup masterGroup;
    public String filePath;

    public void Load(String relativePath){
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            if(!Files.exists(Paths.get("./config/" + relativePath))) throw new Exception("Config file " + relativePath + " not found!");

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
        
    }
}
