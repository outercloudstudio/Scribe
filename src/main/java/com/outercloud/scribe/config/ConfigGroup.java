package com.outercloud.scribe.config;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import netscape.javascript.JSObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConfigGroup {
    public ConfigGroup parent;
    public Config parentConfig;

    public Map<String, ConfigValue> values = new HashMap<String, ConfigValue>();

    public ConfigGroup (JsonObject group, ConfigGroup parent){
        this.parent = parent;

        Setup(group);
    }

    public ConfigGroup (JsonObject group, Config parentConfig){
        this.parentConfig = parentConfig;

        Setup(group);
    }

    private void Setup(JsonObject jsonObject){
        Iterator<String> keys = (Iterator<String>) jsonObject.keySet();

        while (keys.hasNext()){
            String key = keys.next();

            Object value = jsonObject.get(key);

            if(value instanceof JsonObject){
                values.put(key, new ConfigValue(new ConfigGroup((JsonObject)value, this)));
            } else if(value instanceof Integer){
                values.put(key, new ConfigValue((int)value));
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
        if(!HasKey(key)) return;

        values.replace(key, new ConfigValue(value));

        Save();
    }
}
