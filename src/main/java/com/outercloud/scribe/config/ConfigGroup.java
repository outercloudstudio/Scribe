package com.outercloud.scribe.config;

import com.google.gson.JsonElement;
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
        Iterator<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet().iterator();

        while (entries.hasNext()){
            Map.Entry<String, JsonElement> entry = entries.next();

            JsonElement value = jsonObject.get(entry.getKey());

            if(value.isJsonObject()){
                values.put(entry.getKey(), new ConfigValue(new ConfigGroup(value.getAsJsonObject(), this)));
            } else if(value.isJsonPrimitive() && value.getAsJsonPrimitive().isNumber()){
                values.put(entry.getKey(), new ConfigValue(value.getAsNumber()));
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

    public void Default(String key, int value){
        if(HasKey(key)) return;

        values.put(key, new ConfigValue(value));

        Save();
    }

    public void Default(String key, ConfigGroup value){
        if(HasKey(key)) return;

        values.put(key, new ConfigValue(value));

        Save();
    }

    public void DefaultEmptyGroup(String key){
        if(HasKey(key)) return;

        values.put(key, new ConfigValue(new ConfigGroup(null, this)));

        Save();
    }

    public void UpdateEmtpyGroup(String key){
        if(HasKey(key)) {
            values.replace(key, new ConfigValue(new ConfigGroup(null, this)));
        }else{
            values.put(key, new ConfigValue(new ConfigGroup(null, this)));
        }

        Save();
    }

    public void Remove(String key){
        if(!HasKey(key)) return;

        values.remove(key);

        Save();
    }

    public ConfigValue Get(String key){
        if(!values.containsKey(key)) return null;

        return values.get(key);
    }

    public Number GetNumber(String key){
        if(!values.containsKey(key)) return 0;

        return values.get(key).GetNumberValue();
    }

    public ConfigGroup GetGroup(String key){
        if(!values.containsKey(key)) return null;

        return values.get(key).GetGroupValue();
    }

    public JsonObject ToJson(){
        JsonObject jsonObject = new JsonObject();

        Iterator<Map.Entry<String, ConfigValue>> entries = values.entrySet().iterator();

        while (entries.hasNext()){
            Map.Entry<String, ConfigValue> entry = entries.next();

            ConfigValue value = entry.getValue();

            if (value.valueType == ConfigValue.ValueType.GROUP){
                jsonObject.add(entry.getKey(), value.GetGroupValue().ToJson());
            } else if (value.valueType == ConfigValue.ValueType.NUMBER) {
                jsonObject.addProperty(entry.getKey(), value.GetNumberValue());
            }
        }

        return jsonObject;
    }
}
