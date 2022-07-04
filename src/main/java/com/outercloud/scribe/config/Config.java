package com.outercloud.scribe.config;

import com.google.gson.*;
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

    public void Save(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonElement jsonElement = masterGroup.ToJson();

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

    public boolean HasKey(String key){
        return masterGroup.HasKey(key);
    }


    //Update
    public void Update(String key, Number value){
        masterGroup.Update(key, value);
    }

    public void Update(int index, Number value){
        masterGroup.Update(index, value);
    }

    public void Update(String key, String value){
        masterGroup.Update(key, value);
    }

    public void Update(int index, String value){
        masterGroup.Update(index, value);
    }

    public void Update(String key, Boolean value){
        masterGroup.Update(key, value);
    }

    public void Update(int index, Boolean value){
        masterGroup.Update(index, value);
    }

    public void Update(String key, ConfigGroup value){
        masterGroup.Update(key, value);
    }

    public void Update(int index, ConfigGroup value){
        masterGroup.Update(index, value);
    }

    public void UpdateEmtpyGroup(String key){
        masterGroup.UpdateEmtpyGroup(key);
    }

    public void UpdateEmtpyArray(String key){
        masterGroup.UpdateEmtpyArray(key);
    }

    public void UpdateEmtpyGroup(int index){
        masterGroup.UpdateEmtpyGroup(index);
    }

    public void UpdateEmtpyArray(int index){
        masterGroup.UpdateEmtpyArray(index);
    }


    //Insert
    public void Insert(int index, Number value){
        masterGroup.Insert(index, value);
    }

    public void Insert(int index, String value){
        masterGroup.Insert(index, value);
    }

    public void Insert(int index, Boolean value){
        masterGroup.Insert(index, value);
    }

    public void Insert(int index, ConfigGroup value){
        masterGroup.Insert(index, value);
    }

    public void InsertEmptyGroup(int index){
        masterGroup.InsertEmptyGroup(index);
    }

    public void InsertEmptyArray(int index){
        masterGroup.InsertEmptyArray(index);
    }


    //Default
    public void Default(String key, Number value){
        masterGroup.Default(key, value);
    }

    public void Default(int index, Number value){
        masterGroup.Default(index, value);
    }

    public void Default(String key, String value){
        masterGroup.Default(key, value);
    }

    public void Default(int index, String value){
        masterGroup.Default(index, value);
    }

    public void Default(String key, Boolean value){
        masterGroup.Default(key, value);
    }

    public void Default(int index, Boolean value){
        masterGroup.Default(index, value);
    }

    public void Default(String key, ConfigGroup value){
        masterGroup.Default(key, value);
    }

    public void Default(int index, ConfigGroup value){
        masterGroup.Default(index, value);
    }

    public void DefaultEmptyGroup(String key){
        masterGroup.DefaultEmptyGroup(key);
    }

    public void DefaultEmptyGroup(int index){
        masterGroup.DefaultEmptyGroup(index);
    }

    public void DefaultEmptyArray(String key){
        masterGroup.DefaultEmptyArray(key);
    }

    public void DefaultEmptyArray(int index){
        masterGroup.DefaultEmptyArray(index);
    }


    //Add
    public void Add(Number value){
        masterGroup.Add(value);
    }

    public void Add(String value){
        masterGroup.Add(value);
    }

    public void Add(Boolean value){
        masterGroup.Add(value);
    }

    public void AddEmtpyGroup(){
        masterGroup.AddEmtpyGroup();
    }

    public void AddEmtpyArray(){
       masterGroup.AddEmtpyGroup();
    }

    public void Add(ConfigGroup value){
        masterGroup.Add(value);
    }


    //Remove
    public void Remove(String key){
        masterGroup.Remove(key);
    }

    public void Remove(int index){
        masterGroup.Remove(index);
    }


    //Get
    public ConfigValue Get(String key){
        return masterGroup.Get(key);
    }

    public ConfigValue Get(int index){
        return masterGroup.Get(index);
    }

    public Number GetNumber(String key){
        return masterGroup.GetNumber(key);
    }

    public Number GetNumber(int index){
        return masterGroup.GetNumber(index);
    }

    public String GetString(String key){
        return masterGroup.GetString(key);
    }

    public String GetString(int index){
        return masterGroup.GetString(index);
    }

    public Boolean GetBool(String key){
        return masterGroup.GetBool(key);
    }

    public Boolean GetBool(int index){
        return masterGroup.GetBool(index);
    }

    public ConfigGroup GetGroup(String key){
        return masterGroup.GetGroup(key);
    }

    public ConfigGroup GetGroup(int index){
        return masterGroup.GetGroup(index);
    }

    public ConfigGroup GetArray(String key){
        return masterGroup.GetArray(key);
    }

    public ConfigGroup GetArray(int index){
        return masterGroup.GetArray(index);
    }
}
