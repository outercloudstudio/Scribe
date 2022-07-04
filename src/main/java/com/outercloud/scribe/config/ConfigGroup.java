package com.outercloud.scribe.config;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConfigGroup {
    public ConfigGroup parent;
    public Config parentConfig;

    public Map<String, ConfigValue> values = new HashMap<String, ConfigValue>();

    public ConfigGroup (JsonObject group, ConfigGroup parent){
        this.parent = parent;

        if(group != null) Setup(group);
    }

    public ConfigGroup (JsonObject group, Config parentConfig){
        this.parentConfig = parentConfig;

        if(group != null) Setup(group);
    }

    private void Setup(JsonObject jsonObject){
        Iterator<String> keys = (Iterator<String>)jsonObject.keySet();

        while (keys.hasNext()){
            String key = keys.next();

            Object value = jsonObject.get(key);

            if(value instanceof JsonObject){
                values.put(key, new ConfigValue(new ConfigGroup((JsonObject)value, this)));
            } else if(value instanceof Number){
                values.put(key, new ConfigValue((Number)value));
            }
        }
    }

    public boolean HasKey(String key){
        return values.containsKey(key);
    }

    public void Save(){
        if(parent != null) parent.Save();
        if(parentConfig != null) parentConfig.Save();
    }

    public void Update(String key, int value){
        if(HasKey(key)) {
            values.replace(key, new ConfigValue(value));
        }else{
            values.put(key, new ConfigValue(value));
        }

        Save();
    }

    public void Update(String key, ConfigGroup value){
        if(HasKey(key)) {
            values.replace(key, new ConfigValue(value));
        }else{
            values.put(key, new ConfigValue(value));
        }

        Save();
    }

    public void Remove(String key){
        if(!HasKey(key)) return;

        values.remove(key);

        Save();
    }

    public JsonObject ToJson(){
        JsonObject jsonObject = new JsonObject();

        Iterator<String> keys = (Iterator<String>)values.keySet();

        while (keys.hasNext()){
            String key = keys.next();

            ConfigValue value = values.get(key);

            if (value.valueType == ConfigValue.ValueType.GROUP){
                jsonObject.addProperty(key, value.GetNumber());
            } else if (value.valueType == ConfigValue.ValueType.NUMBER) {
                jsonObject.add(key, value.GetGroup().ToJson());
            }
        }

        return jsonObject;
    }
}
