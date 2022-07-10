package com.outercloud.scribe.config;

import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Config {
    public ConfigGroup masterGroup;
    public String filePath;

    public void load(String relativePath){
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            if(!Files.exists(Paths.get("./config/" + relativePath))){
                masterGroup = new ConfigGroup((JsonObject)null, this);

                filePath = relativePath;

                return;
            }

            Reader reader = Files.newBufferedReader(Paths.get("./config/" + relativePath));

            JsonElement jsonElement = JsonParser.parseReader(reader);

            reader.close();

            filePath = relativePath;

            if(jsonElement.isJsonObject()){
                masterGroup = new ConfigGroup((JsonObject)jsonElement, this);
            }else if(jsonElement.isJsonArray()) {
                masterGroup = new ConfigGroup((JsonArray)jsonElement, this);
            }else {
                throw new Exception("Read strange type???");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void save(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonElement jsonElement = masterGroup.toJson();

        try {
            if(!Files.exists(Paths.get("./config/" + filePath))){
                new File("./config/" + filePath).createNewFile();
            }

            FileWriter fileWriter = new FileWriter("./config/" + filePath);

            fileWriter.write(gson.toJson(jsonElement));

            fileWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean hasKey(String key){
        return masterGroup.hasKey(key);
    }


    //Update
    public void update(String key, Number value){
        masterGroup.update(key, value);
    }

    public void update(int index, Number value){
        masterGroup.update(index, value);
    }

    public void update(String key, String value){
        masterGroup.update(key, value);
    }

    public void update(int index, String value){
        masterGroup.update(index, value);
    }

    public void update(String key, Boolean value){
        masterGroup.update(key, value);
    }

    public void update(int index, Boolean value){
        masterGroup.update(index, value);
    }

    public void update(String key, ConfigGroup value){
        masterGroup.update(key, value);
    }

    public void update(int index, ConfigGroup value){
        masterGroup.update(index, value);
    }

    public void updateEmtpyGroup(String key){
        masterGroup.updateEmtpyGroup(key);
    }

    public void UpdateEmtpyArray(String key){
        masterGroup.updateEmtpyArray(key);
    }

    public void updateEmtpyGroup(int index){
        masterGroup.updateEmtpyGroup(index);
    }

    public void UpdateEmtpyArray(int index){
        masterGroup.updateEmtpyArray(index);
    }


    //Insert
    public void insert(int index, Number value){
        masterGroup.insert(index, value);
    }

    public void insert(int index, String value){
        masterGroup.insert(index, value);
    }

    public void insert(int index, Boolean value){
        masterGroup.insert(index, value);
    }

    public void insert(int index, ConfigGroup value){
        masterGroup.insert(index, value);
    }

    public void insertEmptyGroup(int index){
        masterGroup.insertEmptyGroup(index);
    }

    public void insertEmptyArray(int index){
        masterGroup.insertEmptyArray(index);
    }


    //Default
    public void setDefault(String key, Number value){
        masterGroup.setDefault(key, value);
    }

    public void setDefault(int index, Number value){
        masterGroup.setDefault(index, value);
    }

    public void setDefault(String key, String value){
        masterGroup.setDefault(key, value);
    }

    public void setDefault(int index, String value){
        masterGroup.setDefault(index, value);
    }

    public void setDefault(String key, Boolean value){
        masterGroup.setDefault(key, value);
    }

    public void setDefault(int index, Boolean value){
        masterGroup.setDefault(index, value);
    }

    public void setDefault(String key, ConfigGroup value){
        masterGroup.setDefault(key, value);
    }

    public void setDefault(int index, ConfigGroup value){
        masterGroup.setDefault(index, value);
    }

    public void setDefaultEmptyGroup(String key){
        masterGroup.setDefaultEmptyGroup(key);
    }

    public void setDefaultEmptyGroup(int index){
        masterGroup.setDefaultEmptyGroup(index);
    }

    public void setDefaultEmptyArray(String key){
        masterGroup.setDefaultEmptyArray(key);
    }

    public void setDefaultEmptyArray(int index){
        masterGroup.setDefaultEmptyArray(index);
    }


    //Add
    public void add(Number value){
        masterGroup.add(value);
    }

    public void add(String value){
        masterGroup.add(value);
    }

    public void add(Boolean value){
        masterGroup.add(value);
    }

    public void add(ConfigGroup value){
        masterGroup.add(value);
    }

    public void addEmtpyGroup(){
        masterGroup.addEmtpyGroup();
    }

    public void addEmtpyArray(){
       masterGroup.addEmtpyGroup();
    }


    //Remove
    public void remove(String key){
        masterGroup.remove(key);
    }

    public void remove(int index){
        masterGroup.remove(index);
    }


    //Get
    public ConfigValue get(String key){
        return masterGroup.get(key);
    }

    public ConfigValue get(int index){
        return masterGroup.get(index);
    }

    public Number getNumber(String key){
        return masterGroup.getNumber(key);
    }

    public Number getNumber(int index){
        return masterGroup.getNumber(index);
    }

    public String getString(String key){
        return masterGroup.getString(key);
    }

    public String getString(int index){
        return masterGroup.getString(index);
    }

    public Boolean getBool(String key){
        return masterGroup.getBool(key);
    }

    public Boolean getBool(int index){
        return masterGroup.getBool(index);
    }

    public ConfigGroup getGroup(String key){
        return masterGroup.getGroup(key);
    }

    public ConfigGroup getGroup(int index){
        return masterGroup.getGroup(index);
    }

    public ConfigGroup getArray(String key){
        return masterGroup.getArray(key);
    }

    public ConfigGroup getArray(int index){
        return masterGroup.getArray(index);
    }
}
