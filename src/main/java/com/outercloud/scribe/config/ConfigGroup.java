package com.outercloud.scribe.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.outercloud.scribe.Scribe;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ConfigGroup {
    public ConfigGroup parent;
    public Config parentConfig;

    public boolean isArray;
    public int length;

    public Map<String, ConfigValue> values = new HashMap<String, ConfigValue>();

    public ConfigGroup (JsonObject group, ConfigGroup parent){
        this.parent = parent;

        if(group != null) Setup(group);
    }

    public ConfigGroup (JsonObject group, Config parentConfig){
        this.parentConfig = parentConfig;

        if(group != null) Setup(group);
    }

    public ConfigGroup (JsonArray array, ConfigGroup parent){
        this.parent = parent;

        this.isArray = true;

        if(array != null) Setup(array);
    }

    public ConfigGroup (JsonArray array, Config parentConfig){
        this.parentConfig = parentConfig;

        this.isArray = true;

        if(array != null) Setup(array);
    }

    private void Setup(JsonObject jsonObject){
        Iterator<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet().iterator();

        while (entries.hasNext()){
            Map.Entry<String, JsonElement> entry = entries.next();

            JsonElement value = jsonObject.get(entry.getKey());

            if(value.isJsonObject()){
                values.put(entry.getKey(), new ConfigValue(new ConfigGroup(value.getAsJsonObject(), this)));
            } else if(value.isJsonArray()){
                values.put(entry.getKey(), new ConfigValue(new ConfigGroup(value.getAsJsonArray(), this)));
            } else if(value.getAsJsonPrimitive().isNumber()){
                values.put(entry.getKey(), new ConfigValue(value.getAsNumber()));
            } else if(value.getAsJsonPrimitive().isString()){
                values.put(entry.getKey(), new ConfigValue(value.getAsString()));
            } else if(value.getAsJsonPrimitive().isBoolean()){
                values.put(entry.getKey(), new ConfigValue(value.getAsBoolean()));
            }
        }
    }

    private void Setup(JsonArray jsonArray){
        Iterator<JsonElement> elements = jsonArray.iterator();

        int index = 0;

        while (elements.hasNext()){
            JsonElement element = elements.next();

            if(element.isJsonObject()){
                values.put(String.valueOf(index), new ConfigValue(new ConfigGroup(element.getAsJsonObject(), this)));
            } else if(element.isJsonArray()){
                values.put(String.valueOf(index), new ConfigValue(new ConfigGroup(element.getAsJsonArray(), this)));
            } else if(element.getAsJsonPrimitive().isNumber()){
                values.put(String.valueOf(index), new ConfigValue(element.getAsNumber()));
            } else if(element.getAsJsonPrimitive().isString()){
                values.put(String.valueOf(index), new ConfigValue(element.getAsString()));
            } else if(element.getAsJsonPrimitive().isBoolean()){
                values.put(String.valueOf(index), new ConfigValue(element.getAsBoolean()));
            }

            index++;
        }

        length = index;
    }

    public boolean HasKey(String key){
        return values.containsKey(key);
    }

    public void Save(){
        if(parent != null) parent.Save();
        if(parentConfig != null) parentConfig.Save();
    }


    //Update
    public void Update(String key, Number value){
        if(isArray) return;

        if(HasKey(key)) {
            values.replace(key, new ConfigValue(value));
        }else{
            values.put(key, new ConfigValue(value));
        }

        Save();
    }

    public void Update(int index, Number value){
        if(!isArray) return;

        if(HasKey(String.valueOf(index))) {
            values.replace(String.valueOf(index), new ConfigValue(value));
        }else{
            values.put(String.valueOf(index), new ConfigValue(value));
        }

        Save();
    }

    public void Update(String key, String value){
        if(isArray) return;

        if(HasKey(key)) {
            values.replace(key, new ConfigValue(value));
        }else{
            values.put(key, new ConfigValue(value));
        }

        Save();
    }

    public void Update(int index, String value){
        if(!isArray) return;

        if(HasKey(String.valueOf(index))) {
            values.replace(String.valueOf(index), new ConfigValue(value));
        }else{
            values.put(String.valueOf(index), new ConfigValue(value));
        }

        Save();
    }

    public void Update(String key, Boolean value){
        if(isArray) return;

        if(HasKey(key)) {
            values.replace(key, new ConfigValue(value));
        }else{
            values.put(key, new ConfigValue(value));
        }

        Save();
    }

    public void Update(int index, Boolean value){
        if(!isArray) return;

        if(HasKey(String.valueOf(index))) {
            values.replace(String.valueOf(index), new ConfigValue(value));
        }else{
            values.put(String.valueOf(index), new ConfigValue(value));
        }

        Save();
    }

    public void Update(String key, ConfigGroup value){
        if(isArray) return;

        if(HasKey(key)) {
            values.replace(key, new ConfigValue(value));
        }else{
            values.put(key, new ConfigValue(value));
        }

        Save();
    }

    public void Update(int index, ConfigGroup value){
        if(!isArray) return;

        if(HasKey(String.valueOf(index))) {
            values.replace(String.valueOf(index), new ConfigValue(value));
        }else{
            values.put(String.valueOf(index), new ConfigValue(value));
        }

        Save();
    }

    public void UpdateEmtpyGroup(String key){
        if(isArray) return;

        if(HasKey(key)) {
            values.replace(key, new ConfigValue(new ConfigGroup((JsonObject)null, this)));
        }else{
            values.put(key, new ConfigValue(new ConfigGroup((JsonObject)null, this)));
        }

        Save();
    }

    public void UpdateEmtpyArray(String key){
        if(isArray) return;

        if(HasKey(key)) {
            values.replace(key, new ConfigValue(new ConfigGroup((JsonArray)null, this)));
        }else{
            values.put(key, new ConfigValue(new ConfigGroup((JsonArray)null, this)));
        }

        Save();
    }

    public void UpdateEmtpyGroup(int index){
        if(!isArray) return;

        if(HasKey(String.valueOf(index))) {
            values.replace(String.valueOf(index), new ConfigValue(new ConfigGroup((JsonObject)null, this)));
        }else{
            values.put(String.valueOf(index), new ConfigValue(new ConfigGroup((JsonObject)null, this)));
        }

        Save();
    }

    public void UpdateEmtpyArray(int index){
        if(HasKey(String.valueOf(index))) {
            values.replace(String.valueOf(index), new ConfigValue(new ConfigGroup((JsonArray)null, this)));
        }else{
            values.put(String.valueOf(index), new ConfigValue(new ConfigGroup((JsonArray)null, this)));
        }

        Save();
    }


    //Insert
    public void Insert(int index, Number value){
        if(!isArray) return;

        for (int i = index; i < length; i++) {
            ConfigValue oldValue = values.remove(String.valueOf(i));
            values.put(String.valueOf(i + 1), oldValue);
        }

        values.put(String.valueOf(index), new ConfigValue(value));

        length++;

        Save();
    }

    public void Insert(int index, String value){
        if(!isArray) return;

        for (int i = index; i < length; i++) {
            ConfigValue oldValue = values.remove(String.valueOf(i));
            values.put(String.valueOf(i + 1), oldValue);
        }

        values.put(String.valueOf(index), new ConfigValue(value));

        length++;

        Save();
    }

    public void Insert(int index, Boolean value){
        if(!isArray) return;

        for (int i = index; i < length; i++) {
            ConfigValue oldValue = values.remove(String.valueOf(i));
            values.put(String.valueOf(i + 1), oldValue);
        }

        values.put(String.valueOf(index), new ConfigValue(value));

        length++;

        Save();
    }

    public void Insert(int index, ConfigGroup value){
        if(!isArray) return;

        for (int i = index; i < length; i++) {
            ConfigValue oldValue = values.remove(String.valueOf(i));
            values.put(String.valueOf(i + 1), oldValue);
        }

        values.put(String.valueOf(index), new ConfigValue(value));

        length++;

        Save();
    }

    public void InsertEmptyGroup(int index){
        if(!isArray) return;

        for (int i = index; i < length; i++) {
            ConfigValue oldValue = values.remove(String.valueOf(i));
            values.put(String.valueOf(i + 1), oldValue);
        }

        values.put(String.valueOf(index), new ConfigValue(new ConfigGroup((JsonObject)null, this)));

        length++;

        Save();
    }

    public void InsertEmptyArray(int index){
        if(!isArray) return;

        for (int i = index; i < length; i++) {
            ConfigValue oldValue = values.remove(String.valueOf(i));
            values.put(String.valueOf(i + 1), oldValue);
        }

        values.put(String.valueOf(index), new ConfigValue(new ConfigGroup((JsonArray) null, this)));

        length++;

        Save();
    }


    //Default
    public void Default(String key, Number value){
        if(isArray) return;

        if(HasKey(key)) return;

        values.put(key, new ConfigValue(value));

        Save();
    }

    public void Default(int index, Number value){
        if(!isArray) return;

        if(index > length) return;

        if(index == length) length++;

        if(HasKey(String.valueOf(index))) return;

        values.put(String.valueOf(index), new ConfigValue(value));

        Save();
    }

    public void Default(String key, String value){
        if(isArray) return;

        if(HasKey(key)) return;

        values.put(key, new ConfigValue(value));

        Save();
    }

    public void Default(int index, String value){
        if(!isArray) return;

        if(index > length) return;

        if(index == length) length++;

        if(HasKey(String.valueOf(index))) return;

        values.put(String.valueOf(index), new ConfigValue(value));

        Save();
    }

    public void Default(String key, Boolean value){
        if(isArray) return;

        if(HasKey(key)) return;

        values.put(key, new ConfigValue(value));

        Save();
    }

    public void Default(int index, Boolean value){
        if(!isArray) return;

        if(index > length) return;

        if(index == length) length++;

        if(HasKey(String.valueOf(index))) return;

        values.put(String.valueOf(index), new ConfigValue(value));

        Save();
    }

    public void Default(String key, ConfigGroup value){
        if(isArray) return;

        if(HasKey(key)) return;

        values.put(key, new ConfigValue(value));

        Save();
    }

    public void Default(int index, ConfigGroup value){
        if(!isArray) return;

        if(index > length) return;

        if(index == length) length++;

        if(HasKey(String.valueOf(index))) return;

        values.put(String.valueOf(index), new ConfigValue(value));

        Save();
    }

    public void DefaultEmptyGroup(String key){
        if(isArray) return;

        if(HasKey(key)) return;

        values.put(key, new ConfigValue(new ConfigGroup((JsonObject)null, this)));

        Save();
    }

    public void DefaultEmptyGroup(int index){
        if(!isArray) return;

        if(HasKey(String.valueOf(index))) return;

        values.put(String.valueOf(index), new ConfigValue(new ConfigGroup((JsonObject)null, this)));

        Save();
    }

    public void DefaultEmptyArray(String key){
        if(isArray) return;

        if(HasKey(key)) return;

        values.put(key, new ConfigValue(new ConfigGroup((JsonArray)null, this)));

        Save();
    }

    public void DefaultEmptyArray(int index){
        if(isArray) return;

        if(HasKey(String.valueOf(index))) return;

        values.put(String.valueOf(index), new ConfigValue(new ConfigGroup((JsonArray)null, this)));

        Save();
    }


    //Add
    public void Add(Number value){
        if(!isArray) return;

        values.put(String.valueOf(length), new ConfigValue(value));

        Save();
    }

    public void Add(String value){
        if(!isArray) return;

        values.put(String.valueOf(length), new ConfigValue(value));

        Save();
    }

    public void Add(Boolean value){
        if(!isArray) return;

        values.put(String.valueOf(length), new ConfigValue(value));

        Save();
    }

    public void AddEmtpyGroup(){
        if(isArray) return;

        values.put(String.valueOf(length), new ConfigValue(new ConfigGroup((JsonObject)null, this)));

        Save();
    }

    public void AddEmtpyArray(){
        if(isArray) return;

        values.put(String.valueOf(length), new ConfigValue(new ConfigGroup((JsonArray) null, this)));

        Save();
    }

    public void Add(ConfigGroup value){
        if(!isArray) return;

        values.put(String.valueOf(length), new ConfigValue(value));

        Save();
    }


    //Remove
    public void Remove(String key){
        if(!HasKey(key)) return;

        values.remove(key);

        Save();
    }

    public void Remove(int index){
        if(!HasKey(String.valueOf(index))) return;

        values.remove(String.valueOf(index));

        for (int i = index + 1; i < length; i++) {
            ConfigValue oldValue = values.remove(String.valueOf(i));
            values.put(String.valueOf(i - 1), oldValue);
        }

        length--;

        Save();
    }


    //Get
    public ConfigValue Get(String key){
        if(!values.containsKey(key)) return null;

        return values.get(key);
    }

    public ConfigValue Get(int index){
        if(!values.containsKey(String.valueOf(index))) return null;

        return values.get(String.valueOf(index));
    }

    public Number GetNumber(String key){
        if(!values.containsKey(key)) return 0;

        return values.get(key).GetNumberValue();
    }

    public Number GetNumber(int index){
        if(!values.containsKey(String.valueOf(index))) return 0;

        return values.get(String.valueOf(index)).GetNumberValue();
    }

    public String GetString(String key){
        if(!values.containsKey(key)) return "";

        return values.get(key).GetStringValue();
    }

    public String GetString(int index){
        if(!values.containsKey(String.valueOf(index))) return "";

        return values.get(String.valueOf(index)).GetStringValue();
    }

    public Boolean GetBool(String key){
        if(!values.containsKey(key)) return false;

        return values.get(key).GetBoolValue();
    }

    public Boolean GetBool(int index){
        if(!values.containsKey(String.valueOf(index))) return false;

        return values.get(String.valueOf(index)).GetBoolValue();
    }

    public ConfigGroup GetGroup(String key){
        if(!values.containsKey(key)) return null;

        return values.get(key).GetGroupValue();
    }

    public ConfigGroup GetGroup(int index){
        if(!values.containsKey(String.valueOf(index))) return null;

        return values.get(String.valueOf(index)).GetGroupValue();
    }

    public ConfigGroup GetArray(String key){
        if(!values.containsKey(key)) return null;

        return values.get(key).GetArrayValue();
    }

    public ConfigGroup GetArray(int index){
        if(!values.containsKey(String.valueOf(index))) return null;

        return values.get(String.valueOf(index)).GetArrayValue();
    }


    public JsonElement ToJson(){
        if (!isArray) {
            JsonObject jsonObject = new JsonObject();

            Iterator<Map.Entry<String, ConfigValue>> entries = values.entrySet().iterator();

            while (entries.hasNext()){
                Map.Entry<String, ConfigValue> entry = entries.next();

                ConfigValue value = entry.getValue();

                if (value.valueType == ConfigValue.ValueType.GROUP){
                    jsonObject.add(entry.getKey(), value.GetGroupValue().ToJson());
                } else if (value.valueType == ConfigValue.ValueType.NUMBER) {
                    jsonObject.addProperty(entry.getKey(), value.GetNumberValue());
                } else if (value.valueType == ConfigValue.ValueType.ARRAY) {
                    jsonObject.add(entry.getKey(), value.GetArrayValue().ToJson());
                } else if (value.valueType == ConfigValue.ValueType.STRING) {
                    jsonObject.addProperty(entry.getKey(), value.GetStringValue());
                } else if (value.valueType == ConfigValue.ValueType.BOOL) {
                    jsonObject.addProperty(entry.getKey(), value.GetBoolValue());
                }
            }

            return jsonObject;
        }else {
            JsonArray jsonArray = new JsonArray();

            for (int i = 0; i < length; i++) {
                ConfigValue value = values.get(String.valueOf(i));

                if (value.valueType == ConfigValue.ValueType.GROUP){
                    jsonArray.add(value.GetGroupValue().ToJson());
                } else if (value.valueType == ConfigValue.ValueType.NUMBER) {
                    jsonArray.add(value.GetNumberValue());
                } else if (value.valueType == ConfigValue.ValueType.ARRAY) {
                    jsonArray.add(value.GetArrayValue().ToJson());
                } else if (value.valueType == ConfigValue.ValueType.STRING) {
                    jsonArray.add(value.GetStringValue());
                } else if (value.valueType == ConfigValue.ValueType.BOOL) {
                    jsonArray.add(value.GetBoolValue());
                }
            }

            return jsonArray;
        }
    }
}
